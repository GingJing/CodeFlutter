package com.github.gingjing.plugin.translate.executor.impl;


import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.translate.executor.TranslateExecutor;
import com.github.gingjing.plugin.translate.response.JinShanResponse;
import com.github.gingjing.plugin.translate.uils.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 金山翻译
 *
 * @author gingjingdm
 * @date 2020年 07月04日 12时51分
 */
public class JinShanTranslateExecutor implements TranslateExecutor {

    private static final String URL = "http://dict-co.iciba.com/api/dictionary.php?key=1E55091D2F202FA617472001B3AF0D39&type=json&w=%s";

    @Override
    public String translate(String text) {
        try {
            JinShanResponse response = getResponse(text);
            return Objects.requireNonNull(response).getSymbols().get(0).getParts().get(0).getMeans().get(0);
        } catch (Exception ignore) {
            return StringUtils.EMPTY;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public JinShanResponse getResponse(String text) {
        try {
            return PluginJsonUtil.fromByJackson(HttpUtil.get(String.format(URL, HttpUtil.encode(text))), JinShanResponse.class);
        } catch (Exception ignore) {
            return null;
        }
    }


}
