package com.github.gingjing.plugin.generator.doc.service.variable.impl;

import com.github.gingjing.plugin.generator.doc.config.GenJavadocConfigComponent;
import com.github.gingjing.plugin.generator.doc.model.GenJavadocConfiguration;
import com.github.gingjing.plugin.generator.doc.service.variable.VariableGenerator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;


/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-12-07 23:16:00
 */
public class AuthorVariableGenerator implements VariableGenerator {
    private GenJavadocConfiguration config = ServiceManager.getService(GenJavadocConfigComponent.class).getState();

    @Override
    public String generate(PsiElement element) {
        return config.getAuthor();
    }
}