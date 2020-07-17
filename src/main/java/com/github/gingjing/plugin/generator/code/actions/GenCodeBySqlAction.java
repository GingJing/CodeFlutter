package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.github.gingjing.plugin.generator.code.tool.StringUtils;
import com.github.gingjing.plugin.generator.code.ui.CreateSqlDialog;
import com.github.gingjing.plugin.generator.code.ui.SetTableInfoByModelOrSqlDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * create sql语句生成代码菜单
 *
 * @author: GingJingDM
 * @date: 2020年 07月05日 19时10分
 * @version: 1.0
 */
public class GenCodeBySqlAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            project = ProjectUtils.getCurrProject();
        }
        CreateSqlDialog dialog = new CreateSqlDialog();
        dialog.open();
        CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
        String createSql = cacheDataUtils.getCurrCreateSql();
        cacheDataUtils.setCreateMode(CreateModeEnum.CREATE_SQL);
        if (StringUtils.isEmpty(createSql)) {
            return;
        }
        // 开始处理
        new SetTableInfoByModelOrSqlDialog(project).open();
    }
}
