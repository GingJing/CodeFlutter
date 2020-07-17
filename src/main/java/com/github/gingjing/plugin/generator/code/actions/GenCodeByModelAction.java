package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.common.utils.PluginPsiUtil;
import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.github.gingjing.plugin.generator.code.ui.SetTableInfoByModelOrSqlDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 选择java实体类生成代码菜单
 *
 * @author: GingJingDM
 * @date: 2020年 07月05日 19时10分
 * @version: 1.0
 */
public class GenCodeByModelAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getDataContext().getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        Project project = e.getProject();
        if (project == null) {
            project = ProjectUtils.getCurrProject();
        }
        PsiFile psiFile = e.getDataContext().getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }
        PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass selectedClass = (PsiClass) PsiTreeUtil.getContextOfType(referenceAt, new Class[]{PsiClass.class});
        CacheDataUtils cacheDataUtils = CacheDataUtils.getInstance();
        cacheDataUtils.setSelectClass(selectedClass);
        cacheDataUtils.setCreateMode(CreateModeEnum.SELECT_MODEL);
        //开始处理
        new SetTableInfoByModelOrSqlDialog(project).open();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getDataContext().getData(CommonDataKeys.EDITOR);
        e.getPresentation().setVisible(editor != null && PluginPsiUtil.isJavaFile(e));
    }
}
