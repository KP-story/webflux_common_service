package com.delight.notify.utils;

public class NotificationTextUtils {
    public static String formatText(String text) {
        if (text.length() > 100) {
            return text.substring(0, 100) + "...";
        } else return text;
    }
}
