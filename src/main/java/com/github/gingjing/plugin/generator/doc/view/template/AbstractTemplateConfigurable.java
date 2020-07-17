package com.github.gingjing.plugin.generator.doc.view.template;

import com.github.gingjing.plugin.generator.doc.config.GenJavadocConfigComponent;
import com.github.gingjing.plugin.generator.doc.model.GenJavadocConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-11-10 18:14:00
 */
public abstract class AbstractTemplateConfigurable implements Configurable {

    protected GenJavadocConfiguration config = ServiceManager.getService(GenJavadocConfigComponent.class).getState();

    @Nullable
    @Override
    public JComponent createComponent() {
        return getView().getComponent();
    }

    public abstract AbstractTemplateConfigView getView();
}
