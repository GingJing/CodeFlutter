package com.github.gingjing.plugin.translate.executor.impl;

import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.translate.executor.TranslateExecutor;
import com.github.gingjing.plugin.translate.response.GoogleResponse;
import com.github.gingjing.plugin.translate.uils.HttpUtil;
import com.github.gingjing.plugin.translate.uils.TkUtil;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 谷歌翻译
 *
 * @author: gingjingdm
 * @date: 2020年 07月04日 12时53分
 * @version: 1.0
 */
public abstract class AbstractGoogleTranslator implements TranslateExecutor {

    @Override
    public String translate(String text) {
        try {
            GoogleResponse response = getResponse(text);
            return Objects.requireNonNull(response).getSentences().stream()
                    .map(GoogleResponse.SentencesBean::getTrans).collect(Collectors.joining(" "));
        } catch (Exception ignore) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 得到Url
     *
     * @return {@link String}
     */
    protected abstract String getUrl();


    @Override
    @SuppressWarnings("unchecked")
    public GoogleResponse getResponse(String text) {
        try {
            return PluginJsonUtil.fromByGson(HttpUtil.get(String.format(getUrl(), TkUtil.tk(text), HttpUtil.encode(text))), GoogleResponse.class);
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

}
