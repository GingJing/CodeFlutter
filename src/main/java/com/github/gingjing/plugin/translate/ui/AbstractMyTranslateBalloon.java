package com.github.gingjing.plugin.translate.ui;

import com.github.gingjing.plugin.formatter.ui.modules.ColorService;
import com.github.gingjing.plugin.formatter.ui.modules.IdeaColorService;
import com.github.gingjing.plugin.translate.response.GoogleResponse;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * 翻译ui
 *
 * @author: <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @modify gingjingdm
 * @date: 2020年 06月28日 21时09分
 * @version: 1.0
 */
public abstract class AbstractMyTranslateBalloon {

    static {
        ColorService.install(new IdeaColorService());
    }

    protected GoogleResponse result;

    protected JPanel mainPanel;

    public static JEditorPane origEditorPane = new JEditorPane();

    public static JEditorPane transEditorPane = new JEditorPane();

    protected int origLength;

    protected int transLength;

    protected int height;

    protected int width;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public AbstractMyTranslateBalloon(GoogleResponse result) {
        this.result = result;
        init();
    }

    /**
     * 初始化
     */
    protected abstract void init();

    protected void createPanel(String text, StringBuilder builder, String position, JEditorPane editorPane) {
        System.out.println(builder.toString());
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text);
        label.setForeground(new Color(16, 187, 100));
        label.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        editorPane.setContentType("text/html");
        editorPane.setText(builder.toString());
        editorPane.setEditable(false);
        editorPane.setBackground(ColorService.forCurrentTheme(ColorService.Background));
        editorPane.setSelectionStart(0);
        editorPane.setSelectionEnd(0);
        editorPane.getSelectedText();
        JBScrollPane scrollPane = new JBScrollPane(editorPane);
        scrollPane.setBorder(null);
        scrollPane.setMaximumSize(new Dimension(520, 200));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);
        setPreferredSize(builder.length(), panel);
        mainPanel.add(panel, position);
    }

    private void setPreferredSize(int len, JPanel panel) {
        boolean flag = len * 16 > 540;
        int width = flag ? 520 : (len * 16 + 80);
        int height = flag ? Math.min(((len * 16 / 520 + 2) * 16), 200) : 32;
        this.height += height;
        this.width = Math.max(this.width, width);
        panel.setPreferredSize(new Dimension(width, height));
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
