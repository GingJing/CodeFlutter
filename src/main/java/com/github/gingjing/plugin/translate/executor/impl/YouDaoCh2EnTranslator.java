package com.github.gingjing.plugin.translate.executor.impl;

/**
 * 有道翻译：中译英
 *
 * @author gingjingdm
 * @date 2020年 07月04日 12时51分
 */
public class YouDaoCh2EnTranslator extends AbstractYouDaoTranslator {

    private static final String URL = "http://fanyi.youdao.com/translate?&doctype=json&type=ZH_CN2EN&i=%s";

    @Override
    protected String getUrl() {
        return URL;
    }


}
