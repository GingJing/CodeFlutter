package com.github.gingjing.plugin.generator.code.ui;

import com.alibaba.druid.util.JdbcConstants;
import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionComboBoxModel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * 建表sql输入对话框
 *
 * @author: GingJingDM
 * @date: 2020年 07月06日 21时32分
 * @version: 1.0
 */
public class CreateSqlDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JComboBox<String> dbComboBox;

    private String dbType;

    private JTextArea sqlTextArea;

    private CacheDataUtils cacheDataUtils;


    public CreateSqlDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.cacheDataUtils = CacheDataUtils.getInstance();
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void init() {
        ComboBoxModel<String> dbTypeCbxModel = new CollectionComboBoxModel<>(
                new ArrayList<>(ImmutableList.<String>builder()
                        .add(JdbcConstants.MYSQL)
                        .add(JdbcConstants.HIVE)
                        .add(JdbcConstants.ORACLE)
                        .add(JdbcConstants.HBASE)
                        .add(JdbcConstants.SQL_SERVER)
                        .add(JdbcConstants.CLICKHOUSE)
                        .add(JdbcConstants.H2)
                        .add(JdbcConstants.HSQL)
                        .add(JdbcConstants.SQLITE)
                        .add(JdbcConstants.GBASE)
                        .add(JdbcConstants.DB2)
                        .add(JdbcConstants.ALI_ORACLE)
                        .add(JdbcConstants.OCEANBASE)
                        .add(JdbcConstants.KINGBASE)
                        .add(JdbcConstants.SYBASE)
                        .add(JdbcConstants.DERBY)
                        .add(JdbcConstants.DM)
                        .add(JdbcConstants.JTDS)
                        .add(JdbcConstants.TERADATA)
                        .add(JdbcConstants.XUGU)
                        .add(JdbcConstants.MARIADB)
                        .add(JdbcConstants.MOCK)
                        .build().asList())
        );
        ComboBox<String> cbx = new ComboBox<>(dbTypeCbxModel);
        cbx.setSelectedItem(JdbcConstants.MYSQL);
        cbx.addActionListener(e -> dbType = (String) cbx.getSelectedItem());
    }

    private void onOK() {
        // add your code here
        this.cacheDataUtils.setCurrDbType(PluginStringUtil.isBlank(dbType) ? JdbcConstants.MYSQL : dbType);
        this.cacheDataUtils.setCurrCreateSql(this.sqlTextArea.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void open() {
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        CreateSqlDialog dialog = new CreateSqlDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
