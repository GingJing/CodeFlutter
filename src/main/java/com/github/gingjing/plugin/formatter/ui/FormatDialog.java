package com.github.gingjing.plugin.formatter.ui;

import com.alibaba.druid.util.JdbcConstants;
import com.github.gingjing.plugin.common.utils.PluginJsonUtil;
import com.github.gingjing.plugin.common.utils.PluginSqlUtil;
import com.github.gingjing.plugin.common.utils.PluginStringUtil;
import com.github.gingjing.plugin.formatter.enums.FormatEnum;
import com.github.gingjing.plugin.formatter.sqlformat.DefaultSqlFormatHandlerChain;
import com.github.gingjing.plugin.formatter.sqlformat.SqlFormatter;
import com.github.gingjing.plugin.formatter.util.HtmlUtil;
import com.github.gingjing.plugin.formatter.util.NoticeUtil;
import com.github.gingjing.plugin.formatter.util.XmlUtil;
import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.tool.StringUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class FormatDialog extends JDialog {

    private static final Logger LOG = Logger.getInstance(FormatDialog.class);

    /** 主面板 */
    private JPanel contentPane;

    /** 待格式化面板 */
    private JPanel centerPanel;

    /** 格式化类型 */
    private JComboBox<FormatEnum> typeCbx;

    /** 文本面板 */
    private JPanel textPanel;

    /** 格式化按钮 */
    private JButton formatBtn;

    /** 取消按钮 */
    private JButton cancelButton;

    /** 数据库类型标签 */
    private JLabel dbLabel;

    /** 数据库类型选择框 */
    private JComboBox<String> dbCbx;

    /** Mybatis log sql参数标签 */
    private JLabel sqlParamLb;

    /** sql参数文本域 */
    private JTextField sqlParamTf;

    /** mybatis log sql确认框 */
    private JCheckBox mybatisLogCx;

    /** 需格式化内容 */
    private JTextPane toFormatTp;

    /** 高亮显示按钮 */
    private JButton hlBtn;

    /** 当前项目 */
    private Project project;

    /** 格式化字符串 */
    private String formatStr = "";

    /** 当前选择的格式化类型 */
    private FormatEnum selectedType = FormatEnum.JSON;

    /** 格式化后展示面板关闭标记 */
    private boolean closeFlag;

    /** 数据库类型 */
    private String dbType;

    public FormatDialog(Editor editor, FormatEnum formatEnum) {
        this.project = editor.getProject();
        this.formatStr = StringUtils.isEmpty(editor.getSelectionModel().getSelectedText()) ? "" : editor.getSelectionModel().getSelectedText();
        if (formatEnum != null) {
            this.selectedType = formatEnum;
        }
        initDbList();
        initTextPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(formatBtn);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addListener();
    }

    private void initTextPanel(){
        setSqlComponentVisible();
        this.textPanel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        this.centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.typeCbx.setModel(new DefaultComboBoxModel<>(FormatEnum.values()));
        this.typeCbx.setSelectedItem(selectedType);
        this.toFormatTp.setText(formatStr);
        this.mybatisLogCx.setSelected(false);
    }

    private void initDbList() {
        dbCbx.addItem(JdbcConstants.MYSQL);
        dbCbx.addItem(JdbcConstants.HIVE);
        dbCbx.addItem(JdbcConstants.ORACLE);
        dbCbx.addItem(JdbcConstants.HBASE);
        dbCbx.addItem(JdbcConstants.SQL_SERVER);
        dbCbx.addItem(JdbcConstants.CLICKHOUSE);
        dbCbx.addItem(JdbcConstants.H2);
        dbCbx.addItem(JdbcConstants.HSQL);
        dbCbx.addItem(JdbcConstants.SQLITE);
        dbCbx.addItem(JdbcConstants.GBASE);
        dbCbx.addItem(JdbcConstants.DB2);
        dbCbx.addItem(JdbcConstants.ALI_ORACLE);
        dbCbx.addItem(JdbcConstants.OCEANBASE);
        dbCbx.addItem(JdbcConstants.KINGBASE);
        dbCbx.addItem(JdbcConstants.SYBASE);
        dbCbx.addItem(JdbcConstants.DERBY);
        dbCbx.addItem(JdbcConstants.DM);
        dbCbx.addItem(JdbcConstants.JTDS);
        dbCbx.addItem(JdbcConstants.TERADATA);
        dbCbx.addItem(JdbcConstants.XUGU);
        dbCbx.addItem(JdbcConstants.MARIADB);
        dbCbx.addItem(JdbcConstants.MOCK);
        dbCbx.setSelectedIndex(0);
    }

    private void setSqlComponentVisible() {
        this.dbCbx.setVisible(selectedType.equals(FormatEnum.SQL));
        this.dbLabel.setVisible(selectedType.equals(FormatEnum.SQL));
        this.sqlParamLb.setVisible(selectedType.equals(FormatEnum.SQL));
        this.sqlParamTf.setVisible(selectedType.equals(FormatEnum.SQL));
        this.mybatisLogCx.setVisible(selectedType.equals(FormatEnum.SQL));
    }

    /**
     * create editor with specified language and content
     */
    private Editor currEditor() {
        if (StringUtils.isEmpty(this.toFormatTp.getText())) {
            formatStr = "";
        }
        return ((FormatEnum) Objects.requireNonNull(typeCbx.getSelectedItem())).createEditor(project, formatStr);
    }

    class TypeItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            selectedType = (FormatEnum) typeCbx.getSelectedItem();
            if (selectedType == null) {
                return;
            }
            if (selectedType.equals(FormatEnum.SQL)) {
                dbCbx.setVisible(true);
                dbLabel.setVisible(true);
                mybatisLogCx.setVisible(true);
            } else {
                setSqlComponentVisible();
            }
        }
    }
    public void addListener() {
        typeCbx.addItemListener(new TypeItemListener());

        dbCbx.addActionListener(e -> dbType = (String) dbCbx.getSelectedItem());

        mybatisLogCx.addActionListener(e -> {
            if (mybatisLogCx.isSelected()) {
                sqlParamLb.setVisible(true);
                sqlParamTf.setVisible(true);
            }
        });

        hlBtn.addActionListener(e -> onHighlighter());

        formatBtn.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    /** 高亮显示弹框 */
    private void highlightFormattedDialog() {
        // 构建dialog
        DialogBuilder dialogBuilder = new DialogBuilder(project);
        dialogBuilder.setTitle("CodeFlutter Format");
        // 格式化后的编辑器
        Editor hasFormattedEditor = currEditor();
        JComponent component = hasFormattedEditor.getComponent();
        component.setEnabled(true);
        component.setPreferredSize(new Dimension(800, 600));
        component.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        dialogBuilder.setCenterPanel(component);
        dialogBuilder.addCloseButton();
        dialogBuilder.addDisposable(() -> {
            //释放掉编辑框
            if (hasFormattedEditor != null) {
                EditorFactory.getInstance().releaseEditor(hasFormattedEditor);
            }
            dialogBuilder.dispose();
            closeFlag = true;
        });
        dialogBuilder.show();
    }

    /** 高亮 */
    private void onHighlighter() {
        if (StringUtils.isEmpty(this.toFormatTp.getText())) {
            NoticeUtil.error("请输入数据");
            return;
        }
        highlightFormattedDialog();
    }

    /** 格式化 */
    private void onOK() {
        if (StringUtils.isEmpty(this.toFormatTp.getText())) {
            NoticeUtil.error("请输入数据");
            return;
        }
        String formattedString = "";
        if (FormatEnum.JSON.equals(selectedType)) {
            formattedString = formatJson(this.toFormatTp.getText());
            formatStr = formattedString;
        } else if (FormatEnum.XML.equals(selectedType)) {
            formattedString = formatXml(this.toFormatTp.getText());
            formatStr = formattedString;
        } else if (FormatEnum.SQL.equals(selectedType)) {
            formattedString = formatSql(this.toFormatTp.getText(), this.sqlParamTf.getText());
            formatStr = formattedString;
        } else if (FormatEnum.HTML.equals(selectedType)) {
            formattedString = formatHtml(this.toFormatTp.getText());
            formatStr = formattedString;
        } else {
            NoticeUtil.error("正在开发中...");
        }
        if ("".equals(formattedString)) {
            return;
        }
        this.toFormatTp.setText(formatStr);

        if (!closeFlag) {
            NoticeUtil.error("请先关闭显示格式化后内容的对话框！");
        }
    }

    private void onCancel() {
        dispose();
    }

    /**
     * 格式化json
     *
     * @param text 需要格式化的json文本
     * @return 格式化后的字符串
     */
    private String formatJson(String text) {
        String str = "";
        try {
            str = PluginJsonUtil.formatByGson(text);
        } catch (Exception e) {
            LOG.info(NoticeUtil.getStackTrace(e));
            NoticeUtil.error("JSON格式有误");
        }
        return str;
    }

    /**
     * 格式化xml
     *
     * @param text 需要格式化的text文本
     * @return 格式化后的字符串
     */
    private String formatXml(String text) {
        String str = "";
        try {
            str = XmlUtil.format(text);
        } catch (Exception e) {
            LOG.info(NoticeUtil.getStackTrace(e));
            NoticeUtil.error("Xml格式有误");
        }
        return str;
    }

    /**
     * 格式化json
     *
     * @param text 需要格式化的json文本
     * @return 格式化后的字符串
     */
    private String formatSql(String text, String params) {
        String str = "";
        try {
            if (this.mybatisLogCx.isSelected()) {
                if (PluginStringUtil.isBlank(params)) {
                    DefaultSqlFormatHandlerChain chain = new DefaultSqlFormatHandlerChain(text, "?", dbType);
                    SqlFormatter sqlFormatter = chain.doProcess();
                    sqlParamTf.setText(sqlFormatter.getSqlParamStr());
                    str = sqlFormatter.getSqlEntity().getRealSql();
                }
                str = PluginSqlUtil.format(text, dbType, PluginSqlUtil.parser(params));
            } else {
                str = PluginSqlUtil.format(text, dbType);
            }
        } catch (Exception e) {
            LOG.info(NoticeUtil.getStackTrace(e));
            NoticeUtil.error("Sql格式有误");
        }
        return str;
    }

    /**
     * 格式化html
     *
     * @param html 需要格式化的html文本
     * @return 格式化后文本
     */
    private String formatHtml(String html) {
        String str = "";
        try {
            str = HtmlUtil.format(html);
        } catch (Exception e) {
            LOG.info(NoticeUtil.getStackTrace(e));
            NoticeUtil.error("Html格式有误");
        }
        return str;
    }

    public void open() {
        this.setSize(880, 515);
        this.setTitle("CodeFlutter Format");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {

        /*Formatssss dialog = new Formatssss(null);
        dialog.setSize(870, 515);
        dialog.setTitle("CoderFlutter JsonFormat");
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);*/
    }
}
