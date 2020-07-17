package com.github.gingjing.plugin.formatter.enums;

import com.github.gingjing.plugin.common.constants.PluginConstants;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.Nullable;

/**
 * 格式化枚举
 *
 * @author: gingjingdm
 * @date: 2020年 06月27日 19时59分
 * @version: 1.0
 */
public enum FormatEnum {

    /** sql */
    SQL("sql") {
        @Override
        public Editor createEditor(Project project, String str) {
            return doGetEditor(project, str);
        }
    },

    /** json */
    JSON("json") {
        @Override
        public Editor createEditor(Project project, String str) {
            return doGetEditor(project, str);
        }
    },

    /** xml */
    XML("xml") {
        @Override
        public Editor createEditor(Project project, String str) {
            return doGetEditor(project, str);
        }
    },

    HTML("html") {
        @Override
        public Editor createEditor(Project project, String str) {
            return doGetEditor(project, str);
        }
    };

    FormatEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public abstract Editor createEditor(Project project, String str);

    @Nullable
    protected Editor doGetEditor(Project project, String str) {
        EditorFactory editorFactory = EditorFactory.getInstance();
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension(getValue());
        PsiFile psiFile = psiFileFactory.createFileFromText(PluginConstants.PLUGIN_FORMAT + getValue() + ".ft", fileType, str, 0, true);
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        return editorFactory.createEditor(document, project, fileType, true);
    }

    public static FormatEnum getByValue(String value) {
        for (FormatEnum formatEnum : FormatEnum.values()) {
            if (formatEnum.getValue().equals(value)) {
                return formatEnum;
            }
        }
        return null;
    }
}
