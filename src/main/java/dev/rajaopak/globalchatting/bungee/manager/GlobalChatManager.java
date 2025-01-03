package dev.rajaopak.globalchatting.bungee.manager;

import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;
import dev.rajaopak.globalchatting.bungee.hooks.HookManager;
import dev.rajaopak.globalchatting.bungee.util.Common;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GlobalChatManager {

    public static void sendConsoleGlobalChat(CommandSender sender, String message) {
        String format = GlobalChattingBungee.getConfigManager().getConfiguration().getString("console.format");
        ProxyServer.getInstance().getPlayers().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(Common.translateHexColor(formatPlaceholder(sender, format, message))));
        Common.log(Common.translateHexColor(formatPlaceholder(sender, format, message)));
    }

    public static void sendPlayerGlobalChat(ProxiedPlayer player, String message) {
        String key = getKey(player);

        if (key == null) {
            player.sendMessage(Common.color(new TextComponent("&cYou don't have permission to use Global Chatting!")));
            return;
        }

        String finalPerm = GlobalChattingBungee.getConfigManager().getConfiguration().getString("globalchat." + key + ".permission");
        String finalFormat = GlobalChattingBungee.getConfigManager().getConfiguration().getString("globalchat." + key + ".format");
        boolean finalUseColor = GlobalChattingBungee.getConfigManager().getConfiguration().getBoolean("globalchat." + key + ".useColor");
        boolean finalUseHexColor = GlobalChattingBungee.getConfigManager().getConfiguration().getBoolean("globalchat." + key + ".useHexColor");
        int finalCooldown = GlobalChattingBungee.getConfigManager().getConfiguration().getInt("globalchat." + key + ".cooldown");

        if (finalFormat == null || finalFormat.isEmpty()) {
            finalFormat = "&6[Global] &7{player}&7: &f{message}";
        }

        if (finalPerm != null && !finalPerm.isEmpty()) {
            if (!player.hasPermission(finalPerm)) {
                player.sendMessage(Common.color(new TextComponent("&cYou don't have permission to use Global Chatting!")));
                return;
            }
        }

        if (finalCooldown > 0) {
            GlobalChattingBungee.getCooldownManager().setCooldown(player.getUniqueId(), finalCooldown);
        }

        sendMessage(player, message, finalFormat, finalUseColor, finalUseHexColor);
    }

    private static void sendMessage(ProxiedPlayer player, String message, String format, boolean useColor, boolean useHexColor) {
        if (useColor) {
            if (useHexColor) {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(formatPlaceholder(player, Common.translateHexColor(format), Common.translateHexColor(message))));
                Common.log(formatPlaceholder(player, Common.color(format), Common.translateHexColor(message)));
            } else {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(formatPlaceholder(player, Common.translateHexColor(format), Common.color(message))));
                Common.log(Common.color(new TextComponent(formatPlaceholder(player, format, message))).getText());
            }
        } else {
            if (useHexColor) {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(formatPlaceholder(player, Common.translateHexColor(format), Common.translateHexColor(ChatColor.stripColor(message)))));
                Common.log(formatPlaceholder(player, Common.color(format), Common.translateHexColor(ChatColor.stripColor(message))));
            } else {
                ProxyServer.getInstance().getPlayers().forEach(t -> t.sendMessage(formatPlaceholder(player, Common.translateHexColor(format), ChatColor.stripColor(message))));
                Common.log(formatPlaceholder(player, Common.color(format), ChatColor.stripColor(message)));
            }
        }
    }

    @Nullable
    public static String getKey(ProxiedPlayer player) {
        HashMap<Integer, String> priorityList = new HashMap<>();

        for (String s : GlobalChattingBungee.getConfigManager().getConfiguration().getSection("globalchat").getKeys().stream().map(Object::toString).collect(Collectors.toSet())) {
            String perm = GlobalChattingBungee.getConfigManager().getConfiguration().getString("globalchat." + s + ".permission");
            if (perm == null || perm.isEmpty()) {
                priorityList.put(GlobalChattingBungee.getConfigManager().getConfiguration().getInt("globalchat." + s + ".priority"), s);
            } else if (player.hasPermission(perm)) {
                priorityList.put(GlobalChattingBungee.getConfigManager().getConfiguration().getInt("globalchat." + s + ".priority"), s);
            }
        }

        if (priorityList.isEmpty()) {
            return null;
        }

        int priority = Collections.max(priorityList.keySet());
        return priorityList.get(priority);
    }

    public static String formatPlaceholder(CommandSender sender, String format, String message) {
        if (sender instanceof ProxiedPlayer player) {
            return format.replace("{player}", player.getName())
                    .replace("{message}", message)
                    .replace("{server}", GlobalChattingBungee.getServerGroupManager().getServerGroup(player.getServer().getInfo().getName()))
                    .replace("{luckperms_prefix}", getPlayerPrefix(player))
                    .replace("{luckperms_suffix}", getPlayerSuffix(player))
                    .replace("{time}", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()))
                    .replace("{date}", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()));
        } else {
            return format.replace("{player}", GlobalChattingBungee.getConfigManager().getConfiguration().getString("console.name"))
                    .replace("{message}", message)
                    .replace("{server}", GlobalChattingBungee.getConfigManager().getConfiguration().getString("console.server"))
                    .replace("{time}", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()))
                    .replace("{date}", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()));
        }
    }

    private static String getPlayerPrefix(ProxiedPlayer player) {
        if (!HookManager.isLuckPermsEnabled()) return "";
        String prefix = HookManager.getLuckperms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
        return prefix == null ? "" : prefix;
    }

    private static String getPlayerSuffix(ProxiedPlayer player) {
        if (!HookManager.isLuckPermsEnabled()) return "";
        String suffix = HookManager.getLuckperms().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix();
        return suffix == null ? "" : suffix;
    }

}
