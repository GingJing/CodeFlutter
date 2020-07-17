package com.github.gingjing.plugin.generator.doc.service.variable;

import com.intellij.psi.PsiElement;

/**
 * 变量生成器
 *
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @date 2019/12/07
 */
public interface VariableGenerator {
    /**
     * 生成
     *
     * @param element 元素
     * @return {@link String}
     */
    String generate(PsiElement element);
}
