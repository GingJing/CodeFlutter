package com.github.gingjing.plugin.generator.code.actions;

import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.FileUtils;
import com.github.gingjing.plugin.gsonformat.ui.JsonDialog;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.IdeView;
import com.intellij.ide.actions.*;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.JavaCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.WriteActionAware;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * Json生成pojo动作菜单
 *
 * @author: GingJingDM
 * @date: 2020年 06月27日 19时59分
 * @version: 1.0
 */
public class NewClassFromJsonAction extends AnAction implements WriteActionAware {

    public NewClassFromJsonAction() {
        super(PlatformIcons.CLASS_ICON);
    }

    /**
     * java类
     */
    PsiClass psiClass;

    /**
     * 项目
     */
    Project project;

    @Override
    public void update(@NotNull final AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final Presentation presentation = e.getPresentation();
        final boolean enabled = isAvailable(dataContext);
        presentation.setEnabledAndVisible(enabled);
    }

    private boolean isAvailable(DataContext dataContext) {
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        return project != null && view != null && view.getDirectories().length != 0;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (e == null) {
            return;
        }

        final DataContext dataContext = e.getDataContext();
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null) {
            return;
        }

        project = CommonDataKeys.PROJECT.getData(dataContext);

        final PsiDirectory dir = view.getOrChooseDirectory();
        if (dir == null || project == null) {
            return;
        }
        CreateFileFromTemplateDialog.Builder builder = CreateFileFromTemplateDialog.createDialog(project);
        buildDialog(project, dir, builder);
        Ref<String> selectedTemplateName = Ref.create(null);

        builder.show(getErrorTitle(), getDefaultTemplateName(dir),
                new CreateFileFromTemplateDialog.FileCreator<PsiClass>() {

                    @Override
                    public PsiClass createFile(@NotNull String name, @NotNull String templateName) {
                        selectedTemplateName.set(templateName);
                        return NewClassFromJsonAction.this.createFile(name, templateName, dir);
                    }

                    @Override
                    public boolean startInWriteAction() {
                        return NewClassFromJsonAction.this.startInWriteAction();
                    }

                    @Override
                    @NotNull
                    public String getActionName(@NotNull String name, @NotNull String templateName) {
                        return NewClassFromJsonAction.this.getActionName(dir, name, templateName);
                    }
                },
                createdElement -> {
                    if (createdElement != null) {
                        view.selectElement(createdElement);
                        postProcess(createdElement, selectedTemplateName.get(), builder.getCustomProperties());
                    }
                });

    }


    private String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
        return IdeBundle.message(
                "progress.creating.class",
                StringUtil.getQualifiedName(Objects.requireNonNull(JavaDirectoryService.getInstance().getPackage(directory)).getQualifiedName(), newName)
        );
    }

    @NotNull
    private String getErrorTitle() {
        return IdeBundle.message("title.cannot.create.class");
    }

    @Nullable
    private String getDefaultTemplateName(@NotNull PsiDirectory dir) {
        String property = getDefaultTemplateProperty();
        return property == null ? null : PropertiesComponent.getInstance(dir.getProject()).getValue(property);
    }

    @Nullable
    private String getDefaultTemplateProperty() {
        return null;
    }

    @Nullable
    private PsiClass createFile(String name, String templateName, PsiDirectory dir) {
        psiClass = checkOrCreate(name, dir, templateName);
        if (psiClass != null) {
            CacheDataUtils.getInstance().setNewClass(project, true);
            PsiFile psiFile = psiClass.getContainingFile();
            JsonDialog dialog = new JsonDialog(psiClass, psiFile, project);
            dialog.setClass(psiClass);
            dialog.setFile(psiFile);
            dialog.setProject(project);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
        return psiClass;
    }

    /**
     * 检查并创建文件
     *
     * @param newName         新类名
     * @param directory       package
     * @param templateName    模板名
     * @return com.intellij.psi.PsiClass
     * @since  v1.0.0
     * @date   2020年07月13日 22时38分
     */
    @Nullable
    private PsiClass checkOrCreate(String newName, PsiDirectory directory, String templateName) throws IncorrectOperationException {
        PsiDirectory dir = directory;
        String className = removeExtension(templateName, newName);

        if (className.contains(".")) {
            String[] names = className.split("\\.");

            for (int i = 0; i < names.length - 1; i++) {
                dir = CreateFileAction.findOrCreateSubdirectory(dir, names[i]);
            }

            className = names[names.length - 1];
        }

        DumbService service = DumbService.getInstance(dir.getProject());
        service.setAlternativeResolveEnabled(true);
        try {
            return doCreate(dir, className, templateName);
        } finally {
            service.setAlternativeResolveEnabled(false);
        }
    }

    private PsiClass doCreate(PsiDirectory dir, String className, String templateName) throws IncorrectOperationException {
        return JavaDirectoryService.getInstance().createClass(dir, className, templateName, true);
    }

    /**
     * 去除扩展名
     *
     * @param templateName  模板名
     * @param className     类名
     * @return java.lang.String 类名
     * @since  v1.0.0
     * @date   2020年07月13日 22时36分
     */
    private String removeExtension(String templateName, String className) {
        return StringUtil.trimEnd(className, ".java");
    }

    /**
     * 构建new class对话框
     *
     * @param project   项目
     * @param directory package
     * @param builder   对话框构建者
     */
    private void buildDialog(final Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder
                .setTitle(IdeBundle.message("action.create.new.class"))
                .addKind("Class", PlatformIcons.CLASS_ICON, JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);
        LanguageLevel level = PsiUtil.getLanguageLevel(directory);
        for (FileTemplate template : FileTemplateManager.getInstance(project).getAllTemplates()) {
            final JavaCreateFromTemplateHandler handler = new JavaCreateFromTemplateHandler();
            if (handler.handlesTemplate(template) && JavaCreateFromTemplateHandler.canCreate(directory)) {
                builder.addKind(template.getName(), JavaFileType.INSTANCE.getIcon(), template.getName());
            }
        }

        builder.setValidator(new InputValidatorEx() {
            @Override
            public String getErrorText(String inputString) {
                if (inputString.length() > 0 && !PsiNameHelper.getInstance(project).isQualifiedName(inputString)) {
                    return "This is not a valid Java qualified name";
                }
                if (level.isAtLeast(LanguageLevel.JDK_10) && PsiKeyword.VAR.equals(StringUtil.getShortName(inputString))) {
                    return "var cannot be used for type declarations";
                }
                return null;
            }

            @Override
            public boolean checkInput(String inputString) {
                return true;
            }

            @Override
            public boolean canClose(String inputString) {
                return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
            }
        });
    }

    private void postProcess(PsiClass createdElement, String templateName, Map<String, String> customProperties) {
        moveCaretAfterNameIdentifier(createdElement);
    }

    //todo append $END variable to templates?
    public static void moveCaretAfterNameIdentifier(PsiNameIdentifierOwner createdElement) {
        final Project project = createdElement.getProject();
        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            final VirtualFile virtualFile = createdElement.getContainingFile().getVirtualFile();
            if (virtualFile != null) {
                if (FileDocumentManager.getInstance().getDocument(virtualFile) == editor.getDocument()) {
                    final PsiElement nameIdentifier = createdElement.getNameIdentifier();
                    if (nameIdentifier != null) {
                        editor.getCaretModel().moveToOffset(nameIdentifier.getTextRange().getEndOffset());
                    }
                }
            }
        }
    }
}
