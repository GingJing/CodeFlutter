package com.github.gingjing.plugin.converter.ymlandpro.yml2pro;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.TreeMap;


/**
 * yml文件转properties
 *
 * @author xqchen
 * @date: 2020年 07月09日 23时51分
 * @version: 1.0
 */
public class Yml2Properties {

    TreeMap<String, Map<String, Object>> config;

    @SuppressWarnings("unchecked")
    public Yml2Properties(String contents) {
        Yaml yaml = new Yaml();
        this.config = (TreeMap<String, Map<String, Object>>) yaml.loadAs(contents, TreeMap.class);
    }

    public static Yml2Properties fromContent(String content) {
        return new Yml2Properties(content);
    }

    public String convert() {
        return toProperties(this.config);
    }

    private static String toProperties(final TreeMap<String, Map<String, Object>> config) {
        StringBuilder sb = new StringBuilder();
        for (final String key : config.keySet()) {
            sb.append(toString(key, config.get(key)));
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static String toString(final String key, final Object o) {
        StringBuilder sb = new StringBuilder();
        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            for (final String mapKey : map.keySet()) {
                if (map.get(mapKey) instanceof Map) {
                    sb.append(toString(String.format("%s.%s", key, mapKey), map.get(mapKey)));
                } else {
                    sb.append(String.format("%s.%s=%s%n", key, mapKey, (null == map.get(mapKey)) ? null : map.get(mapKey).toString()));
                }
            }
        } else {
            sb.append(String.format("%s=%s%n", key, o));
        }
        return sb.toString();
    }
}
