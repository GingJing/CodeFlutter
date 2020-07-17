package com.github.gingjing.plugin.generator.code.ui;


import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.entity.CreateModeEnum;
import com.github.gingjing.plugin.generator.code.entity.TableInfo;
import com.github.gingjing.plugin.generator.code.entity.Template;
import com.github.gingjing.plugin.generator.code.service.CodeGenerateService;
import com.github.gingjing.plugin.generator.code.service.TableInfoService;
import com.github.gingjing.plugin.generator.code.tool.CacheDataUtils;
import com.github.gingjing.plugin.generator.code.tool.CollectionUtil;
import com.github.gingjing.plugin.generator.code.tool.DialogUtils;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.github.gingjing.plugin.generator.code.ui.base.TemplateEditor;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.psi.DbTable;
import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.util.TreeFileChooser;
import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.util.Conditions;
import com.intellij.psi.*;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 模板调试展示面板
 *
 * @date: 2020/7/3 12:22
 * @author: GingJingDM
 * @version: 1.0
 */
public class TemplateDebugPanel {

    private static final String[] DEBUG_WAYS = new String[]{"json", "sql"};

    private Project project;

    private TemplateEditor templateEditor;

    private JPanel mainPanel;

    private TableInfo tableInfo;

    public TemplateDebugPanel(Project project, TemplateEditor templateEditor) {
        this.project = project;
        this.templateEditor = templateEditor;
        init();
    }

    private void init() {
        // 主面板
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 创建调试模式选择下拉框
        panel.add(new JLabel("选择实时调试方式"));
        ComboBoxModel<CreateModeEnum> modeCbxModel =
                new CollectionComboBoxModel<>(Arrays.asList(CreateModeEnum.DATABASE_TOOL, CreateModeEnum.SELECT_MODEL));
        ComboBox<CreateModeEnum> modeCbx = new ComboBox<>(modeCbxModel);
        modeCbx.setSelectedIndex(0);
        CacheDataUtils.getInstance().setCreateMode(CreateModeEnum.DATABASE_TOOL);
        // 添加调式模式选择下拉框
        panel.add(modeCbx);

        // 创建数据库表名列表下拉框
        List<String> tableList = new ArrayList<>();
        List<DbDataSource> dataSourceList = DbPsiFacade.getInstance(project).getDataSources();
        ComboBoxModel<String> tableNameCbxModel = new CollectionComboBoxModel<>(tableList);
        ComboBox<String> tableNameCbx = new ComboBox<>(tableNameCbxModel);
        if (!CollectionUtil.isEmpty(dataSourceList)) {
            dataSourceList.forEach(dbDataSource -> getTables(dbDataSource).forEach(table -> tableList.add(table.toString())));
        }
        tableNameCbx.setSelectedIndex(0);

        // 初始化调试动作组按钮
        DbToolDebuggerAction dbToolDebuggerAction = new DbToolDebuggerAction(tableNameCbx, AllIcons.Actions.Run_anything);
        SelectModelDebuggerAction selectModelDebuggerAction = new SelectModelDebuggerAction(tableList, AllIcons.Actions.Run_anything);
        DefaultActionGroup actionGroup = new DefaultActionGroup(dbToolDebuggerAction);


        modeCbx.addItemListener(e -> {
            CacheDataUtils.getInstance().setCreateMode((CreateModeEnum) modeCbx.getSelectedItem());
            tableNameCbx.setEnabled(Objects.equals(modeCbx.getSelectedItem(), CreateModeEnum.DATABASE_TOOL));
            if (CreateModeEnum.DATABASE_TOOL.equals(modeCbx.getSelectedItem())) {
                actionGroup.removeAll();
                actionGroup.add(dbToolDebuggerAction);
            } else {
                actionGroup.removeAll();
                actionGroup.add(selectModelDebuggerAction);
            }
        });

        // 添加数据库表名列表下拉框
        panel.add(tableNameCbx);

        JLabel livingDebugger = new JLabel("实时调试");
        panel.add(livingDebugger);
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("Template Debug", actionGroup, true);
        panel.add(actionToolbar.getComponent());
        this.mainPanel = panel;
    }

