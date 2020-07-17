package com.github.gingjing.plugin.converter.ymlandpro.pro2yml;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toMap;

/**
 * yaml树构建器
 *
 * @author xqchen
 * @date: 2020年 07月10日 00时00分
 * @version: 1.0
 */
class TreeBuilder {

    private final Properties properties;
    private final static Pattern PATTERN = Pattern.compile("[0-9]+");
    private final boolean useNumericKeysAsArrayIndexes;

    public TreeBuilder(Properties properties, boolean useNumericKeysAsArrayIndexes) {
        this.properties = properties;
        this.useNumericKeysAsArrayIndexes = useNumericKeysAsArrayIndexes;
    }

    public TreeBuilder(Properties properties) {
        this(properties, true);
    }

    public PropertyTree build() {
        PropertyTree root = new PropertyTree();
        properties.stringPropertyNames().stream()
                .collect(toMap(
                        this::splitPropertyName,
                        propertyName -> ValueConverter.asObject(properties.getProperty(propertyName))))
                .forEach(root::appendBranchFromKeyValue);
        return root;
    }


    private List<String> splitPropertyName(String property) {
        List<String> strings = Arrays.asList(property.split("\\."));
        List<String> result = useNumericKeysAsArrayIndexes ? applyArrayNotation(strings) : strings;
        Collections.reverse(result);
        return result;
    }

    private List<String> applyArrayNotation(List<String> strings) {
        List<String> result = new ArrayList<>();
        for (String element : strings) {
            Matcher matcher = PATTERN.matcher(element);
            if (matcher.matches()) {
                int index = result.size() - 1;
                result.set(index, result.get(index) + String.format("[%s]", element));
            } else {
                result.add(element);
            }
        }
        return result;
    }
}
