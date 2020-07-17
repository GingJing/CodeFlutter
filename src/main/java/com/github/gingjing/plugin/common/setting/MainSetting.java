package com.github.gingjing.plugin.common.setting;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.time.LocalDateTime;


/**
 * 插件主配置面板
 *
 * @author: gingjingdm
 * @date: 2020年 07月05日 15时51分
 * @version: 1.0
 */
public class MainSetting implements Configurable {

    private JPanel contentPanel;
    private JLabel timeLb;
    private JLabel noteLb;

    private MainPersistence setting;

    public MainSetting() {
        init();
    }

    private void init() {
        LocalDateTime now = LocalDateTime.now();
        this.noteLb.setText("插件：Code Flutter 作者：GingJingDM 版本： 1.0.0");
        this.timeLb.setText(
                now.getYear() + "年 " +
                now.getMonthValue() + "月" +
                now.getDayOfMonth() + "日 " +
                now.getHour() + "时" +
                now.getMinute() + "分"
        );
        if (setting == null) {
            setting = new MainPersistence();
        }
    }



    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "CodeFlutter";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return contentPanel;
    }

    @Override
    public boolean isModified() {
        return !this.noteLb.getText().equals(setting.getNote());
    }

    @Override
    public void apply() {
        this.setting.setNote(this.noteLb.getText());
    }
}
