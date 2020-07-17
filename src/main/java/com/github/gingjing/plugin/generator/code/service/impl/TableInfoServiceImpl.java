package com.github.gingjing.plugin.generator.code.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.common.utils.PluginSqlUtil;
import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.entity.*;
import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.*;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Conditions;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.containers.JBIterable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.gingjing.plugin.generator.code.constants.MsgValue.DATABASE_CONFIG_TYPE_NOT_EXIST;
import static com.github.gingjing.plugin.generator.code.constants.MsgValue.TYPE_VALIDATOR;

;

/**
 * 表信息服务类
 *
 * @author gingjingdm
 * @version 1.0.0
 * @since 2018/09/02 12:13
 */
public class TableInfoServiceImpl implements TableInfoService {
    /**
     * 项目对象
     */
    private Project project;

    /**
     * 命名工具类
     */
    private NameUtils nameUtils;
    /**
     * jackson格式化工具
     */
    private ObjectMapper objectMapper;
    /**
     * 文件工具类
     */
    private FileUtils fileUtils;

    /**
     * 保存的相对路径
     */
    private static final String SAVE_PATH = "/.idea/GenerateCodeConfig";

    public TableInfoServiceImpl(Project project) {
        this.project = project;
        this.nameUtils = NameUtils.getInstance();
        this.objectMapper = new ObjectMapper();
        this.fileUtils = FileUtils.getInstance();
    }


    @Override
    public TableInfo getTableInfoByModel(PsiClass javaClass) {
        if (javaClass == null) {
            return null;
        }

        DbTable dbTable = ProjectUtils.getDbTableByPsiClass(project, javaClass);
        if (dbTable == null) {
//            int confirmationDialog = Messages.showTwoStepConfirmationDialog(
//                    "IntelliJ IDEA DatabaseTool doesn't find this table ==>{%s}. " +
//                            "Maybe curse of you have not use it as a tool to connect your database or ocurrs some exception during the connection. " +
//                            "Do you continue making code?",
//                    "CodeFlutter Generator: ",
//                    "If you choose continue, you can't want to debug this code you will generate  Confirm continue?", Messages.getQuestionIcon());
            return parseTableInfoByClass(javaClass);
        } else {
            return parseTableInfoByDbTable(dbTable);
        }
    }


    @Override
    public TableInfo parseTableInfoByClass(PsiClass psiClass) {
        TableInfo tableInfo = new TableInfo();
        // Doc注释
        if (psiClass.getDocComment() != null) {
            tableInfo.setComment(psiClass.getDocComment().getText());
        }
        // 设置类名
        tableInfo.setName(psiClass.getName());
        // 设置所有列
        tableInfo.setFullColumn(new ArrayList<>());
        // 设置主键列
        tableInfo.setPkColumn(new ArrayList<>());
        // 设置其他列
        tableInfo.setOtherColumn(new ArrayList<>());

        PsiField[] allFields = psiClass.getAllFields();
        if (ArrayUtil.isEmpty(allFields)) {
            return tableInfo;
        }
        for (PsiField psiField : allFields) {
            ColumnInfo columnInfo = new ColumnInfo();
            // 列类型
            columnInfo.setType(psiField.getType().getCanonicalText());
            // Doc注释
            if (psiField.getDocComment() != null) {
                columnInfo.setComment(psiField.getDocComment().getText());
            }
            // 默认为false
            columnInfo.setPk(false);
            // 列名
            columnInfo.setName(psiField.getName());
            // 短类型
            columnInfo.setShortType(psiField.getType().getPresentableText());
            // 扩展项
            columnInfo.setExt(new LinkedHashMap<>());
            // 添加到全部列
            tableInfo.getFullColumn().add(columnInfo);
        }
        return tableInfo;
    }

    @Override
    public TableInfo getTableInfoBySql(String createSql) {
        if (StringUtils.isEmpty(createSql) || (!createSql.contains("create") && !createSql.contains("CREATE"))) {
            return null;
        }
        return parseTableInfoBySql(createSql);
    }

    private TableInfo parseTableInfoBySql(String currCreateSql) {
        try {
            return PluginSqlUtil.initTableAndColumn(currCreateSql, CacheDataUtils.getInstance().getCurrDbType());
        } catch (Exception e) {
            return new TableInfo();
        }
    }

    @Override
    public TableInfo getTableInfoByDbTable(DbTable dbTable) {
        if (dbTable == null) {
            return null;
        }
        // 设置原属对象
        return parseTableInfoByDbTable(dbTable);
    }

