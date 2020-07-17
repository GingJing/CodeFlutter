package com.github.gingjing.plugin.formatter.formatAction;

import com.github.gingjing.plugin.formatter.ui.FormatDialog;
import com.intellij.openapi.editor.Editor;

/**
 * 普通编辑器格式化事件菜单
 *
 * @author: gingjingdm
 * @date: 2020年 06月28日 21时09分
 * @version: 1.0
 */
public class NormalEditorFormatAction extends AbstractFormatAction {

    @Override
    protected void showDialog(Editor editor) {
        FormatDialog dialog = new FormatDialog(editor, null);
        dialog.open();
    }

}
