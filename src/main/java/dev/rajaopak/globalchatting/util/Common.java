package dev.rajaopak.globalchatting.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class Common {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static TextComponent color(TextComponent s) {
        return new TextComponent(color(s.getText()));
    }

    public static void log(String s) {
        ProxyServer.getInstance().getLogger().info(s);
    }

    public static String formatTime(int seconds) {
        if (seconds == 0) {
            return "0s";
        }

        long minute = seconds / 60;
        seconds = seconds % 60;
        long hour = minute / 60;
        minute = minute % 60;
        long day = hour / 24;
        hour = hour % 24;

        StringBuilder time = new StringBuilder();
        if (day != 0) {
            time.append(day).append("d ");
        }
        if (hour != 0) {
            time.append(hour).append("h ");
        }
        if (minute != 0) {
            time.append(minute).append("m ");
        }
        if (seconds != 0) {
            time.append(seconds).append("s");
        }

        return time.toString().trim();
    }

}
