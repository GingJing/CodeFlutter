package com.github.gingjing.plugin.generator.code.ui;

import b.j.D;
import com.github.gingjing.plugin.common.utils.PluginSqlUtil;
import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.entity.*;
import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.CurrGroupUtils;
import com.github.gingjing.plugin.generator.code.tool.DialogUtils;
import com.github.gingjing.plugin.generator.code.tool.StringUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.gingjing.plugin.common.utils.PluginSqlUtil.TABLE_NAME;

/**
 * 表信息配置对话框，通过选取java实体或者建表语句获取表信息，并通过对话框进一步设置
 *
 * @author: gingjingdm
 * @date: 2020年 06月27日 19时59分
 * @version: 1.0
 */
public class SetTableInfoByModelOrSqlDialog extends JDialog {

    /** 注释填写说明 */
    public static final String COMMENT_NOTE = "注释填写：\n\t此处的填写请保持原样格式，请不要额外添加/*或*/";

    /** 主键列说明 */
    public static final String PK_NOTE = "主键列填写：\n\t'Y'表示是主键，'N'表示非主键，默认为'N'";

    /** 类型列说明 */
    public static final String TYPE_NOTE = "类型列填写：\n\tjava权限定名，如：java.lang.String";

    /** disable列说明 */
    public static final String DISABLE_NOTE = "disable列表填写：\n\ttrue或者false，鼠标单击即可改变值，默认为false";


    /** 是主键 */
    public static final String IS_PK = "Y";

    /** 不是主键 */
    public static final String NOT_PK = "N";

    /** 主面板 */
    private JPanel contentPane;

    /** OK */
    private JButton buttonOK;

    /** 取消 */
    private JButton buttonCancel;

    /** 表注释文本 */
    private JTextArea tabCmtTf;

    /** 说明文本 */
    private JTextArea noteTextArea;

    /** 列信息表 */
    private JTable colInfoTab;

    /** 添加列事件 */
    private JButton removeButton;

    /** 减少列事件 */
    private JButton addButton;

    /** schema名称文本域 */
    private JTextField schemaTextField;

    /** 表信息 */
    private TableInfo tableInfo;

    /** 默认的表模型 */
    private DefaultTableModel tableModel;

    /** 列配置 */
    private List<ColumnConfig> columnConfigList;

    /** 缓存数据工具类 */
    private CacheDataUtils cacheDataUtils;

    /** 表信息服务 */
    private TableInfoService tableInfoService;

    /** 初始化标记 */
    private boolean initFlag;

    /** 项目 */
    private Project project;

