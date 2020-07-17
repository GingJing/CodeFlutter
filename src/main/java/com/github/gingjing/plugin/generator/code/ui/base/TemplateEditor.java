package com.github.gingjing.plugin.generator.code.ui.base;

import com.github.gingjing.plugin.generator.code.tool.DialogUtils;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.BrowserHyperlinkListener;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SeparatorFactory;
import com.intellij.util.LocalTimeCounter;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 模板编辑
 *
 * @author makejava
 * @modify gingjingdm
 * @version 1.0.0
 * @since 2018/08/11 10:20
 */
public class TemplateEditor {
    /**
     * EASY CODE 模板
     */
    public static final String EASY_CODE_TEMPLATE = "EasyCodeTemplate.vm.ft";
    /**
     * 项目对象
     */
    private Project project;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 文件类型
     */
    private FileType fileType;

    /**
     * 编辑器对象
     */
    private Editor editor;

    /**
     * 回调结构
     */
    private Callback callback;

    /**
     * 构造方法
     *
     * @param project     项目对象
     * @param name        模板名称
     * @param content     模板内容
     * @param description 描述内容
     * @param fileType    文件类型
     */
    public TemplateEditor(@NotNull Project project, String name, String content, String description, @NotNull FileType fileType) {
        this.project = project;
        this.name = name;
        this.content = content;
        this.description = description;
        this.fileType = fileType;
    }

    /**
     * 创建面板组件
     *
     * @return 面板组件
     */
    public JComponent createComponent() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        if (editor != null) {
            editorFactory.releaseEditor(editor);
        }

        LightVirtualFile virtualFile = new LightVirtualFile(EASY_CODE_TEMPLATE, fileType, content, LocalTimeCounter.currentTime());
        // 标识为模板，让velocity跳过语法校验
        virtualFile.putUserData(FileTemplateManager.DEFAULT_TEMPLATE_PROPERTIES, FileTemplateManager.getInstance(project).getDefaultProperties());

        // 创建文档对象
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        assert document != null;
        // 创建编辑框
        editor = editorFactory.createEditor(document, project);

        // 添加修改事件
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(DocumentEvent event) {

            }

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                String text = editor.getDocument().getText();
                // 回调事件
                if (callback != null && !Objects.equals(text, content)) {
                    callback.call();
                }
            }
        });

        DialogUtils.setEditorSettings(editor);

        ((EditorEx) editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project, new LightVirtualFile(name + ".ft")));

        // 描述信息
        JEditorPane editorPane = new JEditorPane();
        // html形式展示
        editorPane.setEditorKit(UIUtil.getHTMLEditorKit());
        // 仅查看
        editorPane.setEditable(false);
        editorPane.setText(description);
        editorPane.addHyperlinkListener(new BrowserHyperlinkListener());

        // 描述面板
        JPanel descriptionPanel = new JPanel(new GridBagLayout());
        descriptionPanel.add(SeparatorFactory.createSeparator(IdeBundle.message("label.description"), null),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        JBUI.insetsBottom(2), 0, 0));
        descriptionPanel.add(ScrollPaneFactory.createScrollPane(editorPane),
                new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        JBUI.insetsTop(2), 0, 0));

        // 包装编辑器
        JPanel panel = new JPanel(new BorderLayout());

        // 分割器
        Splitter splitter = new Splitter(true, 0.6F);
        splitter.setFirstComponent(editor.getComponent());
        splitter.setSecondComponent(descriptionPanel);

        panel.add(splitter, BorderLayout.CENTER);
        panel.setPreferredSize(JBUI.size(400, 300));
        return panel;
    }

    /**
     * 重置
     *
     * @param name    模板名称
     * @param content 文本内容
     */
    public void reset(String name, String content) {
        if (content != null && this.editor != null) {
            // 重置语法高亮
            ((EditorEx) editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project, new LightVirtualFile(name + ".ft")));
            // 重置文本内容
            WriteCommandAction.runWriteCommandAction(project, () -> this.editor.getDocument().setText(content));
        }
    }

    /**
     * 关闭回调方法
     */
    public void onClose() {
        if (editor != null) {
            EditorFactory.getInstance().releaseEditor(editor);
        }
        editor = null;
    }

    public static String getEasyCodeTemplate() {
        return EASY_CODE_TEMPLATE;
    }

    public Project getProject() {
        return project;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public FileType getFileType() {
        return fileType;
    }

    public Editor getEditor() {
        return editor;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * 回调接口
     */
    public interface Callback {
        /**
         * 文档修改回调
         */
        void call();
    }
}
