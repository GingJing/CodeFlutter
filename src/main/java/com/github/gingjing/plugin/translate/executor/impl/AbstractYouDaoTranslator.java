package com.github.gingjing.plugin.translate.executor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.translate.executor.TranslateExecutor;
import com.github.gingjing.plugin.translate.response.YouDaoResponse;
import com.github.gingjing.plugin.translate.uils.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 有道翻译
 *
 * @author gingjingdm
 * @date 2019/09/01
 */
public abstract class AbstractYouDaoTranslator implements TranslateExecutor {

    @Override
    public String translate(String text) {
        try {
            YouDaoResponse response = getResponse(text);
            return Objects.requireNonNull(response).getTranslateResult().stream()
                .map(translateResults -> translateResults.stream().map(YouDaoResponse.TranslateResult::getTgt).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
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
    public YouDaoResponse getResponse(String text) {
        try {
            return PluginJsonUtil.fromByJackson(HttpUtil.get(String.format(getUrl(), HttpUtil.encode(text))), YouDaoResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
