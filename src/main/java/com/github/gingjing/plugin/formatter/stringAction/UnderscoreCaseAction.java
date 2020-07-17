package com.github.gingjing.plugin.formatter.stringAction;


import com.github.gingjing.plugin.formatter.util.StringUtil;

/**
 * @author D丶Cheng
 * @description 普通字符串转换下划线
 */
public class UnderscoreCaseAction extends AbstractStringAction {

    @Override
    protected String transformStr(String selectedText) {
        return StringUtil.textToUnderscoreCase(selectedText);
    }
}