    private JBIterable<DasTable> getTables(DbDataSource dataSource) {
        return dataSource.getModel().traverser().expandAndSkip(Conditions.instanceOf(DasNamespace.class)).filter(DasTable.class);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TemplateEditor getTemplateEditor() {
        return templateEditor;
    }

    public void setTemplateEditor(TemplateEditor templateEditor) {
        this.templateEditor = templateEditor;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }






    /**
     * 选择java实体类调试动作
     */
    private class SelectModelDebuggerAction extends AnAction {

        private final List<String> tableNames;

        public SelectModelDebuggerAction(List<String> tableNames, Icon icon) {
            super(icon);
            this.tableNames = tableNames;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
            CacheDataUtils.getInstance().setDebugging(true);
            PsiJavaFile selectedFile = DialogUtils.showAndGetChooseJavaFile(project);
            if (selectedFile != null) {
                PsiClass psiClass = selectedFile.getClasses()[0];
                DbTable dbTable = ProjectUtils.getDbTableByPsiClass(project, psiClass);
                if (dbTable != null) {
                    CacheDataUtils.getInstance().setCreateMode(CreateModeEnum.DATABASE_TOOL);
                    CacheDataUtils.getInstance().setSelectDbTable(dbTable);
                    tableInfo = TableInfoService.getInstance(project).getTableInfoAndConfig(dbTable);
                    showDebugDialog(tableInfo);
                    return;
                }
                CacheDataUtils.getInstance().setSelectClass(psiClass);
                CacheDataUtils.getInstance().setCreateMode(CreateModeEnum.SELECT_MODEL);
                if (!MessageDialogBuilder.yesNo(MsgValue.TEMPLATE_DEBUG_INFO,
                        "此类之前是否已使用此插件生成过代码？")
                        .isYes()) {
                    new SetTableInfoByModelOrSqlDialog(project).open();
                } else {
                    tableInfo = TableInfoService.getInstance(project).getTableInfoAndConfig(psiClass);
                }
                showDebugDialog(tableInfo);
            }
        }
    }

    /**
     * 选择IntelliJ Idea DatabaseTool调试
     */
    private class DbToolDebuggerAction extends AnAction {

        private final ComboBox<String> tableNameCbx;

        public DbToolDebuggerAction(ComboBox<String> tableNameCbx, Icon icon) {
            super(icon);
            this.tableNameCbx = tableNameCbx;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            CacheDataUtils.getInstance().setDebugging(true);
            // 获取选中的表
            String name = (String) tableNameCbx.getSelectedItem();
            DbTable dbTable = ProjectUtils.getDbTableByTableName(project, name);
            // 获取表信息
            tableInfo = TableInfoService.getInstance(project).getTableInfoAndConfig(dbTable);
            showDebugDialog(tableInfo);
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(tableNameCbx.getSelectedItem() != null);
        }
    }

    public void showDebugDialog(TableInfo tableInfo) {
        // 为未配置的表设置一个默认包名
        if (tableInfo.getSavePackageName() == null) {
            tableInfo.setSavePackageName("com.companyname.modulename");
        }
        // 生成代码
        String code = CodeGenerateService.getInstance(project).generate(new Template("temp", templateEditor.getEditor().getDocument().getText()), tableInfo);

        // 创建编辑框
        EditorFactory editorFactory = EditorFactory.getInstance();
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        String fileName = templateEditor.getName();
        FileType velocityFileType = FileTypeManager.getInstance().getFileTypeByExtension("vm");
        PsiFile psiFile = psiFileFactory.createFileFromText("EasyCodeTemplateDebug.vm.ft", velocityFileType, code, 0, true);
        // 标识为模板，让velocity跳过语法校验
        psiFile.getViewProvider().putUserData(FileTemplateManager.DEFAULT_TEMPLATE_PROPERTIES, FileTemplateManager.getInstance(project).getDefaultProperties());
        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);
        assert document != null;
        Editor editor = editorFactory.createEditor(document, project, velocityFileType, true);

        DialogUtils.setEditorSettings(editor);

        ((EditorEx) editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project, new LightVirtualFile(fileName)));

        // 构建dialog
        DialogBuilder dialogBuilder = new DialogBuilder(project);
        dialogBuilder.setTitle(MsgValue.TITLE_INFO);
        JComponent component = editor.getComponent();
        component.setPreferredSize(new Dimension(800, 600));
        dialogBuilder.setCenterPanel(component);
        DialogBuilder.CustomizableAction closeButton = dialogBuilder.addCloseButton();
        closeButton.setText("关闭");
        dialogBuilder.addDisposable(() -> {
            //释放掉编辑框
            editorFactory.releaseEditor(editor);
            dialogBuilder.dispose();
        });
        dialogBuilder.show();
        CacheDataUtils.getInstance().setDebugging(false);
    }



}
