package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.common.utils.PluginPsiUtil;
import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;
import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.github.gingjing.plugin.generator.code.tool.StringUtils;
import com.github.gingjing.plugin.generator.code.ui.SetTableInfoByModelOrSqlDialog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import static com.github.gingjing.plugin.generator.code.constants.MsgValue.TYPE_VALIDATOR;

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
        // TODO 去除表名前缀？
        try {
            // 校验类型映射
            if (!TableInfoService.getInstance(project).typeValidator(selectedClass)) {
                // 没通过不打开窗口
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(ex.getMessage(), TYPE_VALIDATOR);
            return;
        }
        String input = Messages.showInputDialog("请输入schema名称", MsgValue.TITLE_INFO, AllIcons.Logo_welcomeScreen, "", new InputValidator() {
            @Override
            public boolean checkInput(String inputString) {
                return !StringUtils.isEmpty(inputString);
            }

            @Override
            public boolean canClose(String inputString) {
                return checkInput(inputString);
            }
        });
        if (StringUtils.isEmpty(input)) {
            return;
        }
        cacheDataUtils.setSchema(input);
        //开始处理
        new SetTableInfoByModelOrSqlDialog(project).open();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getDataContext().getData(CommonDataKeys.EDITOR);
        e.getPresentation().setVisible(editor != null && PluginPsiUtil.isJavaFile(e));
    }
}
