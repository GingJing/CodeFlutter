package com.github.gingjing.plugin.generator.doc.service.variable.impl;

import com.github.gingjing.plugin.generator.doc.config.Consts;
import com.github.gingjing.plugin.generator.doc.service.variable.VariableGenerator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;


/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-12-07 23:18:00
 */
public class ReturnVariableGenerator implements VariableGenerator {

    @Override
    public String generate(PsiElement element) {
        if (!(element instanceof PsiMethod)) {
            return "";
        }
        PsiMethod psiMethod = (PsiMethod) element;
        String returnName = psiMethod.getReturnType() == null ? "" : psiMethod.getReturnType().getPresentableText();

        if (Consts.BASE_TYPE_SET.contains(returnName)) {
            return returnName;
        } else if ("void".equalsIgnoreCase(returnName)) {
            return "";
        } else {
            return String.format("{@link %s }", returnName);
        }
    }
}