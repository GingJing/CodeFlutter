package com.github.gingjing.plugin.formatter.stringAction;

import com.github.gingjing.plugin.formatter.util.StringUtil;

/**
 * @author D丶Cheng
 * @description 普通字符串单词拆分(selectText select Text select_text select_Text SELECT_TEXT)
 */
public class WordsAction extends AbstractStringAction {

    @Override
    protected String transformStr(String selectedText) {
        return StringUtil.textToWords(selectedText);
    }
}
