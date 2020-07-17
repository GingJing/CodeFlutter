package com.github.gingjing.plugin.gsonformat.process;

import com.github.gingjing.plugin.gsonformat.entity.ClassEntity;
import com.github.gingjing.plugin.gsonformat.entity.ConvertLibrary;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;



/**
 * Created by dim on 16/11/7.
 */
public class ClassProcessor {

    private PsiElementFactory factory;
    private PsiClass cls;
    private BaseProcessor processor;

    public ClassProcessor(PsiElementFactory factory, PsiClass cls) {
        this.factory = factory;
        this.cls = cls;
        processor = BaseProcessor.getProcessor(ConvertLibrary.from());
    }

    public void generate(ClassEntity classEntity, IProcessor visitor) {
        if (processor != null) processor.process(classEntity, factory, cls, visitor);
    }
}
