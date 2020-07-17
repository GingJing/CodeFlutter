package com.github.gingjing.plugin.translate.actions;


import com.github.gingjing.plugin.formatter.ui.modules.ColorService;
import com.github.gingjing.plugin.translate.response.GoogleResponse;
import com.github.gingjing.plugin.translate.ui.AbstractMyTranslateBalloon;
import com.github.gingjing.plugin.translate.ui.GoogleTranslateBalloon;
import com.intellij.notification.impl.NotificationsManagerImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

/**
 * 谷歌翻译事件菜单
 *
 * @author: D丶Cheng
 * @modify by gingjingdm
 * @date: 2020年 06月28日 21时09分
 * @version: 1.0
 */
public class GoogleTranslateAndShowAction extends AbstractTranslateAndShowAction {


    @Override
    protected void showPopupBalloon(GoogleResponse result, String translateType) {
        ApplicationManager.getApplication().invokeLater((Runnable) new Runnable() {
            @Override
            public void run() {
                final JBPopupFactory factory = JBPopupFactory.getInstance();
                GoogleTranslateBalloon googleTranslateBalloon = new GoogleTranslateBalloon(result);
                BalloonBuilder balloonBuilder = factory.createBalloonBuilder(googleTranslateBalloon.getMainPanel());
                balloonBuilder.setFillColor(ColorService.forCurrentTheme(ColorService.Background));
                balloonBuilder.setContentInsets(JBUI.insets(40, 40));
                balloonBuilder.setBorderInsets(JBUI.emptyInsets());
                balloonBuilder.setBorderColor(ColorService.forCurrentTheme(ColorService.Background));
                balloonBuilder.setShadow(true);
                Balloon balloon = balloonBuilder.createBalloon();
                setBounds(googleTranslateBalloon, balloon);
                IdeFrame window = (IdeFrame) NotificationsManagerImpl.findWindowForBalloon(project);
                RelativePoint pointToShowPopup = null;
                if (window != null) {
                    pointToShowPopup = RelativePoint.getSouthEastOf(window.getComponent());
                }
                balloon.show(pointToShowPopup, Balloon.Position.above);
            }
        });
    }

    /**
     * 设置了宽高，定位就无效了...
     *
     * @param balloon
     */
    private void setBounds(AbstractMyTranslateBalloon abstractTranslateBalloons, Balloon balloon) {
        int width = abstractTranslateBalloons.getWidth();
        int height = abstractTranslateBalloons.getHeight() + 80;
        JFrame jFrame = WindowManager.getInstance().getFrame(project);
        int x = (int) (jFrame.getBounds().getWidth() / 2 - width / 2);
        int y = (int) (jFrame.getBounds().getHeight() / 2 - height / 2);
        balloon.setBounds(new Rectangle(x, y, width, height));
    }
}
