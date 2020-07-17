package com.github.gingjing.plugin.translate.factory;

import com.github.gingjing.plugin.translate.constants.TranslateConstant;
import com.github.gingjing.plugin.translate.executor.TranslateExecutor;
import com.github.gingjing.plugin.translate.executor.impl.*;
import com.github.gingjing.plugin.translate.response.BaiDuResponse;
import com.github.gingjing.plugin.translate.response.GoogleResponse;
import com.github.gingjing.plugin.translate.response.JinShanResponse;
import com.github.gingjing.plugin.translate.response.YouDaoResponse;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

/**
 * 翻译对象工厂
 *
 * @author: GingJingDM
 * @date: 2020年 07月04日 20时28分
 * @version: 1.0
 */
public class TranslateFactory {

    private volatile static TranslateFactory factory;

    private TranslateFactory() {}

    /**
     * 单例模式
     */
    public static TranslateFactory getInstance() {
        if (factory == null) {
            synchronized (TranslateFactory.class) {
                if (factory == null) {
                    factory = new TranslateFactory();
                }
            }
        }
        return factory;
    }

    private final Map<Class<?>, TranslateExecutor> EN_TO_CH_MAP = ImmutableMap.<Class<?>, TranslateExecutor>builder()
            .put(BaiDuResponse.class, new BaiDuTranslateExecutor())
            .put(JinShanResponse.class, new JinShanTranslateExecutor())
            .put(YouDaoResponse.class, new YouDaoEn2ChTranslator())
            .put(GoogleResponse.class, new GoogleEn2ChTranslator())
            .build();

    private final Map<Class<?>, TranslateExecutor> CH_TO_EN_MAP = ImmutableMap.<Class<?>, TranslateExecutor>builder()
            .put(BaiDuResponse.class, new BaiDuTranslateExecutor())
            .put(JinShanResponse.class, new JinShanTranslateExecutor())
            .put(YouDaoResponse.class, new YouDaoCh2EnTranslator())
            .put(GoogleResponse.class, new GoogleCh2EnTranslator())
            .build();


    public TranslateExecutor getTranslateExecutor(Class<?> clazz, String translateType) {
        TranslateExecutor executor;
        if (Objects.equals(translateType, TranslateConstant.ZH_CN_TO_EN)) {
            executor = CH_TO_EN_MAP.get(clazz);
            if (executor == null) {
                return new BaiDuTranslateExecutor();
            }
        } else {
            executor = EN_TO_CH_MAP.get(clazz);
        }
        return executor;
    }


    public  <T> T getResponse(Class<T> clazz, String text, String translateType) {
        return getTranslateExecutor(clazz, translateType).getResponse(text);
    }
}
