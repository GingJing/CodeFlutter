package com.github.gingjing.plugin.translate.actions;

import com.github.gingjing.plugin.formatter.util.StringUtil;
import com.github.gingjing.plugin.translate.constants.TranslateConstant;
import com.github.gingjing.plugin.translate.response.GoogleResponse;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;

/**
 * 谷歌翻译并选择替换内容菜单
 *
 * @author: D、deng
 * @modify by gingjingdm
 * @date: 2020年 06月27日 20时02分
 * @version: 1.0
 */
public class GoogleReplaceAndShowAction extends AbstractTranslateAndShowAction {

    public static final String SPACE = " ";

    @Override
    protected void showPopupBalloon(GoogleResponse result, String translateType) {
        ApplicationManager.getApplication().invokeLater((Runnable) new Runnable() {
            @Override
            public void run() {
                final JBPopupFactory factory = JBPopupFactory.getInstance();
                LinkedHashSet<String> set = new LinkedHashSet<>();
                StringBuilder text = new StringBuilder();
                boolean addFlag = true;
                if (result.getDict() == null) {
                    if (result.getSentences().size() == 1 || result.getSentences().size() == 2) {
                        text = new StringBuilder(result.getSentences().get(0).getTrans());
                    } else {
                        addFlag = false;
                        for (GoogleResponse.SentencesBean sentence : result.getSentences()) {
                            if (sentence.getTrans() != null) {
                                text.append(sentence.getTrans());
                            }
                        }
                    }
                    set.add(text.toString());
                } else {
                    for (GoogleResponse.DictBean dictBean : result.getDict()) {
                        for (GoogleResponse.DictBean.EntryBean entryBean : dictBean.getEntry()) {
                            text = new StringBuilder(entryBean.getWord());
                            set.add(text.toString());
                        }
                    }
                }
                if (addFlag) {
                    LinkedHashSet<String> candidateWords = new LinkedHashSet<>();
                    // 填充结果
                    for (String s : set) {
                        addWord(candidateWords, s.trim(), translateType);
                    }
                    set.addAll(candidateWords);
                }
                JBList<Object> jList = new JBList<>(set.toArray());
                JBScrollPane scrollPane = new JBScrollPane(jList);
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(scrollPane, BorderLayout.CENTER);
                factory.createComponentPopupBuilder(panel, jList).createPopup().show(factory.guessBestPopupLocation(editor));
                jList.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            System.out.println(jList.getSelectedValue());
                            final SelectionModel selectionModel = editor.getSelectionModel();
                            replaceStr(project, editor, selectionModel, jList.getSelectedValue().toString());
                        }
                    }
                });
            }
        });
    }

    /**
     * 将可能需要的单词添加到候选list中
     *
     * @param set
     * @param text
     * @param translateType
     */
    private void addWord(LinkedHashSet<String> set, String text, String translateType) {
        if (TranslateConstant.ZH_CN_TO_EN.equals(translateType) && text.contains(SPACE)) {
            set.addAll(StringUtil.getAllTranslateCase(text));
        }
    }


    /**
     * 替换选中的字符串
     *
     * @param editor
     * @param selectionModel
     * @param project
     * @param newText
     */
    public static void replaceStr(Project project, Editor editor, SelectionModel selectionModel, String newText) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                editor.getDocument().replaceString(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd(), newText);
            }
        };
        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();
    }
}
