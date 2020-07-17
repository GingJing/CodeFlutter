package com.github.gingjing.plugin.gsonformat.process;

import com.github.gingjing.plugin.gsonformat.entity.ClassEntity;
import com.intellij.psi.*;


import java.util.regex.Pattern;

/**
 * Created by dim on 16/11/7.
 */
class JackProcessor extends BaseProcessor {

    private static Pattern pattern = Pattern.compile("@.*?JsonIgnoreProperties");

    @Override
    protected void onStartGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, IProcessor visitor) {
        super.onStartGenerateClass(factory, classEntity, parentClass, visitor);
    }

    @Override
    public void onStarProcess(ClassEntity classEntity, PsiElementFactory factory, PsiClass cls, IProcessor visitor) {
        super.onStarProcess(classEntity, factory, cls, visitor);
        injectAnnotation(factory, cls);
    }

    @Override
    protected void onEndGenerateClass(PsiElementFactory factory, ClassEntity classEntity, PsiClass parentClass, PsiClass generateClass, IProcessor visitor) {
        super.onEndGenerateClass(factory, classEntity, parentClass, generateClass, visitor);
        injectAnnotation(factory, generateClass);
    }

    private void injectAnnotation(PsiElementFactory factory, PsiClass generateClass) {
        if (factory == null || generateClass == null) {
            return;
        }
        PsiModifierList modifierList = generateClass.getModifierList();
        PsiElement firstChild = modifierList.getFirstChild();
        if (firstChild != null && !pattern.matcher(firstChild.getText()).find()) {
            PsiAnnotation annotationFromText =
                    factory.createAnnotationFromText("@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)", generateClass);
            modifierList.addBefore(annotationFromText, firstChild);
        }
    }
}
