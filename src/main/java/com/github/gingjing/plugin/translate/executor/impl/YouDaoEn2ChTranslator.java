package com.github.gingjing.plugin.translate.executor.impl;

/**
 * 有道翻译：英译中
 *
 * @author gingjingdm
 * @date 2020年 07月04日 12时51分
 */
public class YouDaoEn2ChTranslator extends AbstractYouDaoTranslator {

    private static final String URL = "http://fanyi.youdao.com/translate?&doctype=json&type=EN2ZH_CN&i=%s";

    @Override
    protected String getUrl() {
        return URL;
    }


}
