package com.github.gingjing.plugin.formatter.stringAction;

import com.github.gingjing.plugin.formatter.util.StringUtil;

/**
 * @author D丶Cheng
 * @description 普通字符串转换常量
 */
public class ConstantAction extends AbstractStringAction {

    @Override
    protected String transformStr(String selectedText) {
        return StringUtil.textToConstant(selectedText);
    }
}
