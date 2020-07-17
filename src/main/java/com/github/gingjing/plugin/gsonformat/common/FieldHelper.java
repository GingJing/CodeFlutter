package com.github.gingjing.plugin.gsonformat.common;



import com.github.gingjing.plugin.gsonformat.config.Constant;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dim on 17/1/21.
 */
public class FieldHelper {

    private static Pattern pattern = Pattern.compile("(\\w+)");

    public static String generateLuckyFieldName(String name) {

        if (name == null) {
            return Constant.DEFAULT_PREFIX + new Random().nextInt(333);
        }
        Matcher matcher = pattern.matcher(name);
        StringBuilder sb = new StringBuilder("_$");
        while (matcher.find()) {
            sb.append(StringUtils.captureName(matcher.group(1)));
        }
        return sb.append(new Random().nextInt(333)).toString();
    }


}
