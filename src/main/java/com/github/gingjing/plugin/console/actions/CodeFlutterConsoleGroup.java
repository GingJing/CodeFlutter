package com.github.gingjing.plugin.console.actions;

import com.github.gingjing.plugin.formatter.formatAction.ConsoleJsonFormatAction;
import com.github.gingjing.plugin.formatter.formatAction.ConsoleSqlFormatAction;
import com.intellij.execution.impl.ConsoleViewUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * GoCoderEasier插件菜单组，根据不同条件生成不同子菜单。
 *
 * @author: Jmm
 * @date: 2020年 05月20日 22时20分
 * @version: 1.0
 */
public class CodeFlutterConsoleGroup extends ActionGroup {

    public CodeFlutterConsoleGroup() {
    }

    /** 是否存在子菜单 */
    private boolean noChildrenMenu;

    public CodeFlutterConsoleGroup(boolean noChildrenMenu) {
        this.noChildrenMenu = noChildrenMenu;
    }

    @Override
    public boolean hideIfNoVisibleChildren() {
        return this.noChildrenMenu;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabledAndVisible(editor != null && ConsoleViewUtil.isConsoleViewEditor(editor));
        super.update(e);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        Project project = getEventProject(e);
        if (project == null) {
            return getEmptyAnAction();
        }
        Editor editor = e.getDataContext().getData(LangDataKeys.EDITOR);
        if (editor == null || !ConsoleViewUtil.isConsoleViewEditor(editor)) {
            return getEmptyAnAction();
        }
        List<AnAction> anActions = new ArrayList<>();
        anActions.add(new ConsoleSqlFormatAction("Sql Format"));
        anActions.add(new ConsoleJsonFormatAction("Json Format"));
        return anActions.toArray(new AnAction[0]);
    }


    /**
     * 获取返回空菜单数组
     *
     * @return com.intellij.openapi.actionSystem.AnAction[]
     * @since v1.0.0
     * @date: 2020/5/20
     */
    private AnAction[] getEmptyAnAction() {
        this.noChildrenMenu = true;
        return AnAction.EMPTY_ARRAY;
    }
}
