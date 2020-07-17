package com.github.gingjing.plugin.formatter.stringAction;


import com.github.gingjing.plugin.formatter.util.StringUtil;

/**
 * @author D丶Cheng
 * @description 普通字符串转换小驼峰
 */
public class CamelCaseAction extends AbstractStringAction {

    @Override
    protected String transformStr(String selectedText) {
        return StringUtil.textToCamelCase(selectedText, false);
    }
}
