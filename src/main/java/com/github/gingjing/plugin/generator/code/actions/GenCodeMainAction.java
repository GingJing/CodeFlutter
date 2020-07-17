package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.ui.SelectSavePathDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 代码生成菜单
 *
 * @author makejava
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class GenCodeMainAction extends AnAction {
    /**
     * 构造方法
     *
     * @param text 菜单名称
     */
    GenCodeMainAction(@Nullable String text) {
        super(text);
    }

    /**
     * 处理方法
     *
     * @param event 事件对象
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        // 校验类型映射
        if (!TableInfoService.getInstance(project).typeValidator(CacheDataUtils.getInstance().getSelectDbTable())) {
            // 没通过不打开窗口
            return;
        }
        //开始处理
        new SelectSavePathDialog(event.getProject()).open();
    }
}
