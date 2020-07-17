package com.github.gingjing.plugin.generator.doc.service;

import com.github.gingjing.plugin.generator.doc.config.Consts;
import com.github.gingjing.plugin.generator.doc.config.GenJavadocConfigComponent;
import com.github.gingjing.plugin.generator.doc.model.GenJavadocConfiguration;
import com.github.gingjing.plugin.generator.doc.util.CollectionUtil;
import com.github.gingjing.plugin.translate.executor.TranslateExecutor;
import com.github.gingjing.plugin.translate.executor.impl.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @modify gingjingdm
 * @date 2019/08/25
 */
public class TranslatorService {

    private GenJavadocConfiguration config = ServiceManager.getService(GenJavadocConfigComponent.class).getState();
    private TranslateExecutor ydCh2EnTranslator = new YouDaoCh2EnTranslator();
    private Map<String, TranslateExecutor> translatorMap = ImmutableMap.<String, TranslateExecutor>builder()
            .put("谷歌翻译", new GoogleEn2ChTranslator())
            .put("百度翻译", new BaiDuTranslateExecutor())
            .put("金山翻译", new JinShanTranslateExecutor())
            .put("有道翻译", new YouDaoEn2ChTranslator())
            .build();

    /**
     * 英译中
     *
     * @param source 源
     * @return {@link String}
     */
    public String translate(String source) {
        List<String> words = split(source);
        if (hasCustomWord(words)) {
            // 有自定义单词，使用默认模式，单个单词翻译
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                String res = getFromCustom(word);
                if (StringUtils.isBlank(res)) {
                    res = getFromOthers(word);
                }
                if (StringUtils.isBlank(res)) {
                    res = word;
                }
                sb.append(res);
            }
            return sb.toString();
        } else {
            // 没有自定义单词，使用整句翻译，翻译更准确
            return getFromOthers(StringUtils.join(words, StringUtils.SPACE));
        }
    }

    /**
     * 自动翻译
     *
     * @param source 源
     * @return {@link String}
     */
    public String autoTranslate(String source) {
        TranslateExecutor translator = translatorMap.get(config.getTranslator());
        if (Objects.isNull(translator)) {
            return StringUtils.EMPTY;
        }
        return translator.translate(source);
    }

    /**
     * 中译英
     *
     * @param source 源中文
     * @return {@link String}
     */
    public String translateCh2En(String source) {
        if (StringUtils.isBlank(source)) {
            return "";
        }
        String ch2En = ydCh2EnTranslator.translate(source);
        String[] chs = StringUtils.split(ch2En);
        List<String> chList = chs == null ? Lists.newArrayList() : Lists.newArrayList(chs);
        chList = chList.stream()
                .filter(c -> !Consts.STOP_WORDS.contains(c.toLowerCase()))
                .map(str -> str.replaceAll("[,.'\\-+;:`~]+", ""))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(chList)) {
            return "";
        }
        if (chList.size() == 1) {
            return chList.get(0);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chList.size(); i++) {
            if (StringUtils.isBlank(chList.get(i))) {
                continue;
            }
            if (Consts.STOP_WORDS.contains(chList.get(i).toLowerCase())) {
                continue;
            }
            if (i == 0) {
                sb.append(chList.get(i).toLowerCase());
            } else {
                String lowCh = chList.get(i).toLowerCase();
                sb.append(StringUtils.substring(lowCh, 0, 1).toUpperCase()).append(StringUtils.substring(lowCh, 1));
            }
        }
        return sb.toString();
    }

    private List<String> split(String word) {
        word = word.replaceAll("(?<=[^A-Z])[A-Z][^A-Z]", "_$0");
        word = word.replaceAll("[A-Z]{2,}", "_$0");
        word = word.replaceAll("_+", "_");
        return Arrays.stream(word.split("_")).map(String::toLowerCase).collect(Collectors.toList());
    }

    /**
     * 是否有自定义单词
     *
     * @param words 单词
     * @return boolean
     */
    private boolean hasCustomWord(List<String> words) {
        return CollectionUtil.containsAny(config.getWordMap().keySet(), words);
    }

    private String getFromCustom(String word) {
        return config.getWordMap().get(word.toLowerCase());
    }

    private String getFromOthers(String word) {
        TranslateExecutor translator = translatorMap.get(config.getTranslator());
        if (Objects.isNull(translator)) {
            return StringUtils.EMPTY;
        }
        return translator.translate(word);
    }

}
