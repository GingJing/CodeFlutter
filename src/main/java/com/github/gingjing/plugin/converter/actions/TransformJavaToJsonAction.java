package com.github.gingjing.plugin.converter.actions;

import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.converter.ui.ShowPojo2JsonDialog;
import com.github.gingjing.plugin.generator.code.tool.DialogUtils;
import com.github.gingjing.plugin.gsonformat.ui.NotificationCenter;
import com.google.gson.Gson;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

/**
 * java类转json事件菜单
 *
 * @author: GingJingDM
 * @date: 2020年 07月09日 22时56分
 * @version: 1.0
 */
public class TransformJavaToJsonAction extends AnAction {

    private static NotificationGroup notificationGroup;

    /**
     * 字段
     */
    public static final String FIELD = "@pojo_field";

    /**
     * 内部类
     */
    public static final String INNERCLASS = "@pojo_innerclass";

    /**
     * javadoc 注释
     */
    public static final String COMMENT = "@pojo_comment";

    @NonNls
    private static final Map<String, Object> NORMAL_TYPES = new HashMap<>();

    static {
        notificationGroup = new NotificationGroup("Java2Json.NotificationGroup", NotificationDisplayType.BALLOON, true);


        NORMAL_TYPES.put("Boolean", false);
        NORMAL_TYPES.put("Byte", 0);
        NORMAL_TYPES.put("Short", (short) 0);
        NORMAL_TYPES.put("Integer", 0);
        NORMAL_TYPES.put("Long", 0L);
        NORMAL_TYPES.put("Float", 0.0F);
        NORMAL_TYPES.put("Double", 0.0D);
        NORMAL_TYPES.put("String", "str");
        NORMAL_TYPES.put("BigDecimal", 0.0);
        NORMAL_TYPES.put("Date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        NORMAL_TYPES.put("Timestamp", System.currentTimeMillis());
        NORMAL_TYPES.put("LocalDate", LocalDate.now().toString());
        NORMAL_TYPES.put("LocalTime", LocalTime.now().toString());
        NORMAL_TYPES.put("LocalDateTime", LocalDateTime.now().toString());

    }

    private static boolean isNormalType(String typeName) {
        return NORMAL_TYPES.containsKey(typeName);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        PsiClass selectedClass = null;
        int show = MessageDialogBuilder.yesNoCancel("Pojo To Json",
                "点击'是'直接为此类生成json字符串并展示，同时复制至剪切板。\n" +
                        "点击'否'可以选择一个已存在的javabean生成转换的json字符串。\n" +
                        "点击'取消'将退出此次操作")
                .project(project)
                .yesText("是")
                .noText("否")
                .cancelText("取消")
                .show();
        if (show == Messages.YES) {
            Editor editor = e.getDataContext().getData(CommonDataKeys.EDITOR);
            PsiFile psiFile = e.getDataContext().getData(CommonDataKeys.PSI_FILE);
            if (editor == null) {
                return;
            }
            if (psiFile == null) {
                return;
            }
            PsiElement referenceAt = psiFile.findElementAt(editor.getCaretModel().getOffset());
            PsiClass hostClass = (PsiClass) PsiTreeUtil.getContextOfType(referenceAt, new Class[]{PsiClass.class});
            if (hostClass == null) {
                return;
            }
            selectedClass = hostClass;
        } else if (show == Messages.NO) {
            PsiJavaFile psiJavaFile = DialogUtils.showAndGetChooseJavaFile(project);
            if (psiJavaFile == null) {
                return;
            }
            if (psiJavaFile.isDirectory()) {
                Messages.showErrorDialog("只支持文件类型", "Can't Not Support Directory");
                return;
            }
            if (psiJavaFile.getClasses().length > 1) {
                Messages.showErrorDialog("只支持单个Java文件的转换", "Only Support Single File");
                return;
            }
            selectedClass = psiJavaFile.getClasses()[0];
        } else {
            return;
        }

        if (!selectedClass.getContainingFile().getFileType().getDefaultExtension().endsWith(JavaFileType.DEFAULT_EXTENSION)) {
            Notification notification = notificationGroup.createNotification("Selection is not a POJO.", NotificationType.ERROR);
            Notifications.Bus.notify(notification, project);
            return;
        }
        try {
            Map<String, Object> allMap = this.generateMap(selectedClass, project);
            PsiClass finalSelectedClass = selectedClass;
            ApplicationManager.getApplication().invokeLater(() -> {
                ShowPojo2JsonDialog dialog = new ShowPojo2JsonDialog(finalSelectedClass.getQualifiedName(), allMap);
                dialog.open();
            });
            Gson gson = new Gson();
            String jsonString = gson.toJson(allMap);
            StringSelection selection = new StringSelection(PluginJsonUtil.formatByGson(jsonString));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            String message = "Convert " + finalSelectedClass.getName() + " to JSON success, copied to the clipboard.";
            Notification notification = notificationGroup.createNotification(message, NotificationType.INFORMATION);
            Notifications.Bus.notify(notification, project);
        } catch (Exception var15) {
            Notification notification = notificationGroup.createNotification("Generate JSON failed.", NotificationType.ERROR);
            Notifications.Bus.notify(notification, project);
        }
    }

    private Map<String, Object> generateMap(PsiClass psiClass, Project project) {
        Map<String, Object> allMap = new LinkedHashMap<>();
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        Map<String, Object> innerClassMap = new LinkedHashMap<>();
        Map<String, String> commentMap = new LinkedHashMap<>();

        PsiDocComment psiDocComment = psiClass.getDocComment();
        if (psiDocComment != null && psiDocComment.getText() != null) {
            commentMap.put(psiClass.getName(), psiDocComment.getText());
        }
        PsiField[] psiFields = psiClass.getFields();
        if (!ArrayUtils.isEmpty(psiFields)) {
            for (PsiField psiField : psiFields) {
                fieldMap.put(psiField.getName(), this.getObjectForField(psiField, project));

                PsiDocComment fieldDocComment = psiField.getDocComment();
                if (fieldDocComment != null && fieldDocComment.getText() != null) {
                    commentMap.put(psiField.getName(), fieldDocComment.getText());
                }
            }
        }
        PsiClass[] innerClasses = psiClass.getInnerClasses();
        if (!ArrayUtils.isEmpty(innerClasses)) {
            for (PsiClass innerClass : innerClasses) {
                innerClassMap.put(innerClass.getName(), this.generateMap(innerClass, project));
            }
        }
        allMap.put(FIELD, fieldMap);
        allMap.put(INNERCLASS, innerClassMap);
        allMap.put(COMMENT, commentMap);
        return allMap;
    }

    private Object getObjectForField(PsiField psiField, Project project) {
        PsiType type = psiField.getType();
        if (type instanceof PsiPrimitiveType) {
            if (type.equals(PsiType.INT)) {
                return 0;
            } else if (type.equals(PsiType.BOOLEAN)) {
                return Boolean.TRUE;
            } else if (type.equals(PsiType.BYTE)) {
                return Byte.valueOf("1");
            } else if (type.equals(PsiType.CHAR)) {
                return '-';
            } else if (type.equals(PsiType.DOUBLE)) {
                return 0.0D;
            } else if (type.equals(PsiType.FLOAT)) {
                return 0.0F;
            } else if (type.equals(PsiType.LONG)) {
                return 0L;
            } else {
                return type.equals(PsiType.SHORT) ? Short.valueOf("0") : type.getPresentableText();
            }
        } else {
            String typeName = type.getPresentableText();
            Object value = NORMAL_TYPES.get(typeName);
            if (value != null) {
                return value;
            }
            if (typeName.startsWith(List.class.getSimpleName())) {
                return this.handleList(type, project, psiField.getContainingClass());
            } else {
                PsiClass fieldClass = this.detectCorrectClassByName(typeName, psiField.getContainingClass(), project);
                return fieldClass != null ? this.generateMap(fieldClass, project) : typeName;
            }
        }
    }

    private Object handleList(PsiType psiType, Project project, PsiClass containingClass) {
        List<Object> list = new ArrayList<>();
        PsiClassType classType = (PsiClassType) psiType;
        PsiType[] subTypes = classType.getParameters();
        if (subTypes.length > 0) {
            PsiType subType = subTypes[0];
            String subTypeName = subType.getPresentableText();
            if (subTypeName.startsWith(List.class.getSimpleName())) {
                list.add(this.handleList(subType, project, containingClass));
            } else {
                PsiClass targetClass = this.detectCorrectClassByName(subTypeName, containingClass, project);
                if (targetClass != null) {
                    list.add(this.generateMap(targetClass, project));
                } else if (isNormalType(subTypeName)) {
                    list.add(NORMAL_TYPES.get(subTypeName));
                } else {
                    list.add(subTypeName);
                }
            }
        }

        return list;
    }

    private PsiClass detectCorrectClassByName(String className, PsiClass containingClass, Project project) {
        PsiClass[] classes = PsiShortNamesCache.getInstance(project).getClassesByName(className, GlobalSearchScope.projectScope(project));
        if (classes.length == 0) {
            return null;
        } else if (classes.length == 1) {
            return classes[0];
        } else {
            PsiJavaFile javaFile = (PsiJavaFile) containingClass.getContainingFile();
            PsiImportList importList = javaFile.getImportList();
            assert importList != null;
            PsiImportStatement[] statements = importList.getImportStatements();
            Set<String> importedPackageSet = new HashSet<>();

            int idx;
            for (idx = 0; idx < statements.length; ++idx) {
                importedPackageSet.add(statements[idx].getQualifiedName());
            }

            for (idx = 0; idx < classes.length; ++idx) {
                PsiClass targetClass = classes[idx];
                PsiJavaFile targetClassContainingFile = (PsiJavaFile) targetClass.getContainingFile();
                String packageName = targetClassContainingFile.getPackageName();
                if (importedPackageSet.contains(packageName + "." + targetClass.getName())) {
                    return targetClass;
                }
            }

            return null;
        }
    }

}
