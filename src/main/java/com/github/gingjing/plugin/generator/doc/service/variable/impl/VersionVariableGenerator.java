package com.github.gingjing.plugin.generator.doc.service.variable.impl;

import com.github.gingjing.plugin.generator.doc.service.variable.VariableGenerator;
import com.intellij.psi.PsiElement;


/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @version 1.0.0
 * @since 2019-12-07 23:17:00
 */
public class VersionVariableGenerator implements VariableGenerator {

    @Override
    public String generate(PsiElement element) {
        return "1.0.0";
    }
}