    /**
     * 通过DbTable获取表信息
     *
     * @param dbTable 数据库表
     */
    private TableInfo parseTableInfoByDbTable(DbTable dbTable) {
        TableInfo tableInfo = new TableInfo();
        // 设置原属对象
        tableInfo.setObj(dbTable);
        // 设置类名
        tableInfo.setName(nameUtils.getClassName(dbTable.getName()));
        // 设置注释
        tableInfo.setComment(dbTable.getComment());
        // 设置所有列
        tableInfo.setFullColumn(new ArrayList<>());
        // 设置主键列
        tableInfo.setPkColumn(new ArrayList<>());
        // 设置其他列
        tableInfo.setOtherColumn(new ArrayList<>());
        // 处理所有列
        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dbTable);
        for (DasColumn column : columns) {
            ColumnInfo columnInfo = new ColumnInfo();
            // 原始列对象
            columnInfo.setObj(column);
            // 列类型
            columnInfo.setType(getColumnType(column.getDataType().getSpecification()));
            // 短类型
            columnInfo.setShortType(nameUtils.getClsNameByFullName(columnInfo.getType()));
            // 列名
            columnInfo.setName(nameUtils.getJavaName(column.getName()));
            // 列注释
            columnInfo.setComment(column.getComment());
            // 扩展项
            columnInfo.setExt(new LinkedHashMap<>());
            // 添加到全部列
            tableInfo.getFullColumn().add(columnInfo);
            // 主键列添加到主键列，否则添加到其他列
            if (DasUtil.isPrimary(column)) {
                columnInfo.setPk(true);
                tableInfo.getPkColumn().add(columnInfo);
            } else {
                columnInfo.setPk(false);
                tableInfo.getOtherColumn().add(columnInfo);
            }
        }
        return tableInfo;
    }


    /**
     * 获取数据库中的表树
     *
     * @param dataSource 数据库
     * @return 表迭代器
     */
    private JBIterable<DasTable> getTables(DbDataSource dataSource) {
        return dataSource.getModel().traverser().expandAndSkip(Conditions.instanceOf(DasNamespace.class)).filter(DasTable.class);
    }

    @Override
    public TableInfo getTableInfoAndConfig(PsiClass javaClass) {
        TableInfo tableInfo = this.getTableInfoByModel(javaClass);
        // 加载配置
        this.loadConfig(tableInfo);
        return tableInfo;
    }

    @Override
    public TableInfo getTableInfoAndConfig(String createSql) {
        TableInfo tableInfo = this.getTableInfoBySql(createSql);
        // 加载配置
        this.loadConfig(tableInfo);
        return tableInfo;
    }

    @Override
    public TableInfo getTableInfoAndConfig(DbTable dbTable) {
        TableInfo tableInfo = this.getTableInfoByDbTable(dbTable);
        // 加载配置
        this.loadConfig(tableInfo);
        return tableInfo;
    }

    /**
     * 加载单个表信息配置(用户自定义列与扩张选项)
     *
     * @param tableInfo 表信息对象
     */
    private void loadConfig(TableInfo tableInfo) {
        if (tableInfo == null) {
            return;
        }

        // 读取配置文件中的表信息
        TableInfo tableInfoConfig = read(tableInfo);
        // 返回空直接不处理
        if (tableInfoConfig == null) {
            return;
        }

        // 开始合并数据
        // 选择模型名称
        tableInfo.setSaveModelName(tableInfoConfig.getSaveModelName());
        // 选择的包名
        tableInfo.setSavePackageName(tableInfoConfig.getSavePackageName());
        // 选择的保存路径
        tableInfo.setSavePath(tableInfoConfig.getSavePath());
        // 选择的表名前缀
        tableInfo.setPreName(tableInfoConfig.getPreName());
        // 选择的模板组
        tableInfo.setTemplateGroupName(tableInfoConfig.getTemplateGroupName());

        // 没有列时不处理
        if (CollectionUtil.isEmpty(tableInfoConfig.getFullColumn())) {
            return;
        }

        int fullSize = tableInfoConfig.getFullColumn().size();
        // 所有列
        List<ColumnInfo> fullColumn = new ArrayList<>(fullSize);

        int pkSize = tableInfo.getPkColumn().size();
        // 主键列
        List<ColumnInfo> pkColumn = new ArrayList<>(pkSize);

        // 其他列
        List<ColumnInfo> otherColumn = new ArrayList<>(fullSize - pkSize);

        for (ColumnInfo column : tableInfo.getFullColumn()) {
            boolean exists = false;
            for (ColumnInfo configColumn : tableInfoConfig.getFullColumn()) {
                if (Objects.equals(configColumn.getName(), column.getName())) {
                    exists = true;

                    // 覆盖空值
                    if (configColumn.getType() == null) {
                        configColumn.setType(column.getType());
                    }

                    // 短类型
                    if (!StringUtils.isEmpty(configColumn.getType())) {
                        configColumn.setShortType(nameUtils.getClsNameByFullName(configColumn.getType()));
                    }

                    // 表注释覆盖
                    if (configColumn.getComment() == null) {
                        configColumn.setComment(column.getComment());
                    }

                    // 主键属性覆盖（针对sql、model生成代码）
                    configColumn.setPk(column.isPk());

                    // 列对象覆盖
                    configColumn.setObj(column.getObj());

                    // 添加至新列表中
                    fullColumn.add(configColumn);
                    // 是否为主键
                    if (DasUtil.isPrimary(configColumn.getObj()) || configColumn.isPk()) {
                        pkColumn.add(configColumn);
                    } else {
                        otherColumn.add(configColumn);
                    }
                    break;
                }
            }
            // 新增的列
            if (!exists) {
                fullColumn.add(column);
            }
        }
        // 添加附加列
        for (ColumnInfo configColumn : tableInfoConfig.getFullColumn()) {
            if (configColumn.isCustom()) {
                fullColumn.add(configColumn);
            }
        }

        // 全部覆盖
        tableInfo.setFullColumn(fullColumn);
        tableInfo.setPkColumn(pkColumn);
        tableInfo.setOtherColumn(otherColumn);
    }

    /**
     * 通过映射获取对应的java类型类型名称
     *
     * @param typeName 列类型
     * @return java类型
     */
    private String getColumnType(String typeName) {
        return PluginSqlUtil.getColumnType(typeName);
    }

    /**
     * 检查并引导用户去配置条件类型
     *
     * @param typeMapperList 类型映射对象列表
     * @param typeNames      字段类型字符串列表
     * @return 是否添加映射
     */
    private boolean checkIfAddTypeMapper(List<TypeMapper> typeMapperList, List<String> typeNames) {
        FLAG:
        for (String typeName : typeNames) {
            for (TypeMapper typeMapper : typeMapperList) {
                // 不区分大小写查找类型
                if (Pattern.compile(typeMapper.getColumnType(), Pattern.CASE_INSENSITIVE).matcher(typeName).matches()
                        || typeMapper.getJavaType().equalsIgnoreCase(typeName)) {
                    continue FLAG;
                }
            }
            // 没找到类型，引导用户去添加类型
            if (MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, String.format(DATABASE_CONFIG_TYPE_NOT_EXIST, typeName)).isYes()) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, "Type Mapper");
                return false;
            }
            // 用户取消添加
            return true;
        }
        return true;
    }

    /**
     * 类型校验
     *
     * @param psiClass 选中的javabean类
     * @return 是否验证通过
     */
    public boolean typeValidator(PsiClass psiClass) {
        PsiField[] allFields = psiClass.getAllFields();
        List<TypeMapper> typeMapperList = CurrGroupUtils.getCurrTypeMapperGroup().getElementList();
        if (ArrayUtil.isEmpty(allFields)) {
            Messages.showErrorDialog(String.format("该java类%s，没有字段，请先添加！", psiClass.getQualifiedName()), TYPE_VALIDATOR);
            return false;
        }
        List<String> typeNames = Arrays.stream(allFields).map(f -> f.getType().getCanonicalText()).collect(Collectors.toList());
       /* for (PsiField field : allFields) {
            PsiType type = field.getType();
            String typeName = type.getPresentableText();*/
        return checkIfAddTypeMapper(typeMapperList, typeNames);
    }

    /**
     * 类型校验
     *
     * @param dbTable 原始表对象
     * @return 是否验证通过
     */
    @Override
    public boolean typeValidator(DbTable dbTable) {
        // 处理所有列
        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dbTable);
        List<TypeMapper> typeMapperList = CurrGroupUtils.getCurrTypeMapperGroup().getElementList();
        List<String> typeNames = new ArrayList<>();
        columns.forEach(c -> typeNames.add(c.getDataType().getSpecification()));
        /*for (DasColumn column : columns) {
            String typeName = column.getDataType().getSpecification();*/
        return checkIfAddTypeMapper(typeMapperList, typeNames);
    }

    /**
     * 保存数据
     *
     * @param tableInfo 表信息对象
     */
    @Override
    public void save(TableInfo tableInfo) {
        if (tableInfo == null) {
            return;
        }
        // 获取未修改前的原数据
        TableInfo oldTableInfo = getTableInfo();
        // 克隆对象，防止串改，同时原始对象丢失
        tableInfo = CloneUtils.cloneByJson(tableInfo, false);
        //排除部分字段，这些字段不进行保存
        tableInfo.setOtherColumn(null);
        tableInfo.setPkColumn(null);
        // 获取迭代器
        Iterator<ColumnInfo> columnIterable = tableInfo.getFullColumn().iterator();
        while (columnIterable.hasNext()) {
            Iterator<ColumnInfo> oldColumnIterable = oldTableInfo.getFullColumn().iterator();
            // 新列
            ColumnInfo columnInfo = columnIterable.next();
            // 是否存在
            boolean exists = false;
            while (oldColumnIterable.hasNext()) {
                ColumnInfo oldColumnInfo = oldColumnIterable.next();
                // 不同列直接返回跳过
                if (!Objects.equals(columnInfo.getName(), oldColumnInfo.getName())) {
                    continue;
                }
                // 类型排除
                if (Objects.equals(columnInfo.getType(), oldColumnInfo.getType())) {
                    columnInfo.setType(null);
                }
                // 注释排除
                if (Objects.equals(columnInfo.getComment(), oldColumnInfo.getComment())) {
                    columnInfo.setComment(null);
                }
                // 不保存短类型
                columnInfo.setShortType(null);
                // 列存在，进行处理
                exists = true;
                break;
            }
            // 扩展项不能为空
            if (columnInfo.getExt() == null) {
                columnInfo.setExt(new LinkedHashMap<>());
            }
            // 已经不存在的非自定义列直接删除
            if (!exists && !columnInfo.isCustom()) {
                columnIterable.remove();
            }
        }
        // 获取优雅格式的JSON字符串
        String content = null;
        try {
            content = PluginJsonUtil.formatByJackson(tableInfo);
        } catch (JsonProcessingException e) {
            ExceptionUtil.rethrow(e);
        }
        if (content == null) {
            Messages.showWarningDialog("保存失败，JSON序列化错误。", MsgValue.TITLE_INFO);
            return;
        }
        // 获取或创建保存目录
        String path = project.getBasePath() + SAVE_PATH;
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Messages.showWarningDialog("保存失败，无法创建目录。", MsgValue.TITLE_INFO);
                return;
            }
        }
        // 获取保存文件
        new SaveFile(project, dir.getAbsolutePath(), getConfigFileName(oldTableInfo), content, true, false).write();
    }

    /**
     * 获取当前模式下tableInfo实体，此表信息可能不完整。
     *
     * @return 表信息
     */
    private TableInfo getTableInfo() {
        CreateModeEnum createMode = CacheDataUtils.getInstance().getCreateMode();
        switch (createMode) {
            case SELECT_MODEL:
                return parseTableInfoByClass(CacheDataUtils.getInstance().getSelectClass());
            case CREATE_SQL:
                return parseTableInfoBySql(CacheDataUtils.getInstance().getCurrCreateSql());
            default:
                return parseTableInfoByDbTable(CacheDataUtils.getInstance().getSelectDbTable());
        }
    }

    /**
     * 读取配置文件
     *
     * @param tableInfo 表信息对象
     * @return 读取到的配置信息
     */
    private TableInfo read(TableInfo tableInfo) {
        // 获取保存的目录
        String path = project.getBasePath() + SAVE_PATH;
        File dir = new File(path);
        // 获取保存的文件
        File file = new File(dir, getConfigFileName(tableInfo));
        // 文件不存在时直接返回
        if (!file.exists()) {
            return null;
        }
        // 读取并解析文件
        String json = fileUtils.read(project, file);
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return parser(json);
    }

    /**
     * 获取配置文件名称
     *
     * @param tableInfo 表信息对象
     * @return 对应的配置文件名称
     */
    private String getConfigFileName(TableInfo tableInfo) {
        return CacheDataUtils.getInstance().getCreateMode().getConfigFileName(tableInfo);
    }


    /**
     * 对象还原
     *
     * @param str 原始JSON字符串
     * @return 解析结果
     */
    private TableInfo parser(String str) {
        try {
            return objectMapper.readValue(str, TableInfo.class);
        } catch (IOException e) {
            Messages.showWarningDialog("读取配置失败，JSON反序列化异常。", MsgValue.TITLE_INFO);
            ExceptionUtil.rethrow(e);
        }
        return null;
    }

}