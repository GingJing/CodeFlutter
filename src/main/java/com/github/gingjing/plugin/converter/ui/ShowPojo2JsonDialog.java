package com.github.gingjing.plugin.converter.ui;

import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.generator.code.tool.CollectionUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.gingjing.plugin.converter.actions.TransformJavaToJsonAction.*;

/**
 * java类转json展示对话框
 *
 * @author: GingJingDM
 * @date: 2020年 07月09日 22时56分
 * @version: 1.0
 */
public class ShowPojo2JsonDialog extends JDialog {

    /** 主面板 */
    private JPanel contentPane;

    /** 确认按钮 */
    private JButton buttonOK;

    /** 取消按钮 */
    private JButton buttonCancel;

    /** 格式化按钮 */
    private JButton formatButton;

    /** 源java类名 */
    private JTextField oriClzTextField;

    /** java字段选择按钮 */
    private JRadioButton fieldRadioButton;

    /** java内部类选择按钮 */
    private JRadioButton innerClassRadioButton;

    /** javadoc注释选择按钮 */
    private JRadioButton commentRadioButton;

    /** 所有选择按钮 */
    private JRadioButton allRadioButton;

    /** 内容展示域 */
    private JTextArea showTextArea;

    /** 内容展示滚动面板 */
    private JScrollPane showScrollPane;

    /** json字符串map */
    private Map<String, Object> allMap;

    public ShowPojo2JsonDialog() {
        this(null, null);
    }

    public ShowPojo2JsonDialog(@Nullable String className, @Nullable Map<String, Object> allMap) {
        this.allMap = CollectionUtil.isEmpty(allMap) ? new LinkedHashMap<>() : allMap;
        this.oriClzTextField.setText(className);
        initRadioButton();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        formatButton.addActionListener(e -> onFormat());

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

    private void initRadioButton() {
        this.allRadioButton.setSelected(true);
        this.showTextArea.setText(PluginJsonUtil.formatByGson(PluginJsonUtil.gson.toJson(allMap)));
        ButtonGroup group = new ButtonGroup();
        group.add(allRadioButton);
        group.add(fieldRadioButton);
        group.add(innerClassRadioButton);
        group.add(commentRadioButton);
        group.setSelected(allRadioButton.getModel(), true);
        class SwitchListener implements ItemListener {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (group.isSelected(allRadioButton.getModel())) {
                    showTextArea.setText(PluginJsonUtil.formatByGson(PluginJsonUtil.gson.toJson(allMap)));
                } else if (group.isSelected(fieldRadioButton.getModel())) {
                    showTextArea.setText(PluginJsonUtil.formatByGson(PluginJsonUtil.gson.toJson(allMap.get(FIELD))));
                } else if (group.isSelected(innerClassRadioButton.getModel())) {
                    showTextArea.setText(PluginJsonUtil.formatByGson(PluginJsonUtil.gson.toJson(allMap.get(INNERCLASS))));
                } else {
                    showTextArea.setText(PluginJsonUtil.formatByGson(PluginJsonUtil.gson.toJson(allMap.get(COMMENT))));
                }
            }
        }
        Enumeration<AbstractButton> buttonEnumeration = group.getElements();
        while (buttonEnumeration.hasMoreElements()) {
            buttonEnumeration.nextElement().addItemListener(new SwitchListener());
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onFormat() {
        showTextArea.setText(PluginJsonUtil.formatByGson(showTextArea.getText()));
    }

    public static void main(String[] args) {
        ShowPojo2JsonDialog dialog = new ShowPojo2JsonDialog();
        dialog.setSize(600, 500);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        System.exit(0);
    }

    public void open() {
        this.setSize(600, 500);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
