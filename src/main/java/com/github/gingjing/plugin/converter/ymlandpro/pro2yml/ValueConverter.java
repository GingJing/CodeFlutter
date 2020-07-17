package com.github.gingjing.plugin.converter.ymlandpro.pro2yml;

/**
 * @author xqchen
 * @date: 2020年 07月10日 00时01分
 * @version: 1.0
 */
public class ValueConverter {

    public final static String TRUE_STR = "true";
    public final static String FALSE_STR = "false";

    public static Object asObject(String string) {
        if (TRUE_STR.equalsIgnoreCase(string) || FALSE_STR.equalsIgnoreCase(string)) {
            return Boolean.valueOf(string);
        } else {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException ignored) {
            }
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException ignored) {
            }
            try {
                return Double.parseDouble(string);
            } catch (NumberFormatException ignored) {
            }
            return string;
        }
    }
}
