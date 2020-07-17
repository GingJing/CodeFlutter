package com.github.gingjing.plugin.generator.doc.view.inner;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @date 2019/08/25
 */
public class WordMapAddView extends DialogWrapper {

    private JPanel panel;
    private JTextField sourceTextField;
    private JTextField targetTextField;
    private JLabel source;
    private JLabel target;

    public WordMapAddView() {
        super(false);
        init();
        setTitle("添加单词映射");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (sourceTextField.getText() == null || sourceTextField.getText().length() <= 0) {
            return new ValidationInfo("请输入原单词", sourceTextField);
        }
        if (targetTextField.getText() == null || targetTextField.getText().length() <= 0) {
            return new ValidationInfo("请输入转换后的单词", targetTextField);
        }
        return super.doValidate();
    }

    public Map.Entry<String, String> getMapping() {
        return new SimpleEntry<>(sourceTextField.getText().toLowerCase(), targetTextField.getText());
    }
}
