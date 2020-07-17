package com.github.gingjing.plugin.generator.code.config;

import com.github.gingjing.plugin.common.constants.PluginFileConstants;
import com.github.gingjing.plugin.generator.code.entity.*;
import com.intellij.ide.fileTemplates.impl.UrlUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

/**
 * 全局配置信息
 *
 * @author makejava
 * @modify gingjingdm
 * @version 1.0.0
 * @since 2018/07/18 09:33
 */
@State(name = "CodeFlutterGenCodeSetting", storages = @Storage("code-flutter-gen-code.xml"))
public class CodeFlutterGenCodeConfigComponent implements PersistentStateComponent<CodeFlutterGenCodeConfigComponent> {
    /**
     * 默认名称
     */
    @Transient
    public static final String DEFAULT_NAME = "Default";

    /**
     * 版本号
     */
    private String version;
    /**
     * 当前类型映射组名
     */
    private String currTypeMapperGroupName;
    /**
     * 类型映射组
     */
    private Map<String, TypeMapperGroup> typeMapperGroupMap;
    /**
     * 当前模板组名
     */
    private String currTemplateGroupName;
    /**
     * 模板组
     */
    private Map<String, TemplateGroup> templateGroupMap;
    /**
     * 当前配置表组名
     */
    private String currColumnConfigGroupName;
    /**
     * 配置表组
     */
    private Map<String, ColumnConfigGroup> columnConfigGroupMap;
    /**
     * 当前全局配置组名
     */
    private String currGlobalConfigGroupName;
    /**
     * 全局配置组
     */
    private Map<String, GlobalConfigGroup> globalConfigGroupMap;
    /**
     * 作者
     */
    private String author;

    /**
     * 获取单例实例对象
     *
     * @return 实例对象
     */
    public static CodeFlutterGenCodeConfigComponent getInstance() {
        return ServiceManager.getService(CodeFlutterGenCodeConfigComponent.class);
    }

    /**
     * 默认构造方法
     */
    @SuppressWarnings("unused")
    public CodeFlutterGenCodeConfigComponent() {
        initDefault();
    }

    /**
     * 初始化默认设置
     */
    public void initDefault() {
        // 版本号
        this.version = "1.2.4";
        // 作者名称
        this.author = "user";
        // 当前各项分组名称
        this.currTemplateGroupName = DEFAULT_NAME;
        this.currTypeMapperGroupName = DEFAULT_NAME;
        this.currColumnConfigGroupName = DEFAULT_NAME;
        this.currGlobalConfigGroupName = DEFAULT_NAME;
        //配置默认模板
        if (this.templateGroupMap == null) {
            this.templateGroupMap = new LinkedHashMap<>();
        }
        this.templateGroupMap.put(DEFAULT_NAME, loadTemplateGroup(DEFAULT_NAME, "entity.java", "dao.java", "service.java", "serviceImpl.java", "controller.java", "mapper.xml", "debug.json"));
        this.templateGroupMap.put("MybatisPlus", loadTemplateGroup("MybatisPlus", "entity", "dao", "service", "serviceImpl", "controller"));

        //配置默认类型映射
        if (this.typeMapperGroupMap == null) {
            this.typeMapperGroupMap = new LinkedHashMap<>();
        }
        TypeMapperGroup typeMapperGroup = new TypeMapperGroup();
        List<TypeMapper> typeMapperList = new ArrayList<>();
        typeMapperList.add(new TypeMapper("varchar(\\(\\d+\\))?", "java.lang.String"));
        typeMapperList.add(new TypeMapper("char(\\(\\d+\\))?", "java.lang.String"));
        typeMapperList.add(new TypeMapper("text", "java.lang.String"));
        typeMapperList.add(new TypeMapper("decimal(\\(\\d+\\))?", "java.lang.Double"));
        typeMapperList.add(new TypeMapper("decimal(\\(\\d+,\\d+\\))?", "java.lang.Double"));
        typeMapperList.add(new TypeMapper("integer", "java.lang.Integer"));
        typeMapperList.add(new TypeMapper("int(\\(\\d+\\))?", "java.lang.Integer"));
        typeMapperList.add(new TypeMapper("int4", "java.lang.Integer"));
        typeMapperList.add(new TypeMapper("int8", "java.lang.Long"));
        typeMapperList.add(new TypeMapper("bigint(\\(\\d+\\))?", "java.lang.Long"));
        typeMapperList.add(new TypeMapper("datetime", "java.util.Date"));
        typeMapperList.add(new TypeMapper("timestamp", "java.util.Date"));
        typeMapperList.add(new TypeMapper("boolean", "java.lang.Boolean"));
        typeMapperGroup.setName(DEFAULT_NAME);
        typeMapperGroup.setElementList(typeMapperList);
        typeMapperGroupMap.put(DEFAULT_NAME, typeMapperGroup);

        //初始化表配置
        if (this.columnConfigGroupMap == null) {
            this.columnConfigGroupMap = new LinkedHashMap<>();
        }
        ColumnConfigGroup columnConfigGroup = new ColumnConfigGroup();
        List<ColumnConfig> columnConfigList = new ArrayList<>();
        columnConfigList.add(new ColumnConfig("disable", ColumnConfigType.BOOLEAN));
        columnConfigGroup.setName(DEFAULT_NAME);
        columnConfigGroup.setElementList(columnConfigList);
        columnConfigGroupMap.put(DEFAULT_NAME, columnConfigGroup);

        //初始化全局配置
        if (this.globalConfigGroupMap == null) {
            this.globalConfigGroupMap = new LinkedHashMap<>();
        }
        this.globalConfigGroupMap.put(DEFAULT_NAME, loadGlobalConfigGroup(DEFAULT_NAME, "init", "define", "autoImport", "mybatisSupport"));
    }

