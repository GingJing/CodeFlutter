package com.github.gingjing.plugin.generator.doc.view;

import com.github.gingjing.plugin.generator.doc.config.GenJavadocConfigComponent;
import com.github.gingjing.plugin.generator.doc.model.GenJavadocConfiguration;
import com.github.gingjing.plugin.generator.doc.view.template.ClassConfigurable;
import com.github.gingjing.plugin.generator.doc.view.template.FieldConfigurable;
import com.github.gingjing.plugin.generator.doc.view.template.MethodConfigurable;
import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @modify gingjingdm
 * @date 2019/08/25
 */
public class GenJavadocConfigurableComposite implements Configurable, Configurable.Composite {

    private GenJavadocConfiguration config = ServiceManager.getService(GenJavadocConfigComponent.class).getState();
    private GenJavadocConfigureView view = new GenJavadocConfigureView();
    private static final Set<String> ENABLE_TRANSLATOR_SET = ImmutableSet.of("有道翻译", "关闭（只使用自定义翻译）");


    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Make Javadoc";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return view.getComponent();
    }

    @Override
    public boolean isModified() {
        if (!Objects.equals(config.getAuthor(), view.getAuthorTextField().getText())) {
            return true;
        }
        if (!Objects.equals(config.getDateFormat(), view.getDateFormatTextField().getText())) {
            return true;
        }
        if (!Objects.equals(config.getSimpleFieldDoc(), view.getSimpleDocButton().isSelected())) {
            return true;
        }
        if (!Objects.equals(config.getTranslator(), view.getTranslatorBox().getSelectedItem())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        config.setAuthor(view.getAuthorTextField().getText());
        config.setDateFormat(view.getDateFormatTextField().getText());
        config.setSimpleFieldDoc(view.getSimpleDocButton().isSelected());
        config.setTranslator(String.valueOf(view.getTranslatorBox().getSelectedItem()));
        if (config.getWordMap() == null) {
            config.setWordMap(new TreeMap<>());
        }

        if (config.getAuthor() == null) {
            throw new ConfigurationException("作者不能为null");
        }
        if (config.getDateFormat() == null) {
            throw new ConfigurationException("日期格式不能为null");
        }
        if (config.getSimpleFieldDoc() == null) {
            throw new ConfigurationException("注释形式不能为null");
        }
        if (config.getTranslator() == null || !ENABLE_TRANSLATOR_SET.contains(config.getTranslator())) {
            throw new ConfigurationException("请选择正确的翻译方式");
        }
    }

    @Override
    public void reset() {
        view.refresh();
    }

    @NotNull
    @Override
    public Configurable[] getConfigurables() {
        return new Configurable[]{new ClassConfigurable(), new FieldConfigurable(), new MethodConfigurable()};
    }
}
