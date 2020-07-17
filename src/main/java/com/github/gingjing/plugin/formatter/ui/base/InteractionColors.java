package com.github.gingjing.plugin.formatter.ui.base;

import java.awt.*;

/**
 * 交互颜色
 *
 * @author: D、deng
 * @date: 2020年 06月27日 20时24分
 * @version: 1.0
 */
public class InteractionColors {
    public final Color[] normal;
    public final Color[] rollover;
    public final Color[] pressed;
    public final Color[] disabled;

    public InteractionColors(Color[] normal, Color[] rollover, Color[] pressed, Color[] disabled) {
        this.normal = normal;
        this.rollover = rollover;
        this.pressed = pressed;
        this.disabled = disabled;
    }
}
