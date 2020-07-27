package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.github.gingjing.plugin.generator.code.tool.StringUtils;
import com.github.gingjing.plugin.generator.code.ui.CreateSqlDialog;
import com.github.gingjing.plugin.generator.code.ui.SetTableInfoByModelOrSqlDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import static com.github.gingjing.plugin.generator.code.constants.MsgValue.*;

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
        // TODO 2020.07.21需要做的：schema赋值的时机是否需要修改？
        CreateSqlDialog dialog = new CreateSqlDialog();
        dialog.open();
        CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
        String createSql = cacheDataUtils.getCurrCreateSql();
        if (StringUtils.isEmpty(createSql)) {
            return;
        }
        try {
            // 校验类型映射
            if (!TableInfoService.getInstance(project).typeValidator(createSql)) {
                // 没通过不打开窗口
                return;
            }
            // 开始处理
            new SetTableInfoByModelOrSqlDialog(project).open();
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(String.format("%s，原因====>%s", CREATE_BY_SQL_FAILED, ex.getMessage()), TYPE_VALIDATOR);
        }
    }
}
