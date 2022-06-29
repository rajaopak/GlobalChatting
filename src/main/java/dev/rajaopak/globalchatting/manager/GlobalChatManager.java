package dev.rajaopak.globalchatting.manager;

import dev.rajaopak.globalchatting.GlobalChatting;
import dev.rajaopak.globalchatting.util.Common;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;

import static dev.rajaopak.globalchatting.util.Common.*;

public class GlobalChatManager {

    public static void sendPlayerGlobalChat(ProxiedPlayer player, String message) {
        HashMap<Integer, String> priorityList = new HashMap<>();

        for (String s : GlobalChatting.getConfigManager().getConfiguration().getSection("globalchat").getKeys()) {
            String perm = GlobalChatting.getConfigManager().getConfiguration().getString("globalchat." + s + ".permission");
            if (perm == null || perm.isEmpty()) {
                priorityList.put(GlobalChatting.getConfigManager().getConfiguration().getInt("globalchat." + s + ".priority"), s);
            } else if (player.hasPermission(perm)) {
                priorityList.put(GlobalChatting.getConfigManager().getConfiguration().getInt("globalchat." + s + ".priority"), s);
            }
        }

        if (priorityList.isEmpty()) {
            player.sendMessage(color(new TextComponent("&cYou don't have permission to use Global Chatting!")));
            return;
        }

        int finalPriority = Collections.max(priorityList.keySet());
        String finalKey = priorityList.get(finalPriority);
        String finalPerm = GlobalChatting.getConfigManager().getConfiguration().getString("globalchat." + finalKey + ".permission");
        String finalFormat = GlobalChatting.getConfigManager().getConfiguration().getString("globalchat." + finalKey + ".format");
        boolean finalUseColor = GlobalChatting.getConfigManager().getConfiguration().getBoolean("globalchat." + finalKey + ".useColor");
        boolean finalUseHexColor = GlobalChatting.getConfigManager().getConfiguration().getBoolean("globalchat." + finalKey + ".useHexColor");
        int finalCooldown = GlobalChatting.getConfigManager().getConfiguration().getInt("globalchat." + finalKey + ".cooldown");

        if (finalFormat == null || finalFormat.isEmpty()) {
            finalFormat = "&6[Global] &7{player}&7: &f{message}";
        }

        if (finalPerm != null && !finalPerm.isEmpty()) {
            if (!player.hasPermission(finalPerm)) {
                player.sendMessage(color(new TextComponent("&cYou don't have permission to use Global Chatting!")));
            }
        }

        if (finalCooldown > 0) {
            GlobalChatting.getCooldownManager().setCooldown(player.getUniqueId(), finalCooldown);
        }

        if (finalUseColor) {
            String finalFormat1 = finalFormat;
            if (finalUseHexColor) {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(formatPlaceholder(player, color(finalFormat1), translateHexColor(message))));
                Common.log(formatPlaceholder(player, color(finalFormat), translateHexColor(message)));
            } else {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(color(new TextComponent(formatPlaceholder(player, finalFormat1, message)))));
                Common.log(color(new TextComponent(formatPlaceholder(player, finalFormat, message))).getText());
            }
        } else {
            String finalFormat2 = finalFormat;
            if (finalUseHexColor) {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(formatPlaceholder(player, color(finalFormat2), translateHexColor(ChatColor.stripColor(message)))));
                Common.log(formatPlaceholder(player, color(finalFormat), translateHexColor(ChatColor.stripColor(message))));
            } else {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(new TextComponent(formatPlaceholder(player, color(finalFormat2), ChatColor.stripColor(message)))));
                Common.log(formatPlaceholder(player, color(finalFormat), ChatColor.stripColor(message)));
            }
        }
    }

    public static String formatPlaceholder(ProxiedPlayer player, String format, String message) {
        return format.replace("{player}", player.getName()).replace("{message}", message)
                .replace("{server}", player.getServer().getInfo().getName())
                .replace("{time}", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()))
                .replace("{date}", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()));
    }

}
