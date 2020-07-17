package com.github.gingjing.plugin.converter.ymlandpro.pro2yml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.Properties;

/**
 * properties转yml文件
 *
 * @author: @author xqchen
 * @date: 2020年 07月09日 23时55分
 * @version: 1.0
 */
public class Properties2Yml {

    private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Properties properties = new Properties();

    Properties2Yml(String source) {
        try {
            properties.load(new StringReader(source));
        } catch (IOException e) {
            reportError(e);
        }
    }

    Properties2Yml(File file) {
        try (InputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (IOException e) {
            reportError(e);
        }
    }

    public static Properties2Yml fromContent(String content) {
        return new Properties2Yml(content);
    }

    public static Properties2Yml fromFile(File file) {
        return new Properties2Yml(file);
    }

    public static Properties2Yml fromFile(Path path) {
        return new Properties2Yml(path.toFile());
    }

    public String convert(boolean useNumericKeysAsArrayIndexes) {
        PropertyTree tree = new TreeBuilder(properties, useNumericKeysAsArrayIndexes).build();
        tree = new ArrayProcessor(tree).apply();
        return tree.toYaml();
    }

    public String convert() {
        return convert(true);
    }

    private void reportError(IOException e) {
        LOG.error("Conversion failed", e);
    }
}
