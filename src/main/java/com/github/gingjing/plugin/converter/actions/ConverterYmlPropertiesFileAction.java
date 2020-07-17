package com.github.gingjing.plugin.converter.actions;

import com.github.gingjing.plugin.common.utils.PluginPsiUtil;
import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.converter.ymlandpro.pro2yml.Properties2Yml;
import com.github.gingjing.plugin.converter.ymlandpro.yml2pro.Yml2Properties;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.github.gingjing.plugin.converter.constants.MsgConstants.*;
import static com.github.gingjing.plugin.converter.constants.YmlAndPropertiesFileConstants.*;


/**
 * 转换yml文件与properties文件菜单
 *
 * @author xqchen
 * @date: 2020年 07月10日 00时09分
 * @version: 1.0
 */
public class ConverterYmlPropertiesFileAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        // get file type
        final String fileType = PluginPsiUtil.getFileType(event, true);
        final PsiFile selectedFile = PluginPsiUtil.getSelectedFile(event, true);
        if (PluginStringUtil.isBlank(fileType) || null == selectedFile) {
            return;
        }
        final VirtualFile file = selectedFile.getVirtualFile();

        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                @NotNull String content = new String(file.contentsToByteArray());
                //YAML文件处理
                if (YAML.equals(fileType)) {
                    @NotNull String yamlContent = Yml2Properties.fromContent(content).convert();
                    file.setCharset(file.getCharset());
                    file.rename(this, file.getNameWithoutExtension() + PROPERTIES_SUFFIX);
                    file.setBinaryContent(yamlContent.getBytes());

                    Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, SUCCESS,
                            YAML_TO_PROPERTIES, NotificationType.INFORMATION));
                }//PROPERTIES文件处理
                else if (PROPERTIES.equals(fileType)) {
                    String propsContent = Properties2Yml.fromContent(content).convert();
                    file.rename(this, file.getNameWithoutExtension() + YAML_SUFFIX);
                    file.setBinaryContent(propsContent.getBytes());
                    Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, SUCCESS,
                            PROPERTIES_TO_YAML, NotificationType.INFORMATION));
                } else {
                    Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, INCORRECT_FILE_SELECTED,
                            SELECT_PROPS_OR_YAML_FIRST, NotificationType.ERROR));
                }

            } catch (IOException e) {
                Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID,
                        CANNOT_RENAME_FILE, e.getMessage(), NotificationType.ERROR));
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        Presentation presentation = event.getPresentation();
        // get file type
        final String fileType = PluginPsiUtil.getFileType(event, false);
        //根据类型动态控制Action的隐藏显示
        if (!PluginStringUtil.isBlank(fileType) && presentation.isEnabled()) {
            event.getPresentation().setEnabledAndVisible(YAML.equals(fileType) || PROPERTIES.equals(fileType));
        } else {
            event.getPresentation().setEnabledAndVisible(false);
        }
    }

}
