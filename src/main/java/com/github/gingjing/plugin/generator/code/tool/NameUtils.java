package com.github.gingjing.plugin.generator.code.tool;

import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命名工具类
 *
 * @author makejava
 * @modify gingjingdm
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class NameUtils {
    private volatile static NameUtils nameUtils;

    /**
     * 单例模式
     */
    public static NameUtils getInstance() {
        if (nameUtils == null) {
            synchronized (NameUtils.class) {
                if (nameUtils == null) {
                    nameUtils = new NameUtils();
                }
            }
        }
        return nameUtils;
    }

    /**
     * 私有构造方法
     */
    NameUtils() {
    }

    /**
     * 转驼峰命名正则匹配规则
     */
    private static final Pattern TO_HUMP_PATTERN = Pattern.compile("[-_]([a-z0-9])");
    private static final Pattern TO_LINE_PATTERN = Pattern.compile("[A-Z]+");

    /**
     * 首字母大写方法
     *
     * @param name 名称
     * @return 结果
     */
    public String firstUpperCase(String name) {
        return StringUtils.capitalize(name);
    }

    /**
     * 首字母小写方法
     *
     * @param name 名称
     * @return 结果
     */
    public String firstLowerCase(String name) {
        return StringUtils.uncapitalize(name);
    }

    /**
     * 驼峰转下划线，全小写
     *
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public String hump2Underline(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        Matcher matcher = TO_LINE_PATTERN.matcher(str);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            if (matcher.start() > 0) {
                matcher.appendReplacement(buffer, "_" + matcher.group(0).toLowerCase());
            } else {
                matcher.appendReplacement(buffer, matcher.group(0).toLowerCase());
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 通过java全名获取类名
     * 1. 由create sql和IntelliJ DatabaseTool生成
     * 2. 由select java class生成，判断：
     *   a. 若包含泛型直接截取
     *   b. 若包含泛型则特殊处理
     *
     * @param fullName 全名 java.util.List<A></A>
     * @return 类名
     */
    public String getClsNameByFullName(String fullName) {
        if (!CreateModeEnum.SELECT_MODEL.equals(CacheDataUtils.getInstance().getCreateMode())) {
            return fullName.substring(fullName.lastIndexOf('.') + 1);
        }
        return getShortType(fullName);
    }

    private static final String LEFT_ANGEL_BRACKET = "<";
    private static final String DOT = ".";
    private static final String COMMA = ",";


    private String getShortType(String typeStr) {
        if (!typeStr.contains(LEFT_ANGEL_BRACKET)) {
            return typeStr.contains(DOT)
                    ? typeStr.substring(typeStr.lastIndexOf(DOT) + 1)
                    : typeStr;
        }
        String[] splitByLeftAngelBracket = typeStr.split(LEFT_ANGEL_BRACKET);
        String leftStr = getShortType(splitByLeftAngelBracket[0]);
        String rightStr = splitByLeftAngelBracket[1];
        if (!rightStr.contains(COMMA)) {
            String innerType = null;
            if (rightStr.contains(LEFT_ANGEL_BRACKET)) {
                innerType = getShortType(rightStr);
            } else {
                innerType = rightStr.contains(DOT)
                        ? rightStr.substring(rightStr.lastIndexOf(DOT) + 1)
                        : rightStr;
            }
            return leftStr + LEFT_ANGEL_BRACKET + innerType;
        } else {
            StringJoiner sj = new StringJoiner(", ");
            String[] splitByComma = rightStr.split(COMMA);
            for (String s : splitByComma) {
                sj.add(getShortType(s));
            }
            return leftStr + LEFT_ANGEL_BRACKET + sj.toString();
        }
    }

    /**
     * 下划线中横线命名转驼峰命名（属性名）
     *
     * @param name 名称
     * @return 结果
     */
    public String getJavaName(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        // 强转全小写
        name = name.toLowerCase();
        Matcher matcher = TO_HUMP_PATTERN.matcher(name.toLowerCase());
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 下划线中横线命名转驼峰命名（类名）
     *
     * @param name 名称
     * @return 结果
     */
    public String getClassName(String name) {
        return firstUpperCase(getJavaName(name));
    }

    /**
     * 任意对象合并工具类
     *
     * @param objects 任意对象
     * @return 合并后的字符串结果
     */
    public String append(Object... objects) {

        if (objects == null || objects.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Object s : objects) {
            if (s != null) {
                builder.append(s);
            }
        }
        return builder.toString();
    }
}
