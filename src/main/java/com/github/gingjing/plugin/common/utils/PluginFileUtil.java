package com.github.gingjing.plugin.common.utils;

import com.github.gingjing.plugin.common.constants.PluginFileConstants;
import com.github.gingjing.plugin.common.exception.CodeFlutterException;
import com.github.gingjing.plugin.converter.parser.PropertiesParser;
import com.github.gingjing.plugin.converter.parser.YamlParser;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 插件文件工具
 *
 * @author: GingJingDM
 * @date: 2020年 07月18日 21时16分
 * @version: 1.0
 */
public class PluginFileUtil {

    private static final String ENCODING = "utf-8";
    private static final String DOT = ".";

    public static void yml2Properties(PsiFile psiFile, Charset encoding) {
        if (psiFile == null) {
            return;
        }
        VirtualFile file = psiFile.getVirtualFile();
        Charset charset = Charset.forName(ENCODING);
        if (encoding != null && encoding.canEncode()) {
            charset = encoding;
        }
//        List<String> lines = new LinkedList<>();

        try (OutputStream os = new FileOutputStream(changePathWithOtherExtension(file, PluginFileConstants.PROPERTIES_FILE));
             OutputStreamWriter streamWriter = new OutputStreamWriter(os, charset)) {
            if (PluginStringUtil.isNotBlank(psiFile.getText())) {
                Map<String, Object> yamlMap = YamlParser.yamlToFlattenedMap(psiFile.getText());
                Map<String, Object> proMap = PropertiesParser.ymlMap2PropertiesMap(yamlMap);
                String content = PropertiesParser.map2Content(proMap);
                streamWriter.write(content);
                streamWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CodeFlutterException(String.format("yml convert properties failed, cause of: %s", e.getMessage()));
        }
    }

    public static void properties2Yaml(PsiFile psiFile, Charset encoding) {
        if (psiFile == null) {
            return;
        }
        VirtualFile file = psiFile.getVirtualFile();
        Charset charset = Charset.forName(ENCODING);
        if (encoding != null && encoding.canEncode()) {
            charset = encoding;
        }
        try (OutputStream os = new FileOutputStream(changePathWithOtherExtension(file, PluginFileConstants.YML_FILE));
             OutputStreamWriter streamWriter = new OutputStreamWriter(os, charset)) {

            Map<String, Object> loadProperties = PropertiesParser.propertiesFileToPropertiesMap(file, ENCODING);

            String mapToYaml = YamlParser.flattenedMapToYaml(loadProperties);

            streamWriter.write(mapToYaml);
            streamWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CodeFlutterException(String.format("properties convert yml failed, cause of: %s", e.getMessage()));
        }
    }

    public static String getExtensionByFileType(FileType fileType) {
        return fileType.getDefaultExtension();
    }

    public static String getExtensionByVirtualFile(VirtualFile f) {
        return f.getExtension();
    }

    public static String changePathWithOtherExtension(VirtualFile file, String otherExtension) {
        if (file == null) {
            throw new IllegalArgumentException("文件对象为空");
        }
        if (PluginStringUtil.isBlank(otherExtension)) {
            throw new IllegalArgumentException("文件扩展名为空");
        }
        String path = null;
        if (file.getParent().exists() && file.getParent().isDirectory()) {
            path = file.getParent().getPath();
        }
        String withoutExtension = file.getNameWithoutExtension();
        return path + "/" + withoutExtension + otherExtension;
    }
}