    public SetTableInfoByModelOrSqlDialog(Project project) {
        this.project = project;
        this.cacheDataUtils = CacheDataUtils.getInstance();
        this.tableInfoService = TableInfoService.getInstance(project);
        this.tabCmtTf.setAutoscrolls(true);
        this.noteTextArea.setEnabled(false);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.setText(cacheDataUtils.isDebugging() ? "确定" : "选择路径");

        buttonOK.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getButton());
                if (e.getButton() == MouseEvent.BUTTON1) {
                    onOK();
                }
            }
        });


        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        init();
        initEvent();
    }

    private void onOK() {
        if (StringUtils.isEmpty(schemaTextField.getText())) {
            Messages.showErrorDialog("请填写schema名称", MsgValue.TITLE_INFO);
        }
        // add your code here
        this.tableInfoService.save(tableInfo);
        if (!cacheDataUtils.isDebugging()) {
            new SelectSavePathDialog(project).open();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * 初始化方法
     */
    private void init() {
        this.initFlag = false;
        ColumnConfigGroup columnConfigGroup = CurrGroupUtils.getCurrColumnConfigGroup();
        // 拿到列配置信息
        this.columnConfigList = getInitColumn(columnConfigGroup.getElementList());

        this.tableInfo = cacheDataUtils.getCreateMode().getTableInfoAndConfig(tableInfoService, cacheDataUtils);

        this.schemaTextField.setText(cacheDataUtils.getSchema());

        this.noteTextArea.setText(initNote());

        this.tabCmtTf.setText(tableInfo.getComment());
        this.tabCmtTf.setAutoscrolls(true);

        refresh();

        this.initFlag = true;
    }

    private String initNote() {
        return new StringJoiner(System.lineSeparator())
                .add(COMMENT_NOTE)
                .add(PK_NOTE)
                .add(TYPE_NOTE)
                .add(DISABLE_NOTE)
                .toString();
    }

    /**
     * 获取初始列
     *
     * @param columnConfigList 列配置集合
     * @return 初始列信息
     */
    private List<ColumnConfig> getInitColumn(List<ColumnConfig> columnConfigList) {
        List<ColumnConfig> result = new ArrayList<>();
        result.add(new ColumnConfig("name", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("pk", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("type", ColumnConfigType.TEXT));
        result.add(new ColumnConfig("comment", ColumnConfigType.TEXT));
        result.addAll(columnConfigList);
        return result;
    }

    /**
     * 刷新界面
     */
    private void refresh() {
        this.tableInfo.setComment(StringUtils.isEmpty(tabCmtTf.getText()) ? tableInfo.getComment() : tabCmtTf.getText());
        this.tableModel = new DefaultTableModel();
        this.columnConfigList.forEach(columnConfig -> tableModel.addColumn(columnConfig.getTitle()));
        Set<String> pkColumns =
                Optional.ofNullable(tableInfo.getPkColumn())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(ColumnInfo::getName)
                        .collect(Collectors.toSet());
        //追加数据
        this.tableInfo.getFullColumn().forEach(columnInfo -> {
            List<Object> dataList = new ArrayList<>();
            dataList.add(columnInfo.getName());
            dataList.add(pkColumns.contains(columnInfo.getName()) ? IS_PK : NOT_PK);
            dataList.add(columnInfo.getType());
            dataList.add(columnInfo.getComment());
            //渲染附加数据
            if (columnInfo.getExt() != null && !columnInfo.getExt().isEmpty()) {
                // 跳过默认的4条数据
                for (int i = 4; i < tableModel.getColumnCount(); i++) {
                    dataList.add(columnInfo.getExt().get(tableModel.getColumnName(i)));
                }
            }
            tableModel.addRow(dataList.toArray());
        });
        this.colInfoTab.setModel(tableModel);
        //刷新列编辑器
        refreshColumnEditor(columnConfigList);

        //添加数据修改事件
        this.tableModel.addTableModelListener(e -> {
            // 一键编辑多行时不处理。
            if (e.getFirstRow() != e.getLastRow()) {
                return;
            }
            int row = e.getFirstRow();
            int column = e.getColumn();
            Object val = tableModel.getValueAt(row, column);
            ColumnInfo columnInfo = tableInfo.getFullColumn().get(row);
            if (column == 0) {
                for (ColumnInfo info : tableInfo.getFullColumn()) {
                    if (info.getName().equals(val) && !info.getName().equals(columnInfo.getName())) {
                        Messages.showWarningDialog("Column Name Already exist!", MsgValue.TITLE_INFO);
                        // 输入的名称已经存在时，直接还原
                        tableModel.setValueAt(columnInfo.getName(), row, column);
                        return;
                    }
                }
            }
            switch (column) {
                case 0:
                    columnInfo.setName((String) val);
                    break;
                case 1:
                    Set<String> pks = tableInfo.getPkColumn().stream().map(ColumnInfo::getName).collect(Collectors.toSet());
                    if (IS_PK.equals(String.valueOf(val))) {
                        if (!pks.contains(columnInfo.getName())) {
                            tableInfo.getPkColumn().add(columnInfo);
                        }
                    } else {
                        if (pks.contains(columnInfo.getName())) {
                            tableInfo.getPkColumn().removeIf(c -> c.getName().equals(columnInfo.getName()));
                            tableInfo.getOtherColumn().add(columnInfo);
                        }
                    }
                    break;
                case 2:
                    columnInfo.setType((String) val);
                    break;
                case 3:
                    columnInfo.setComment((String) val);
                    break;
                default:
                    if (columnInfo.getExt() == null) {
                        columnInfo.setExt(new HashMap<>(10));
                    }
                    String title = tableModel.getColumnName(column);
                    columnInfo.getExt().put(title, val);
                    break;
            }
        });
    }

    /**
     * 刷新列编辑器
     *
     * @param columnConfigList 列配置集合
     */
    private void refreshColumnEditor(List<ColumnConfig> columnConfigList) {
        DialogUtils.refreshColumnEditor(columnConfigList, colInfoTab, tableModel);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //添加元素事件
        addButton.addActionListener(e -> {
            if (!initFlag) {
                return;
            }
            String value = DialogUtils.addInput4ColName(tableInfo);
            //取消输入
            if (value == null) {
                return;
            }
            String yes = "Y";
            String no = "N";
            String isPk = Messages.showInputDialog("Input Y/N. Y means primary key. Default return N", "Is Primary Key?", Messages.getQuestionIcon(), "Demo", new InputValidator() {
                @Override
                public boolean checkInput(String inputString) {
                    if (StringUtils.isEmpty(inputString)) {
                        return true;
                    }
                    return yes.equalsIgnoreCase(inputString) || no.equalsIgnoreCase(inputString);
                }

                @Override
                public boolean canClose(String inputString) {
                    return this.checkInput(inputString);
                }
            });

            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(value);
            columnInfo.setType("java.lang.String");
            // 标记为自定义列
            columnInfo.setCustom(true);
            tableInfo.getFullColumn().add(columnInfo);
            if (yes.equalsIgnoreCase(isPk)) {
                columnInfo.setPk(true);
                tableInfo.getPkColumn().add(columnInfo);
            } else {
                columnInfo.setPk(false);
                tableInfo.getOtherColumn().add(columnInfo);
            }
            refresh();
        });

        removeButton.addActionListener(e -> {
            if (!initFlag) {
                return;
            }
            String value = DialogUtils.removeInput4ColName(tableInfo);
            // 无需要删除的列名
            if (value == null) {
                return;
            }
            boolean isPk = tableInfo.getPkColumn().removeIf(columnInfo -> columnInfo.getName().equals(value));
            tableInfo.getFullColumn().removeIf(columnInfo -> columnInfo.getName().equals(value));
            tableInfo.getOtherColumn().removeIf(columnInfo -> !isPk && columnInfo.getName().equals(value));
            refresh();
        });
    }

    private String name() {
        switch (cacheDataUtils.getCreateMode()) {
            case SELECT_MODEL:
                return cacheDataUtils.getSelectClass().getName();
            case CREATE_SQL:
                return PluginSqlUtil.parseAndGetVisitor(cacheDataUtils.getCurrCreateSql(), cacheDataUtils.getCurrDbType()).map.get(TABLE_NAME);
            default:
                return cacheDataUtils.getSelectDbTable().getName();
        }
    }

    public void open() {
        setTitle("Generate Config For " + name());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean isInitFlag() {
        return initFlag;
    }

    public static void main(String[] args) {
        /*SetInfoByModelDialog dialog = new SetInfoByModelDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);*/
    }

}
