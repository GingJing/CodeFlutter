package com.github.gingjing.plugin.converter.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.gingjing.plugin.common.exception.CodeFlutterException;
import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.generator.code.tool.CollectionUtil;
import com.google.common.base.Preconditions;
import com.intellij.lang.properties.PropertiesFileProcessor;
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl;
import com.intellij.openapi.util.PropertiesUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.util.*;

/**
 * properties文件解析器
 *
 * @author: GingJingDM
 * @date: 2020年 07月19日 12时02分
 * @version: 1.0
 */
public class PropertiesParser {

    /**
     * 将properties文件解析为properties map
     *
     * @param file      properties文件
     * @param encoding  文件编码
     * @return 解析后的properties map
     */
    public static Map<String, Object> propertiesFileToPropertiesMap(VirtualFile file, String encoding) {

        Map<String, Object> propertiesMap = null;
        try(InputStream inputStream = file.getInputStream();InputStreamReader fr = new InputStreamReader(inputStream, encoding)) {
            Map<String, String> loadProperties = PropertiesUtil.loadProperties(fr);
            if (!CollectionUtil.isEmpty(loadProperties)) {
                propertiesMap = new LinkedHashMap<>();
                for (Map.Entry<String, String> entry : loadProperties.entrySet()) {
                    propertiesMap.put(entry.getKey(), entry.getValue());
                }
            }
            return propertiesMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertiesMap;
    }

    /**
     * 直接将解析的properties Map转换为properties内容字符串
     *
     * @param proMap 解析后的properties Map
     * @return properties内容字符串
     */
    public static String map2Content(Map<String, Object> proMap) {
        if (CollectionUtil.isEmpty(proMap)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : proMap.entrySet()) {
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append(System.lineSeparator());
        }
        return builder.toString();
    }


    /**
     * 将解析的yaml Map转换为properties Map
     *
     * @param yaml 解析后的yaml Map
     * @return properties Map
     */
    public static Map<String, Object> ymlMap2PropertiesMap(Map<String, Object> yaml) {
        Preconditions.checkArgument(yaml != null && yaml.size() > 0, "yaml 文件内容为空");
        Map<String, Object> properties = new LinkedHashMap<>(yaml.size() * 4);
        parser("", yaml, properties);
        return properties;
    }

    /**
     * 解析代表yaml内容的对象，并将结果放进代表properties文件内容的映射集合
     *
     * @param prefix 前缀
     * @param yaml   yaml内容对象
     * @param result properties文件内容的映射集合
     */
    private static void parser(String prefix, Object yaml, Map<String, Object> result) {
        Preconditions.checkArgument(yaml != null, "parser get null yaml, prefix:%s", prefix);
        if (yaml instanceof Map) {
            Map<String, Object> mapYaml = (Map<String, Object>) yaml;
            for (Map.Entry<String, Object> entry : mapYaml.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String subPreFix = PluginStringUtil.isBlank(prefix)
                        ? "" + key
                        : prefix + "." + key;
                if (value instanceof Map) {
                    parser(subPreFix, value, result);
                } else if (value instanceof List) {
                    parser(subPreFix, value, result);
                } else {
                    result.put(subPreFix, value);
                }
            }
        } else if (yaml instanceof List) {
            List<Object> listYaml = (List<Object>) yaml;
            for (int i = 0; i < listYaml.size(); i++) {
                Object value = listYaml.get(i);
                String subPreFix = PluginStringUtil.isBlank(prefix)
                        ? "[" + i + "]"
                        : String.format("%s.[%s]", prefix, i);

                if (value instanceof Map || value instanceof List) {
                    parser(subPreFix, value, result);
                } else {
                    result.put(subPreFix, value);
                }
            }
        }
    }

    /**
     * 将properties map 写入指定properties文件，若为存在则创建
     *
     * @param properties        代表properties文件内容的映射
     * @param propertiesFile    指定properties文件
     * @throws IOException 发生IO异常
     */
    private void writeProperties(Map<String, Object> properties, File propertiesFile) throws IOException {
        if (!propertiesFile.exists()) {
            if (!propertiesFile.createNewFile()) {
                throw new CodeFlutterException(String.format("properties文件: %s 创建失败！", propertiesFile.getAbsolutePath()));
            }
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(propertiesFile));
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            bw.write(entry.getKey() + "=" + entry.getValue());
            bw.newLine();
        }
        bw.close();
    }
}
