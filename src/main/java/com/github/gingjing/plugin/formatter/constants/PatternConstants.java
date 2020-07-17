package com.github.gingjing.plugin.formatter.constants;

import java.util.regex.Pattern;

/**
 * 模式匹配常量
 *
 * @author: Jmm
 * @date: 2020年05月09日15时13分
 * @version: 1.0
 */
public class PatternConstants {

    /* ==================================== 数据库字段常用类型模式常量 =================================== */

    public static final Pattern VARCHAR = Pattern.compile("varchar(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern CHAR = Pattern.compile("char(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern STRING = Pattern.compile("string", Pattern.CASE_INSENSITIVE);
    public static final Pattern TEXT = Pattern.compile("text", Pattern.CASE_INSENSITIVE);
    public static final Pattern DECIMAL = Pattern.compile("decimal(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern DECIMAL_TWO = Pattern.compile("decimal(\\(\\d+,\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern TINYINT = Pattern.compile("tinyint(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern INTEGER = Pattern.compile("integer", Pattern.CASE_INSENSITIVE);
    public static final Pattern INT = Pattern.compile("int(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern INT4 = Pattern.compile("int4", Pattern.CASE_INSENSITIVE);
    public static final Pattern INT8 = Pattern.compile("int8", Pattern.CASE_INSENSITIVE);
    public static final Pattern BIGINT = Pattern.compile("bigint(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern DATE = Pattern.compile("date(\\(\\d+\\))?", Pattern.CASE_INSENSITIVE);
    public static final Pattern DATETIME = Pattern.compile("datetime", Pattern.CASE_INSENSITIVE);
    public static final Pattern TIMESTAMP = Pattern.compile("timestamp", Pattern.CASE_INSENSITIVE);
    public static final Pattern BOOLEAN = Pattern.compile("boolean", Pattern.CASE_INSENSITIVE);

    /* ==================================== 数据库字段常用类型模式常量 =================================== */
}
