package com.github.gingjing.plugin.formatter.ui.base;

import java.awt.*;


/**
 * 格式颜色
 *
 * @author: D、deng
 * @date: 2020年06月5日15时16分
 * @version: 1.0
 */
public class FormColors {
    public final Color[] normalBorder;
    public final Color[] focusBorder;
    public final Color[] errorBorder;
    public final Color[] background;
    public final Color[] text;
    public final Color[] error;
    public final Color[] fieldName;
    public final Color[] placeholder;

    public FormColors(Color[] normalBorder,
                      Color[] focusBorder,
                      Color[] errorBorder,
                      Color[] background,
                      Color[] text,
                      Color[] error,
                      Color[] fieldName,
                      Color[] placeholder) {
        this.normalBorder = normalBorder;
        this.focusBorder = focusBorder;
        this.errorBorder = errorBorder;
        this.background = background;
        this.text = text;
        this.error = error;
        this.fieldName = fieldName;
        this.placeholder = placeholder;
    }
}
