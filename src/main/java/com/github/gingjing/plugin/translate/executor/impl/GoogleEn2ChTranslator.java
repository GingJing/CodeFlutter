package com.github.gingjing.plugin.translate.executor.impl;

/**
 * 谷歌翻译
 *
 * @author: GingJingDM
 * @date: 2020年 07月04日 12时52分
 * @version: 1.0
 */
public class GoogleEn2ChTranslator extends AbstractGoogleTranslator {

    public static final String URL = "https://translate.google.cn/translate_a/single?client=gtx&dt=t&dt=bd&dt=rm&dj=1&ie=UTF-8&oe=UTF-8&sl=en&tl=zh-CN&hl=zh-CN&tk=%s&q=%s";

    @Override
    protected String getUrl() {
        return URL;
    }


}
