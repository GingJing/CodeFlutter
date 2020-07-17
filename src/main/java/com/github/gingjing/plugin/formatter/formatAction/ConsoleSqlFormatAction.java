package com.github.gingjing.plugin.formatter.formatAction;

import com.github.gingjing.plugin.formatter.enums.FormatEnum;
import com.github.gingjing.plugin.formatter.ui.FormatDialog;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

/**
 * 控制台日志sql格式化事件菜单
 *
 * @author: Jmm
 * @date: 2020年05月20日15时16分
 * @version: 1.0
 */
public class ConsoleSqlFormatAction extends AbstractFormatAction {

    public ConsoleSqlFormatAction() {
    }

    public ConsoleSqlFormatAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
        super(text);
    }

    @Override
    protected void showDialog(Editor editor) {
        FormatDialog dialog = new FormatDialog(editor, FormatEnum.SQL);
        dialog.open();
    }


}
