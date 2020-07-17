package com.github.gingjing.plugin.generator.code.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gingjing.plugin.generator.code.config.CodeFlutterGenCodeConfigComponent;
import com.github.gingjing.plugin.generator.code.constants.MsgValue;
import com.github.gingjing.plugin.generator.code.entity.AbstractGroup;
import com.github.gingjing.plugin.generator.code.tool.CollectionUtil;
import com.github.gingjing.plugin.generator.code.tool.ProjectUtils;
import com.github.gingjing.plugin.generator.code.ui.base.ListCheckboxPanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.UnnamedConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ExceptionUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 主设置面板
 *
 * @author makejava
 * @modify gingjingdm
 * @version 1.0.0
 * @since 2018/07/17 13:10
 */
public class GenCodeConfigurableComposite implements Configurable, Configurable.Composite {
    /**
     * 主面板
     */
    private JPanel mainPanel;
    /**
     * 作者编辑框
     */
    private JTextField authorTextField;
    /**
     * 重置默认设置按钮
     */
    private JButton resetBtn;
    /**
     * 当前版本号
     */
    private JLabel versionLabel;

    /**
     * 重置列表
     */
    private List<Configurable> resetList;

    /**
     * 需要保存的列表
     */
    private List<Configurable> saveList;

    /**
     * 所有列表
     */
    private List<Configurable> allList;

    /**
     * 设置对象
     */
    private CodeFlutterGenCodeConfigComponent settings = CodeFlutterGenCodeConfigComponent.getInstance();

    /**
     * 默认构造方法
     */
    public GenCodeConfigurableComposite() {
        // 获取当前项目
        Project project = ProjectUtils.getCurrProject();
        init();

        //初始化事件
        CodeFlutterGenCodeConfigComponent settings = CodeFlutterGenCodeConfigComponent.getInstance();
        //重置配置信息
        resetBtn.addActionListener(e -> {
            if (MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, MsgValue.RESET_DEFAULT_SETTING_MSG).isYes()) {
                if (CollectionUtil.isEmpty(resetList)) {
                    return;
                }
                // 初始化默认配置
                settings.initDefault();
                // 重置
                resetList.forEach(UnnamedConfigurable::reset);
                if (CollectionUtil.isEmpty(saveList)) {
                    return;
                }
                // 保存
                saveList.forEach(configurable -> {
                    try {
                        configurable.apply();
                    } catch (ConfigurationException e1) {
                        e1.printStackTrace();
                    }
                });
            }
        });

    }

    /**
     * 判断是否选中
     *
     * @param checkboxPanels 复选框面板
     * @return 是否选中
     */
    private boolean isSelected(@NotNull ListCheckboxPanel... checkboxPanels) {
        for (ListCheckboxPanel checkboxPanel : checkboxPanels) {
            if (!CollectionUtil.isEmpty(checkboxPanel.getSelectedItems())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 覆盖配置
     *
     * @param jsonNode json节点对象
     * @param name     配置组名称
     * @param cls      配置组类
     * @param srcGroup 源分组
     */
    private <T extends AbstractGroup> void coverConfig(@NotNull JsonNode jsonNode, @NotNull String name, Class<T> cls, @NotNull Map<String, T> srcGroup) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (!jsonNode.has(name)) {
            return;
        }
        try {
            JsonNode node = jsonNode.get(name);
            if (node.size() == 0) {
                return;
            }
            // 覆盖配置
            Iterator<String> names = node.fieldNames();
            while (names.hasNext()) {
                String key = names.next();
                String value = node.get(key).toString();
                T group = objectMapper.readValue(value, cls);
                if (srcGroup.containsKey(key)) {
                    if (!MessageDialogBuilder.yesNo(MsgValue.TITLE_INFO, String.format("是否覆盖%s配置中的%s分组？", name, key)).isYes()) {
                        continue;
                    }
                }
                srcGroup.put(key, group);
            }
        } catch (IOException e) {
            Messages.showWarningDialog("JSON解析错误！", MsgValue.TITLE_INFO);
            ExceptionUtil.rethrow(e);
        }
    }

    /**
     * 初始化方法
     */
    private void init() {
        //初始化数据
        versionLabel.setText(settings.getVersion());
        authorTextField.setText(settings.getAuthor());
    }

    /**
     * 设置显示名称
     *
     * @return 显示名称
     */
    @Nls
    @Override
    public String getDisplayName() {
        return "Make Code";
    }

    /**
     * Returns the topic in the help file which is shown when help for the configurable is requested.
     *
     * @return the help topic, or {@code null} if no help is available
     */
    @Nullable
    @Override
    public String getHelpTopic() {
        return getDisplayName();
    }

    /**
     * 更多配置
     *
     * @return 配置选项
     */
    @NotNull
    @Override
    public Configurable[] getConfigurables() {
        Configurable[] result = new Configurable[4];
        result[0] = new TypeMapperSettingConfigure(settings);
        result[1] = new TemplateSettingPanel();
        result[2] = new TableSettingConfigurePanel();
        result[3] = new GlobalConfigSettingConfigurePanel();
        // 所有列表
        allList = new ArrayList<>();
        allList.add(result[0]);
        allList.add(result[1]);
        allList.add(result[2]);
        allList.add(result[3]);
        // 需要重置的列表
        resetList = new ArrayList<>();
        resetList.add(result[0]);
        resetList.add(result[1]);
        resetList.add(result[3]);
        // 不需要重置的列表
        saveList = new ArrayList<>();
        saveList.add(this);
        saveList.add(result[2]);
        return result;
    }

    /**
     * 获取主面板信息
     *
     * @return 主面板
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        return mainPanel;
    }

    /**
     * 判断是否修改
     *
     * @return 是否修改
     */
    @Override
    public boolean isModified() {
        return !settings.getAuthor().equals(authorTextField.getText());
    }

    /**
     * 应用修改
     */
    @Override
    public void apply() {
        //保存数据
        settings.setAuthor(authorTextField.getText());
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        init();
    }
}
