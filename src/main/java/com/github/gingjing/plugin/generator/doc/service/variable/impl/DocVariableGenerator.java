package com.github.gingjing.plugin.generator.doc.service.variable.impl;

import com.github.gingjing.plugin.generator.doc.service.TranslatorService;
import com.github.gingjing.plugin.generator.doc.service.variable.VariableGenerator;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;


/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-12-07 23:16:00
 */
public class DocVariableGenerator implements VariableGenerator {
    private TranslatorService translatorService = ServiceManager.getService(TranslatorService.class);

    @Override
    public String generate(PsiElement element) {
        if (element instanceof PsiNamedElement) {
            return translatorService.translate(((PsiNamedElement) element).getName());
        }
        return "";
    }
}