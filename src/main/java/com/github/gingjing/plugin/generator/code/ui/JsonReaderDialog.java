package com.github.gingjing.plugin.generator.code.ui;

import com.github.gingjing.plugin.generator.code.service.impl.JsonToPojoMakeServiceImpl;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class JsonReaderDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea inputField;
    private JRadioButton gsonRadioButton;
    private JRadioButton noneRadioButton;
    private JCheckBox generateGettersAndSettersCheckBox;
    private JCheckBox includeConstructorCheckBox;
    private JTextField className;
    private JButton okButton;
    private JButton cancelButton;
    private AnActionEvent anActionEvent;
    private VirtualFile dir;

    public JsonReaderDialog() {
    }

    public JsonReaderDialog(VirtualFile dir) {
        this(null, dir);
    }

    public JsonReaderDialog(AnActionEvent anActionEvent) {
        this(anActionEvent, null);
    }

    public JsonReaderDialog(AnActionEvent actionEvent, VirtualFile dir) {
        anActionEvent = actionEvent;
        this.dir = dir;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(okButton);
        int width = 500, height = 500;
        if (actionEvent != null) {
            Editor editor = actionEvent.getDataContext().getData(CommonDataKeys.EDITOR);
            if (editor != null) {
                width = editor.getComponent().getWidth();
                height = editor.getComponent().getHeight();
            }
        }
        setBounds(width / 2, height / 2, (width / 2), (height / 2));
        inputField.setVisible(true);
        inputField.setBounds(0, 0, 200, 200);
        inputField.setAutoscrolls(true);
        inputField.setMaximumSize(new Dimension(200, 200));
        okButton.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

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
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        pack();
    }

    private void onOK() {
        // add your code here
        VirtualFile file = anActionEvent != null
                ? anActionEvent.getDataContext().getData(LangDataKeys.VIRTUAL_FILE)
                : dir;
        if (file == null) {
            file = ProjectUtils.getCurrProject().getProjectFile();
        }
        String data = "";
        try {
            data = inputField.getDocument().getText(0, inputField.getDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        if ("".equals(data.trim())) {
            JOptionPane.showMessageDialog(new JFrame(), "Empty Data");
        } else {
            String text = data.substring(0, 1);
            if ("{".equals(text)) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    toGenerateClass(file, data);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
                }
            } else {
                try {
                    JSONArray jsonObject = new JSONArray(data);
                    toGenerateClass(file, data);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
                }
            }
        }
        dispose();
    }

    /**
     * 生成POJO代码
     *
     * @param file 生成POJO的文件夹
     * @param data json字符串
     */
    private void toGenerateClass(VirtualFile file, String data) {
        String classNameText = className.getText();
        if ("".equals(classNameText.trim())) {
            classNameText = "Example";
        }

        boolean isAnnotationRequired = gsonRadioButton.isSelected();
        boolean isGetterSetterRequired = generateGettersAndSettersCheckBox.isSelected();
        boolean isNeedConstructor = includeConstructorCheckBox.isSelected();
        if (file != null) {
            JsonToPojoMakeServiceImpl.staticGenerate(
                    file.getCanonicalPath(),
                    data,
                    classNameText,
                    isAnnotationRequired,
                    isNeedConstructor,
                    isGetterSetterRequired
            );
        }
        if (file != null) {

            VirtualFile virtualFile = file.getParent();
            virtualFile.refresh(true, true);
        }
    }

    public JTextField getClassName() {
        return className;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        JsonReaderDialog dialog = new JsonReaderDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
