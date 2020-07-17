package com.github.gingjing.plugin.formatter.constants;

/**
 * Sql格式化消息枚举
 *
 * @author: gingjingdm
 * @date: 2020年05月22日16时21分
 * @version: 1.0
 */
public enum SqlFormatMessageEnum {
    /** 无占位符 */
    NO_PLACEHOLDER("此sql字符串无占位符"),
    /** 无参数列表 */
    NO_PARAM_LIST("无法解析出sql的参数列表"),
    /** 非法SQL语句 */
    WRONG_SQL("非法SQL语句"),
    /** 均有 */
    ALL("SQL语句与参数列表均解析");

    private String massage;

    SqlFormatMessageEnum(String massage) {
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
