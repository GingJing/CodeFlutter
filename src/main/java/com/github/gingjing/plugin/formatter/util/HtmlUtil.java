package com.github.gingjing.plugin.formatter.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Html工具类
 *
 * @author: gingjingdm
 * @date: 2020年 07月02日 23时08分
 * @version: 1.0
 */
public class HtmlUtil {

    /**
     * 格式化html字符串
     *
     * @param html 需要格式化的html字符串
     * @return 格式化后的html字符串
     */
    public static String format(String html) {
        Document doc = Jsoup.parseBodyFragment(html);
        return doc.body().html();
    }
}
