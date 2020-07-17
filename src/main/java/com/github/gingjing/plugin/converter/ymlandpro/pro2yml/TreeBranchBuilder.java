package com.github.gingjing.plugin.converter.ymlandpro.pro2yml;

import java.util.List;

/**
 * @author xqchen
 * @date: 2020年 07月09日 23时58分
 * @version: 1.0
 */
class TreeBranchBuilder {
    private final List<String> key;
    private final Object value;

    public TreeBranchBuilder(List<String> key, Object value) {
        this.key = key;
        this.value = value;
    }

    public PropertyTree build() {
        return key.stream()
                .reduce(new PropertyTree(),
                        (a, b) -> new PropertyTree(b, a.isEmpty() ? value : a),
                        (a, b) -> {
                            throw new IllegalStateException("Parallel processing is not supported");
                        }
                );
    }

}