    /**
     * 加载模板文件
     *
     * @param filePath 模板路径
     * @return 模板文件内容
     */
    private static String loadTemplate(String filePath) {
        try {
            return UrlUtil.loadText(CodeFlutterGenCodeConfigComponent.class.getResource(filePath)).replace("\r", "");
        } catch (IOException e) {
            ExceptionUtil.rethrow(e);
        }
        return "";
    }

    /**
     * 加载模板组
     *
     * @param groupName     组名
     * @param templateNames 模板名称
     * @return 模板组
     */
    private static TemplateGroup loadTemplateGroup(String groupName, String... templateNames) {
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setName(groupName);
        templateGroup.setElementList(new ArrayList<>());
        for (String templateName : templateNames) {
            String path = "/template/" + groupName + "/" + templateName + PluginFileConstants.VELOCITY_FILE;
            templateGroup.getElementList().add(new Template(templateName, loadTemplate(path)));
        }
        return templateGroup;
    }

    /**
     * 加载全局配置组
     *
     * @param groupName     组名
     * @param templateNames 模板名称
     * @return 模板组
     */
    private static GlobalConfigGroup loadGlobalConfigGroup(String groupName, String... templateNames) {
        GlobalConfigGroup globalConfigGroup = new GlobalConfigGroup();
        globalConfigGroup.setName(groupName);
        globalConfigGroup.setElementList(new ArrayList<>());
        for (String templateName : templateNames) {
            String path = "/globalConfig/" + groupName + "/" + templateName + PluginFileConstants.VELOCITY_FILE;
            globalConfigGroup.getElementList().add(new GlobalConfig(templateName, loadTemplate(path)));
        }
        return globalConfigGroup;
    }

