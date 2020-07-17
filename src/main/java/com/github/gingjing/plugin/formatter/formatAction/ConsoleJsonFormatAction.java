package com.github.gingjing.plugin.formatter.formatAction;

import com.github.gingjing.plugin.formatter.enums.FormatEnum;
import com.github.gingjing.plugin.formatter.ui.FormatDialog;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * 控制台日志json格式化事件菜单
 *
 * @author: Jmm
 * @date: 2020年06月5日15时16分
 * @version: 1.0
 */
public class ConsoleJsonFormatAction extends AbstractFormatAction {

    public ConsoleJsonFormatAction() {
    }

    public ConsoleJsonFormatAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
        super(text);
    }

    @Override
    protected void showDialog(Editor editor) {
        FormatDialog dialog = new FormatDialog(editor, FormatEnum.JSON);
        dialog.open();
    }


}
