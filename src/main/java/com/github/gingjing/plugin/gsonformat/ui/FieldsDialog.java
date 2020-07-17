package com.github.gingjing.plugin.gsonformat.ui;


import com.github.gingjing.plugin.gsonformat.ConvertBridge;
import com.github.gingjing.plugin.gsonformat.action.DataWriter;
import com.github.gingjing.plugin.gsonformat.common.PsiClassUtil;
import com.github.gingjing.plugin.gsonformat.common.StringUtils;
import com.github.gingjing.plugin.gsonformat.config.Config;
import com.github.gingjing.plugin.gsonformat.entity.ClassEntity;
import com.github.gingjing.plugin.gsonformat.entity.FieldEntity;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import cn.vearn.checktreetable.*;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.ux.CheckTreeTableManager;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class FieldsDialog extends JFrame {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel filedPanel;
    private JScrollPane sp;
    private PsiClass psiClass;
    private ClassEntity classEntity;
    private ConvertBridge.Operator operator;
    private PsiElementFactory factory;
    private PsiClass aClass;
    private PsiFile file;
    private Project project;
    private JLabel generateClass;
    private String generateClassStr;
    private ArrayList<DefaultMutableTreeTableNode> defaultMutableTreeTableNodeList;


    public FieldsDialog(ConvertBridge.Operator operator, ClassEntity classEntity,
                        PsiElementFactory factory, PsiClass psiClass, PsiClass aClass, PsiFile file, Project project
            , String generateClassStr) {
        this.operator = operator;
        this.factory = factory;
        this.aClass = aClass;
        this.file = file;
        this.project = project;
        this.psiClass = psiClass;
        this.generateClassStr = generateClassStr;
        setContentPane(contentPane);
        setTitle("Virgo Model");
        getRootPane().setDefaultButton(buttonOK);
        this.setAlwaysOnTop(true);
        initListener(classEntity, generateClassStr);
    }

    private void initListener(ClassEntity classEntity, String generateClassStr) {
        this.classEntity = classEntity;
        defaultMutableTreeTableNodeList = new ArrayList<>();
        JXTreeTable treetable = new JXTreeTable(new FiledTreeTableModel(createData(classEntity)));
        CheckTreeTableManager manager = new CheckTreeTableManager(treetable);
        manager.getSelectionModel().addPathsByNodes(defaultMutableTreeTableNodeList);
        treetable.getColumnModel().getColumn(0).setPreferredWidth(150);
//        treetable.setSelectionBackground(treetable.getBackground());
        treetable.expandAll();
        treetable.setCellSelectionEnabled(false);
        final DefaultListSelectionModel defaultListSelectionModel = new DefaultListSelectionModel();
        treetable.setSelectionModel(defaultListSelectionModel);

        defaultListSelectionModel.setSelectionMode(SINGLE_SELECTION);
        defaultListSelectionModel.addListSelectionListener((e) -> defaultListSelectionModel.clearSelection());
        defaultMutableTreeTableNodeList = null;
        treetable.setRowHeight(30);
        sp.setViewportView(treetable);
        generateClass.setText(generateClassStr);
        buttonOK.addActionListener((e) -> onOK());
        buttonCancel.addActionListener((e) -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction((e) -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        contentPane.registerKeyboardAction((e) -> onOK(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        this.setAlwaysOnTop(false);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            if (psiClass == null) {
                try {
                    psiClass = PsiClassUtil.getPsiClass(file, project, generateClassStr);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    operator.showError(ConvertBridge.Error.DATA_ERROR);
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    throwable.printStackTrace(printWriter);
                    printWriter.close();
                    operator.setErrorInfo(writer.toString());
                    operator.setVisible(true);
                    operator.showError(ConvertBridge.Error.PATH_ERROR);
                }
            }

            if (psiClass != null) {
                String[] arg = generateClassStr.split("\\.");
                if (arg.length > 1) {
                    Config.getInstant().setEntityPackName(generateClassStr.substring(0, generateClassStr.length() - arg[arg.length - 1].length()));
                    Config.getInstant().save();
                }
                try {
                    setVisible(false);
                    DataWriter dataWriter = new DataWriter(file, project, psiClass);
                    dataWriter.execute(classEntity);
                    Config.getInstant().saveCurrentPackPath(StringUtils.getPackage(generateClassStr));
                    operator.dispose();
                    dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                    operator.showError(ConvertBridge.Error.PARSE_ERROR);
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    printWriter.close();
                    operator.setErrorInfo(writer.toString());
                    operator.setVisible(true);
                    dispose();
                }
            }
        });
    }

    private void onCancel() {
        operator.setVisible(true);
        dispose();
    }

    private DefaultMutableTreeTableNode createData(ClassEntity classEntity) {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(classEntity);
        createDataNode(root, classEntity);
        return root;
    }

    private void createDataNode(DefaultMutableTreeTableNode root, ClassEntity innerClassEntity) {
        for (FieldEntity field : innerClassEntity.getFields()) {
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(field);
            root.add(node);
            defaultMutableTreeTableNodeList.add(node);
        }
        for (ClassEntity classEntity : innerClassEntity.getInnerClasss()) {
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(classEntity);
            root.add(node);
            createDataNode(node, classEntity);
        }

    }

}