    @Nullable
    @Override
    public CodeFlutterGenCodeConfigComponent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CodeFlutterGenCodeConfigComponent settings) {
        // 备份初始配置
        Map<String, TemplateGroup> templateGroupMap = this.getTemplateGroupMap();
        Map<String, GlobalConfigGroup> globalConfigGroupMap = this.getGlobalConfigGroupMap();
        String version = this.getVersion();
        // 覆盖初始配置
        XmlSerializerUtil.copyBean(settings, this);

        // 已经合并不再重复合并
        if (Objects.equals(settings.getVersion(), version)) {
            return;
        }

        // 模板备份
        TemplateGroup oldTemplateGroup = settings.getTemplateGroupMap().get(DEFAULT_NAME);
        String newName = oldTemplateGroup.getName() + "Bak";
        int i = 0;
        while (settings.getTemplateGroupMap().containsKey(newName)) {
            newName = newName + i++;
        }
        oldTemplateGroup.setName(newName);
        // 保存
        settings.getTemplateGroupMap().put(newName, oldTemplateGroup);
        // 覆盖
        settings.getTemplateGroupMap().replace(DEFAULT_NAME, templateGroupMap.get(DEFAULT_NAME));



        // 全局配置备份
        GlobalConfigGroup oldGlobalConfigGroup = settings.getGlobalConfigGroupMap().get(DEFAULT_NAME);
        newName = oldGlobalConfigGroup.getName() + "Bak";
        i = 0;
        while (settings.getGlobalConfigGroupMap().containsKey(newName)) {
            newName = newName + i++;
        }
        oldGlobalConfigGroup.setName(newName);
        // 保存
        settings.getGlobalConfigGroupMap().put(newName, oldGlobalConfigGroup);
        // 覆盖
        settings.getGlobalConfigGroupMap().replace(DEFAULT_NAME, globalConfigGroupMap.get(DEFAULT_NAME));

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCurrTypeMapperGroupName() {
        return currTypeMapperGroupName;
    }

    public void setCurrTypeMapperGroupName(String currTypeMapperGroupName) {
        this.currTypeMapperGroupName = currTypeMapperGroupName;
    }

    public Map<String, TypeMapperGroup> getTypeMapperGroupMap() {
        return typeMapperGroupMap;
    }

    public void setTypeMapperGroupMap(Map<String, TypeMapperGroup> typeMapperGroupMap) {
        this.typeMapperGroupMap = typeMapperGroupMap;
    }

    public String getCurrTemplateGroupName() {
        return currTemplateGroupName;
    }

    public void setCurrTemplateGroupName(String currTemplateGroupName) {
        this.currTemplateGroupName = currTemplateGroupName;
    }

    public Map<String, TemplateGroup> getTemplateGroupMap() {
        return templateGroupMap;
    }

    public void setTemplateGroupMap(Map<String, TemplateGroup> templateGroupMap) {
        this.templateGroupMap = templateGroupMap;
    }

    public String getCurrColumnConfigGroupName() {
        return currColumnConfigGroupName;
    }

    public void setCurrColumnConfigGroupName(String currColumnConfigGroupName) {
        this.currColumnConfigGroupName = currColumnConfigGroupName;
    }

    public Map<String, ColumnConfigGroup> getColumnConfigGroupMap() {
        return columnConfigGroupMap;
    }

    public void setColumnConfigGroupMap(Map<String, ColumnConfigGroup> columnConfigGroupMap) {
        this.columnConfigGroupMap = columnConfigGroupMap;
    }

    public String getCurrGlobalConfigGroupName() {
        return currGlobalConfigGroupName;
    }

    public void setCurrGlobalConfigGroupName(String currGlobalConfigGroupName) {
        this.currGlobalConfigGroupName = currGlobalConfigGroupName;
    }

    public Map<String, GlobalConfigGroup> getGlobalConfigGroupMap() {
        return globalConfigGroupMap;
    }

    public void setGlobalConfigGroupMap(Map<String, GlobalConfigGroup> globalConfigGroupMap) {
        this.globalConfigGroupMap = globalConfigGroupMap;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodeFlutterGenCodeConfigComponent settings = (CodeFlutterGenCodeConfigComponent) o;

        if (!Objects.equals(version, settings.version)) {
            return false;
        }
        if (!Objects.equals(currTypeMapperGroupName, settings.currTypeMapperGroupName)) {
            return false;
        }
        if (!Objects.equals(typeMapperGroupMap, settings.typeMapperGroupMap)) {
            return false;
        }
        if (!Objects.equals(currTemplateGroupName, settings.currTemplateGroupName)) {
            return false;
        }
        if (!Objects.equals(templateGroupMap, settings.templateGroupMap)) {
            return false;
        }
        if (!Objects.equals(currColumnConfigGroupName, settings.currColumnConfigGroupName)) {
            return false;
        }
        if (!Objects.equals(columnConfigGroupMap, settings.columnConfigGroupMap)) {
            return false;
        }
        if (!Objects.equals(currGlobalConfigGroupName, settings.currGlobalConfigGroupName)) {
            return false;
        }
        if (!Objects.equals(globalConfigGroupMap, settings.globalConfigGroupMap)) {
            return false;
        }
        return Objects.equals(author, settings.author);
    }

    @Override
    public int hashCode() {
        int result = version != null ? version.hashCode() : 0;
        result = 31 * result + (currTypeMapperGroupName != null ? currTypeMapperGroupName.hashCode() : 0);
        result = 31 * result + (typeMapperGroupMap != null ? typeMapperGroupMap.hashCode() : 0);
        result = 31 * result + (currTemplateGroupName != null ? currTemplateGroupName.hashCode() : 0);
        result = 31 * result + (templateGroupMap != null ? templateGroupMap.hashCode() : 0);
        result = 31 * result + (currColumnConfigGroupName != null ? currColumnConfigGroupName.hashCode() : 0);
        result = 31 * result + (columnConfigGroupMap != null ? columnConfigGroupMap.hashCode() : 0);
        result = 31 * result + (currGlobalConfigGroupName != null ? currGlobalConfigGroupName.hashCode() : 0);
        result = 31 * result + (globalConfigGroupMap != null ? globalConfigGroupMap.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CodeFlutterGenCodeConfigComponent.class.getSimpleName() + "[", "]")
                .add("version='" + version + "'")
                .add("currTypeMapperGroupName='" + currTypeMapperGroupName + "'")
                .add("typeMapperGroupMap=" + typeMapperGroupMap)
                .add("currTemplateGroupName='" + currTemplateGroupName + "'")
                .add("templateGroupMap=" + templateGroupMap)
                .add("currColumnConfigGroupName='" + currColumnConfigGroupName + "'")
                .add("columnConfigGroupMap=" + columnConfigGroupMap)
                .add("currGlobalConfigGroupName='" + currGlobalConfigGroupName + "'")
                .add("globalConfigGroupMap=" + globalConfigGroupMap)
                .add("author='" + author + "'")
                .toString();
    }
}
