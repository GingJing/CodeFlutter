package com.github.gingjing.plugin.converter.ymlandpro.pro2yml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * yml打印器
 *
 * @author xqchen
 * @date: 2020年 07月09日 23时56分
 * @version: 1.0
 */
public class YmlPrinter {

    private final Map<String, Object> mainMap;

    public YmlPrinter(PropertyTree mainMap) {
        this.mainMap = mainMap;
    }

    public String invoke() {
        Yaml yaml = new Yaml(dumperOptions());
        return yaml.dump(mainMap);
    }

    private DumperOptions dumperOptions() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndent(4);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        dumperOptions.setLineBreak(DumperOptions.LineBreak.UNIX);
        dumperOptions.setPrettyFlow(true);
        return dumperOptions;
    }
}
