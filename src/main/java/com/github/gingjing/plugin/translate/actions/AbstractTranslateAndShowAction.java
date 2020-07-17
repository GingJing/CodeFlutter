package com.github.gingjing.plugin.translate.actions;


import com.github.gingjing.plugin.formatter.util.NoticeUtil;
import com.github.gingjing.plugin.formatter.util.StringUtil;
import com.github.gingjing.plugin.translate.constants.TranslateConstant;
import com.github.gingjing.plugin.translate.factory.TranslateFactory;
import com.github.gingjing.plugin.translate.response.GoogleResponse;
import com.github.gingjing.plugin.translate.ui.AbstractMyTranslateBalloon;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 翻译并气泡展示抽象父类菜单
 *
 * @modify by gingjingdm
 * @date: 2020年 06月27日 19时02分
 * @version: 1.0
 */
public abstract class AbstractTranslateAndShowAction extends AnAction {

    public static final Pattern p = compile("[\u4e00-\u9fa5]");
    public static final int SUCCESS = 200;

    /** 当前编辑器 */
    public static Editor editor;
    /** 当前项目 */
    public static Project project;
    /** 当前选中的intelliJ model */
    public static SelectionModel selectionModel;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            String selectedText = getSelectedText();
            if (StringUtils.isEmpty(selectedText)) {
                NoticeUtil.error("请选择要翻译的字符");
                return;
            }
            NoticeUtil.init(this.getClass().getSimpleName(), 1);
            doTranslate(StringUtil.textToWords(selectedText));
        } catch (Exception e) {
            NoticeUtil.error(e);
        }
    }

    protected String getSelectedText() {
        String selectedText = "";
        if (editor == null){
            selectedText = AbstractMyTranslateBalloon.origEditorPane.getSelectedText();
            selectedText = StringUtils.isEmpty(selectedText)
                    ? AbstractMyTranslateBalloon.transEditorPane.getSelectedText()
                    : selectedText;
        }else{
            selectionModel = editor.getSelectionModel();
            selectedText = selectionModel.getSelectedText();
        }
        return selectedText;
    }

    @Override
    public void update(AnActionEvent e) {
        project = e.getData(CommonDataKeys.PROJECT);
        editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setVisible(project != null && editor != null
                && editor.getSelectionModel().hasSelection());
    }

    /**
     * 调用接口,翻译并返回值
     *
     * @param word             单词
     * @param translateType    类型
     * @param mEditor          编辑器
     * @return                 翻译结果
     * @throws Exception       翻译过程中发生异常（IO、系统错误等）
     */
    public static <T> T translate(Class<T> resultClazz, String word, String translateType, Editor mEditor) throws Exception {
        editor = mEditor;
        return TranslateFactory.getInstance().getResponse(resultClazz, word, translateType);
    }


    protected String getTranslateType(String selectText) {
        Matcher m = p.matcher(selectText.trim());
        return m.find() ? TranslateConstant.ZH_CN_TO_EN : TranslateConstant.EN_TO_ZH_CN;
    }

    /**
     * 执行翻译
     *
     * @param selectText 选取文本
     */
    protected void doTranslate(String selectText) {
        String translateType = getTranslateType(selectText);
        try {
            GoogleResponse googleResponse = translate(GoogleResponse.class, selectText, translateType, editor);
            if (googleResponse == null) {
                NoticeUtil.error("翻译错误,请重试!");
                return;
            }
            showPopupBalloon(googleResponse, translateType);
            NoticeUtil.info(translateType, googleResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文本弹出显示
     *
     * @param result           翻译后响应结果
     * @param translateType    翻译类型：中译英或英译中
     */
    protected abstract void showPopupBalloon(GoogleResponse result, String translateType);
}
