package com.github.gingjing.plugin.converter.ymlandpro.pro2yml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xqchen
 * @date: 2020年 07月10日 00时03分
 * @version: 1.0
 */
class ArrayProcessor {
    private final static Pattern PATTERN = Pattern.compile("(.*)\\[(\\d+)\\]");

    private final PropertyTree tree;

    public ArrayProcessor(PropertyTree tree) {
        this.tree = tree;
    }

    public PropertyTree apply() {
        return process(tree);
    }

    private PropertyTree process(final PropertyTree root) {
        final PropertyTree result = new PropertyTree();
        final Map<String, List<Object>> entriesFromList = new HashMap<>(16);
        root.forEach((key, value) -> {
            Matcher matcher = PATTERN.matcher(key);
            if (matcher.find()) {
                String label = matcher.group(1);
                int index = Integer.parseInt(matcher.group(2));
                entriesFromList.put(label, processListElement(entriesFromList.get(label), value, index));
            } else {
                result.put(key, getValue(value));
            }
        });
        result.putAll(entriesFromList);
        return result;
    }

    private List<Object> processListElement(final List<Object> elements, final Object value, final int index) {
        List<Object> result = elements == null ? new ArrayList<>() : new ArrayList<>(elements);
        adjustArray(index, result);
        result.add(index, getValue(value));
        return result;
    }

    private Object getValue(final Object value) {
        return value instanceof PropertyTree ? process((PropertyTree) value) : value;
    }

    private void adjustArray(final int index, List<Object> elementList) {
        if (elementList.size() < index) {
            for (int i = elementList.size(); i < index; i++) {
                elementList.add(i, null);
            }
        }
    }
}
