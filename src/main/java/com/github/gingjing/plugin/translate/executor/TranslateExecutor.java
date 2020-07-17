package com.github.gingjing.plugin.translate.executor;

/**
 * 翻译执行器
 *
 * @author gingjingdm
 * @date 2020年 7月4日 16时32分
 */
public interface TranslateExecutor {

    /**
     * 翻译
     *
     * @param text 文本
     * @return {@link String}
     */
    String translate(String text);

    <T> T getResponse(String text);

}
