package com.github.gingjing.plugin.translate.executor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.translate.executor.TranslateExecutor;
import com.github.gingjing.plugin.translate.response.BaiDuResponse;
import com.github.gingjing.plugin.translate.uils.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 百度翻译执行器
 *
 * @author gingjingdm
 * @date 2019/09/01
 */
public class BaiDuTranslateExecutor implements TranslateExecutor {

    private static final String URL = "http://api.fanyi.baidu.com/api/trans/vip/translate?from=auto&to=zh&appid=%s&salt=%s&sign=%s&q=%s";
    private static final String APP_ID = "20190901000331058";
    private static final String KEY = "aoKt7lnVDBc4RLYrLj03";

    @Override
    public String translate(String text) {
        try {
            BaiDuResponse response = getResponse(text);
            return Objects.requireNonNull(response).getTransResult().get(0).getDst();
        } catch (Exception ignore) {
            return StringUtils.EMPTY;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public BaiDuResponse getResponse(String text) {
        try {
            String word = HttpUtil.encode(text);
            String salt = RandomStringUtils.randomNumeric(16);
            String sign = DigestUtils.md5Hex(APP_ID + word + salt + KEY);
            return PluginJsonUtil.fromByJackson(HttpUtil.get(String.format(URL, APP_ID, salt, sign, word)), BaiDuResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
