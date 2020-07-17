package com.github.gingjing.plugin.translate.ui;

import com.github.gingjing.plugin.formatter.ui.modules.ColorService;
import com.github.gingjing.plugin.formatter.ui.modules.IdeaColorService;
import com.github.gingjing.plugin.translate.response.GoogleResponse;

import javax.swing.*;
import java.awt.*;

/**
 * Google翻译气泡ui
 *
 * @author: <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @modify  gingjingdm
 * @date: 2020年 06月28日 21时09分
 * @version: 1.0
 */
public class GoogleTranslateBalloon extends AbstractMyTranslateBalloon {

    static {
        ColorService.install(new IdeaColorService());
    }


    public GoogleTranslateBalloon(GoogleResponse result) {
        super(result);
    }

    @Override
    protected void init() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        StringBuilder origBuilder = new StringBuilder("<body style='font-size:14px;font-weight: bold;color: #CAA923;'>");
        StringBuilder transBuilder = new StringBuilder("<body style='font-size:14px;font-weight: bold;color: #CAA923;'>");
        if (result.getSentences().size() == 1 || result.getSentences().size() == 2) {
            GoogleResponse.SentencesBean sentencesBean = result.getSentences().get(0);
            origBuilder.append(result.getSentences().get(0).getOrig());
            transBuilder.append(result.getSentences().get(0).getTrans()).append("<br>");
            if (!sentencesBean.getOrig().equals(sentencesBean.getTrans()) && result.getDict() != null) {
                for (int i = 0, len = result.getDict().size(); i < len; i++) {
                    transBuilder.append(result.getDict().get(i));
                }
            }
        } else {
            for (GoogleResponse.SentencesBean sentence : result.getSentences()) {
                if (sentence.getTrans() != null) {
                    origBuilder.append(sentence.getOrig());
                    transBuilder.append(sentence.getTrans());
                }
            }
        }
        origBuilder.append("</body>");
        transBuilder.append("</body>");
        origLength = origBuilder.length();
        transLength = transBuilder.length();
        createPanel("原文：", origBuilder, BorderLayout.NORTH, origEditorPane);
        createPanel("翻译结果：", transBuilder, BorderLayout.CENTER, transEditorPane);
    }


//    public static void main(String[] args) {
//        TranslateBalloon translateBalloon = new TranslateBalloon(null);
////        TranslateBalloon translateBalloon = new TranslateBalloon();
//        JFrame jFrame = new JFrame();
//        jFrame.add(translateBalloon.jPanel);
//        jFrame.setSize(520, 400);
//        jFrame.setVisible(true);
//    }
}
