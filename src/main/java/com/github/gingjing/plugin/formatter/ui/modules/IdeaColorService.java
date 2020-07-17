package com.github.gingjing.plugin.formatter.ui.modules;

import com.intellij.util.ui.UIUtil;

/**
 * 颜色设置服务
 *
 * @author: Jmm
 * @date: 2020年06月5日15时16分
 * @version: 1.0
 */
public class IdeaColorService extends ColorService {
    @Override
    protected <T> T internalForCurrentTheme(T[] objects) {
        if (objects == null) {
            return null;
        } else if (UIUtil.isUnderDarcula() && objects.length > 1) {
            return objects[1];
        } else {
            return objects[0];
        }
    }
}
