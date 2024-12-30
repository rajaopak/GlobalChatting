package dev.rajaopak.globalchatting.velocity.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class Common {

    public static Component translate(String string) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();

        return serializer.deserialize(string);
    }

    public static @NotNull Component translateHexColor(String string) {
//        Matcher matcher = HEX_PATTERN.matcher(string);
//        StringBuffer buffer = new StringBuffer();
//
//        while (matcher.find()) {
//            matcher.appendReplacement(buffer, "<#" + matcher.group(1) + ">");
//        }

        LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().character('&').hexCharacter('#').hexColors().build();

//        return MiniMessage.miniMessage().deserialize(matcher.appendTail(buffer).toString());
        return serializer.deserialize(string);
    }

    public static @NotNull Component color(String s) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
    }

    public static Component color(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(component));
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
