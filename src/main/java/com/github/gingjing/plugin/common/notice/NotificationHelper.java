package com.github.gingjing.plugin.common.notice;

import com.intellij.notification.*;

/**
 * 通知帮助器
 *
 * @author: GingJingDM
 * @date: 2020年 07月25日 14时11分
 * @version: 1.0
 */
public class NotificationHelper {

    public static void info(String message) {
        sendNotification(message, NotificationType.INFORMATION);
    }

    public static void error(String message) {
        sendNotification(message, NotificationType.ERROR);
    }

    public static void sendNotification(String message, NotificationType notificationType) {
        if (message == null || message.trim().length() == 0) {
            return;
        }
        Notification notification = new Notification("com.github.gingjing.plugin.CodeFlutter", "CodeFlutter ", espaceString(message), notificationType);
        Notifications.Bus.notify(notification);
    }

    public static void info(String massage, NotificationGroup notificationGroup) {
        sendNotificationByGroup(notificationGroup, massage, NotificationType.INFORMATION);
    }

    public static void error(String message, NotificationGroup notificationGroup) {
        sendNotificationByGroup(notificationGroup, message, NotificationType.ERROR);
    }

    public static void sendNotificationByGroup(NotificationGroup notificationGroup, String massage, NotificationType notificationType) {
        Notification notification = notificationGroup.createNotification(massage, notificationType);
        Notifications.Bus.notify(notification);
    }


    private static String espaceString(String string) {
        // replace with both so that it returns are preserved in the notification ballon and in the event log
        return string.replaceAll("\n", "\n<br />");
    }

    public static NotificationGroup make(String title) {
        return make(title, NotificationDisplayType.BALLOON);
    }

    public static NotificationGroup make(String title, NotificationDisplayType notificationDisplayType) {
        return new NotificationGroup(title, notificationDisplayType, true);
    }
}
