package com.github.gingjing.plugin.formatter.formatAction;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;


/**
 * 抽象格式化动作事件器
 *
 * @author: gingjingdm
 * @date: 2020年 06月27日 20时24分
 * @version: 1.0
 */
public abstract class AbstractFormatAction extends AnAction {

    public AbstractFormatAction() {
    }

    public AbstractFormatAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
        super(text);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        String text = editor.getSelectionModel().getSelectedText();
        System.out.println(text);
        showDialog(editor);
    }

    /**
     * 展示对话框
     *
     * @param editor 编辑器
     */
    protected abstract void showDialog(Editor editor);



}
