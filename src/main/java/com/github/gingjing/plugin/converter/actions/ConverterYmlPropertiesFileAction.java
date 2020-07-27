package com.github.gingjing.plugin.converter.actions;

import com.github.gingjing.plugin.common.constants.PluginFileConstants;
import com.github.gingjing.plugin.common.notice.NotificationHelper;
import com.github.gingjing.plugin.common.utils.PluginFileUtil;
import com.github.gingjing.plugin.common.utils.PluginPsiUtil;
import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.converter.parser.YamlParser;
import com.github.gingjing.plugin.generator.code.tool.CompareFileUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemBase;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

import static com.github.gingjing.plugin.converter.constants.YmlAndPropertiesFileConstants.*;


/**
 * 转换yml文件与properties文件菜单
 *
 * @author gingjingdm
 * @date: 2020年 07月10日 00时09分
 * @version: 1.0
 */
public class ConverterYmlPropertiesFileAction extends AnAction {

    private NotificationGroup notificationGroup =
            NotificationHelper.make("Yml Properties File Converter", NotificationDisplayType.BALLOON);;

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {

        Project project = event.getProject();
        if (project == null) {
            return;
        }
        // get file type
        final PsiFile selectedFile = PluginPsiUtil.getSelectedFile(event, true);
        if (null == selectedFile) {
            return;
        }
        final String extension = PluginFileUtil.getExtensionByFileType(selectedFile.getFileType());
        if (PluginStringUtil.isBlank(extension)) {
            return;
        }
        VirtualFile file = selectedFile.getVirtualFile();
        VirtualFile dir = file.getParent();

//        VirtualFile copy = file.copy(file, dir, oldName + "_bak." + extension);
        try {

            ApplicationManager.getApplication().runWriteAction(() -> {

                if (isYaml(extension)) {
                    Map<String, Object> yamlToFlattenedMap = YamlParser.yamlToFlattenedMap(selectedFile.getText());
//                    System.out.println(yamlToFlattenedMap);
                    PluginFileUtil.yml2Properties(selectedFile, file.getCharset());
                    NotificationHelper.info("Yml Convert Properties Successfully", notificationGroup);
                } else {
                    PluginFileUtil.properties2Yaml(selectedFile, file.getCharset());
                    NotificationHelper.info("Properties Convert Yml Successfully", notificationGroup);
                }
            });
            dir.refresh(true, true);
        } catch (Exception e) {
            e.printStackTrace();
            NotificationHelper.error("File Convert Failed", notificationGroup);
        }
    }

    private boolean isYaml(String extension) {
        return PluginFileConstants.YAML_FILE_EXTENSION.equalsIgnoreCase(extension)
                || PluginFileConstants.YML_FILE_EXTENSION.equalsIgnoreCase(extension);
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
