package com.github.gingjing.plugin.generator.doc.service;

import com.github.gingjing.plugin.generator.doc.service.generator.DocGenerator;
import com.github.gingjing.plugin.generator.doc.service.generator.impl.ClassDocGenerator;
import com.github.gingjing.plugin.generator.doc.service.generator.impl.FieldDocGenerator;
import com.github.gingjing.plugin.generator.doc.service.generator.impl.MethodDocGenerator;
import com.google.common.collect.ImmutableMap;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * @author <a href="mailto:wangchao.star@gmail.com">wangchao</a>
 * @date 2019/08/25
 */
public class DocGeneratorService {

    private Map<Class<? extends PsiElement>, DocGenerator> docGeneratorMap
        = ImmutableMap.<Class<? extends PsiElement>, DocGenerator>builder()
        .put(PsiClass.class, new ClassDocGenerator())
        .put(PsiMethod.class, new MethodDocGenerator())
        .put(PsiField.class, new FieldDocGenerator())
        .build();

    public String generate(PsiElement psiElement) {
        DocGenerator docGenerator = null;
        for (Entry<Class<? extends PsiElement>, DocGenerator> entry : docGeneratorMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(psiElement.getClass())) {
                docGenerator = entry.getValue();
                break;
            }
        }
        if (Objects.isNull(docGenerator)) {
            return StringUtils.EMPTY;
        }
        return docGenerator.generate(psiElement);
    }
}
