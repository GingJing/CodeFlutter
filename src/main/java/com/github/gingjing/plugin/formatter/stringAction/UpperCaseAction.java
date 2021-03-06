package com.github.gingjing.plugin.formatter.stringAction;

/**
 * @author D丶Cheng
 * @description 普通字符串转换大写格式
 */
public class UpperCaseAction extends AbstractStringAction {

    @Override
    protected String transformStr(String selectedText) {
        return selectedText.toUpperCase();
    }
}
