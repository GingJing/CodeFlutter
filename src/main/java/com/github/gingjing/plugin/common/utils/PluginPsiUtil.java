package com.github.gingjing.plugin.common.utils;

import com.github.gingjing.plugin.common.constants.PluginFileConstants;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiFile;

import static com.github.gingjing.plugin.converter.constants.MsgConstants.NO_FILE_SELECTED;
import static com.github.gingjing.plugin.converter.constants.MsgConstants.SELECT_FILE_FIRST;
import static com.github.gingjing.plugin.converter.constants.YmlAndPropertiesFileConstants.GROUP_DISPLAY_ID;

/**
 * Psi工具类
 *
 * @author: gingjingdm
 * @date: 2020年 07月01日 00时12分
 * @version: 1.0
 */
public class PluginPsiUtil {

    private static final Logger LOG = Logger.getInstance(PluginPsiUtil.class);

    private PluginPsiUtil() {
    }

    public static boolean isJavaFile(AnActionEvent event) {
        if (event == null) {
            return false;
        }
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        return psiFile != null && psiFile.getName().endsWith(PluginFileConstants.JAVA_FILE);
    }

    public static boolean isXmlFile(AnActionEvent event) {
        if (event == null) {
            return false;
        }
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        return psiFile != null && psiFile.getName().endsWith(PluginFileConstants.XML_FILE);
    }

    public static boolean isSqlFile(AnActionEvent event) {
        if (event == null) {
            return false;
        }
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        return psiFile != null && psiFile.getName().endsWith(PluginFileConstants.SQL_FILE);
    }

    public static boolean isIgnoreFile(AnActionEvent event) {
        return !isJavaFile(event) && !isXmlFile(event) && !isSqlFile(event);
    }

    /**
     * 获取选择中的文件
     *
     * @param event                事件
     * @param showNotifications    是否显示提示
     * @return PsiFile
     */
    public static PsiFile getSelectedFile(AnActionEvent event, boolean showNotifications) {
        //获取当前打开的文件
        PsiFile selectedFile = event.getData(LangDataKeys.PSI_FILE);
        if (selectedFile == null) {
            if (showNotifications) {
                Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, NO_FILE_SELECTED,
                        SELECT_FILE_FIRST, NotificationType.ERROR));
            }
            return null;
        }

        return selectedFile;
    }

    /**
     * 获取选中的文件类型
     *
     * @param event              事件
     * @param showNotifications  是否显示提示
     */
    public static String getFileType(AnActionEvent event, boolean showNotifications) {
        //获取打开的文件
        PsiFile selectedFile = getSelectedFile(event, showNotifications);
        if (null == selectedFile) {
            return null;
        } else {
            return selectedFile.getFileType().getName();
        }

    }
}
