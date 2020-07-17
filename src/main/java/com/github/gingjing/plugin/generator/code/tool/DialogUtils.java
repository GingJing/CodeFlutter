package com.github.gingjing.plugin.generator.code.tool;

import com.github.gingjing.plugin.generator.code.entity.ColumnConfig;
import com.github.gingjing.plugin.generator.code.entity.ColumnInfo;
import com.github.gingjing.plugin.generator.code.entity.TableInfo;
import com.intellij.ide.util.TreeFileChooser;
import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiJavaFile;
import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.util.ui.ComboBoxCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 对话框工具类
 *
 * @author: Jmm
 * @date: 2020年07月01日12时02分
 * @version: 1.0
 */
public class DialogUtils {


    /**
     * 设置DialogBuilder的编辑器属性
     *
     * @param editor 编辑器
     */
    public static void setEditorSettings(Editor editor) {
        // 配置编辑框
        EditorSettings editorSettings = editor.getSettings();
        // 关闭虚拟空间
        editorSettings.setVirtualSpace(false);
        // 关闭标记位置（断点位置）
        editorSettings.setLineMarkerAreaShown(false);
        // 关闭缩减指南
        editorSettings.setIndentGuidesShown(false);
        // 显示行号
        editorSettings.setLineNumbersShown(true);
        // 支持代码折叠
        editorSettings.setFoldingOutlineShown(true);
        // 附加行，附加列（提高视野）
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        // 不显示换行符号
        editorSettings.setCaretRowShown(false);
    }


    /**
     * 刷新编辑器
     *
     * @param columnConfigList  列配置列表
     * @param colInfoTab        列信息表
     * @param tableModel        默认表对象
     */
    public static void refreshColumnEditor(List<ColumnConfig> columnConfigList, JTable colInfoTab, DefaultTableModel tableModel) {
        columnConfigList.forEach(columnConfig -> {
            TableColumn tableColumn = colInfoTab.getColumn(columnConfig.getTitle());
            int index = tableColumn.getModelIndex();
            switch (columnConfig.getType()) {
                case TEXT:
                    break;
                case SELECT:
                    tableColumn.setCellEditor(new ComboBoxCellEditor() {
                        @Override
                        protected List<String> getComboBoxItems() {
                            String selectValue = columnConfig.getSelectValue();
                            if (StringUtils.isEmpty(selectValue)) {
                                //noinspection unchecked
                                return Collections.EMPTY_LIST;
                            }
                            return Arrays.asList(columnConfig.getSelectValue().split(","));
                        }
                    });
                    break;
                case BOOLEAN:
                    //给空列赋初始值
                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        if (tableModel.getValueAt(row, index) == null) {
                            tableModel.setValueAt(false, row, index);
                        }
                    }
                    tableColumn.setCellEditor(new BooleanTableCellEditor());
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 获取输入的待添加的额外列的值，并判断是否与传入的表信息中列名重复
     *
     * @param tableInfo 表信息
     * @return 输入列名
     */
    public static String addInput4ColName(TableInfo tableInfo) {
        // 输入列名
        return Messages.showInputDialog("Column Name:", "Input Column Name That You Want Add:", Messages.getQuestionIcon(), "Demo", new InputValidator() {
            @Override
            public boolean checkInput(String inputString) {
                if (StringUtils.isEmpty(inputString)) {
                    return false;
                }
                for (ColumnInfo columnInfo : tableInfo.getFullColumn()) {
                    if (columnInfo.getName().equals(inputString)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean canClose(String inputString) {
                return this.checkInput(inputString);
            }
        });
    }

    /**
     * 获取输入的待删除的额外列的值，并判断是否与传入的表信息中列名重复
     *
     * @param tableInfo 表信息
     * @return 输入列名
     */
    public static String removeInput4ColName(TableInfo tableInfo) {
        // 输入列名
        return Messages.showInputDialog("Column Name:", "Input Column Name That You Want Remove:", Messages.getQuestionIcon(), "", new InputValidator() {
            @Override
            public boolean checkInput(String inputString) {
                if (StringUtils.isEmpty(inputString)) {
                    return true;
                }
                for (ColumnInfo columnInfo : tableInfo.getFullColumn()) {
                    if (columnInfo.getName().equals(inputString)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean canClose(String inputString) {
                return this.checkInput(inputString);
            }
        });
    }

    /**
     * 选择搜索java文件对话框
     *
     * @param project 项目
     * @return com.intellij.psi.PsiJavaFile 选择的java文件
     */
    public static PsiJavaFile showAndGetChooseJavaFile(Project project) {
        TreeFileChooserFactory instance = TreeFileChooserFactory.getInstance(project);
        TreeFileChooser.PsiFileFilter fileFilter = file -> file.getName().endsWith(".java");
        TreeFileChooser javaFileChooser = instance.createFileChooser("java文件选择器", null, null, fileFilter);
        javaFileChooser.showDialog();
        return (PsiJavaFile)javaFileChooser.getSelectedFile();
    }
}
