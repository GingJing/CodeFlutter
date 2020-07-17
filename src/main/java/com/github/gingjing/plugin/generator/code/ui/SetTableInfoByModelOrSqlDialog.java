package com.github.gingjing.plugin.generator.code.ui;

import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.entity.*;
import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.CurrGroupUtils;
import com.github.gingjing.plugin.generator.code.tool.DialogUtils;
import com.github.gingjing.plugin.generator.code.tool.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 表信息配置对话框，通过选取java实体或者建表语句获取表信息，并通过对话框进一步设置
 *
 * @author: gingjingdm
 * @date: 2020年 06月27日 19时59分
 * @version: 1.0
 */
public class SetTableInfoByModelOrSqlDialog extends JDialog {

    /** 主面板 */
    private JPanel contentPane;

    /** OK */
    private JButton buttonOK;

    /** 取消 */
    private JButton buttonCancel;

    /** 表注释文本 */
    private JTextField tabCmtTf;

    /** 说明文本 */
    private JTextArea noteTextArea;

    /** 列信息表 */
    private JTable colInfoTab;

    /** 添加列事件 */
    private JButton removeButton;

    /** 减少列事件 */
    private JButton addButton;

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

    private Project project;

    public SetTableInfoByModelOrSqlDialog(Project project) {
        this.project = project;
        this.cacheDataUtils = CacheDataUtils.getInstance();
        this.tableInfoService = TableInfoService.getInstance(project);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.setText(cacheDataUtils.isDebugging() ? "确定" : "选择路径");

        buttonOK.addActionListener(e -> onOK());

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

        this.tabCmtTf.setText(tableInfo.getComment());

        refresh();

        this.initFlag = true;
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
        Set<String> pkColumns = tableInfo.getPkColumn().stream().map(ColumnInfo::getName).collect(Collectors.toSet());
        //追加数据
        this.tableInfo.getFullColumn().forEach(columnInfo -> {
            List<Object> dataList = new ArrayList<>();
            dataList.add(columnInfo.getName());
            JCheckBox isPkCx = new JCheckBox();
            isPkCx.setVisible(true);
            isPkCx.setSelected(pkColumns.contains(columnInfo.getName()));
            dataList.add(isPkCx);
            dataList.add(columnInfo.getType());
            dataList.add(columnInfo.getComment());
            //渲染附加数据
            if (columnInfo.getExt() != null && !columnInfo.getExt().isEmpty()) {
                // 跳过默认的3条数据
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
                    if (((JCheckBox)val).isSelected()) {
                        Set<String> pks = tableInfo.getPkColumn().stream().map(ColumnInfo::getName).collect(Collectors.toSet());
                        if (!pks.contains(columnInfo.getName())) {
                            tableInfo.getPkColumn().add(columnInfo);
                        }
                    } else {
                        Set<String> pks = tableInfo.getPkColumn().stream().map(ColumnInfo::getName).collect(Collectors.toSet());
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

    public void open() {
        setTitle("Generate Config For " + cacheDataUtils.getSelectClass().getName());
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
