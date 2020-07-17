package com.github.gingjing.plugin.gsonformat.entity;


import com.github.gingjing.plugin.gsonformat.config.Config;
import com.github.gingjing.plugin.gsonformat.config.Constant;

/**
 * Created by didm on 16/11/7.
 */
public enum ConvertLibrary {

    /**
     * Gson
     */
    Gson,
    /**
     * Jack
     */
    Jack,
    /**
     * FastJson
     */
    FastJson,
    /**
     * LoganSquare
     */
    LoganSquare,
    /**
     * AutoValue
     */
    AutoValue,
    /**
     * Other
     */
    Other,
    /**
     * Lombok
     */
    Lombok;

    public static ConvertLibrary from() {
        return from(Config.getInstant().getAnnotationStr());
    }

    private static ConvertLibrary from(String annotation) {
        if (Config.getInstant().getAnnotationStr().equals(Constant.gsonAnnotation)) {
            return Gson;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.fastAnnotation)) {
            return FastJson;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.loganSquareAnnotation)) {
            return LoganSquare;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.autoValueAnnotation)) {
            return AutoValue;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.jackAnnotation)) {
            return Jack;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.lombokAnnotation)) {
            return Lombok;
        }
        return Other;
    }
}
