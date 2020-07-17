package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.generator.code.ui.ConfigTableByDbToolDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

/**
 * 使用DatabaseTool生成代码时的表配置菜单
 *
 * @author makejava
 * @modify gingjingdm
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class DbToolTableConfigAction extends AnAction {
    /**
     * 构造方法
     *
     * @param text 菜单名称
     */
    DbToolTableConfigAction(@Nullable String text) {
        super(text);
    }

    /**
     * 处理方法
     *
     * @param event 事件对象
     */
    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        new ConfigTableByDbToolDialog(project).open();
    }
}
