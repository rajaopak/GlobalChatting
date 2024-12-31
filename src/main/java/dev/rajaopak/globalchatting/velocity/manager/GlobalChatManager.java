package dev.rajaopak.globalchatting.velocity.manager;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import dev.rajaopak.globalchatting.velocity.hooks.HookManager;
import dev.rajaopak.globalchatting.velocity.util.Common;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPermsProvider;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GlobalChatManager {

    public static void sendConsoleGlobalChat(ConsoleCommandSource sender, String message) {
        String format = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getString("console.format");
        GlobalChattingVelocity.getPlugin().getProxy().getAllPlayers().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(formatPlaceholder(sender, format, Common.translate(message))));
        sender.sendMessage(formatPlaceholder(sender, format, Common.translate(message)));
    }

    public static void sendPlayerGlobalChat(Player player, String message) {
        String key = getKey(player);

        if (key == null) {
            player.sendMessage(Common.color("&cYou don't have permission to use Global Chatting!"));
            return;
        }

        String finalPerm = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getString("globalchat." + key + ".permission");
        String finalFormat = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getString("globalchat." + key + ".format");
        boolean finalUseColor = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getBoolean("globalchat." + key + ".useColor");
        boolean finalUseHexColor = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getBoolean("globalchat." + key + ".useHexColor");
        int finalCooldown = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getInt("globalchat." + key + ".cooldown");

        if (finalFormat == null || finalFormat.isEmpty()) {
            finalFormat = "&6[Global] &7{player}&7: &f{message}";
        }

        if (finalPerm != null && !finalPerm.isEmpty()) {
            if (!player.hasPermission(finalPerm)) {
                player.sendMessage(Common.color("&cYou don't have permission to use Global Chatting!"));
                return;
            }
        }

        if (finalCooldown > 0) {
            GlobalChattingVelocity.getPlugin().getCooldownManager().setCooldown(player.getUniqueId(), finalCooldown);
        }

        sendMessage(player, message, finalFormat, finalUseColor, finalUseHexColor);
    }

    private static void sendMessage(Player player, String message, String format, boolean useColor, boolean useHexColor) {
        if (useColor) {
            if (useHexColor) {
                GlobalChattingVelocity.getPlugin().getProxy().getAllPlayers().forEach(target -> target.sendMessage(formatPlaceholder(player, format, Common.translate(message))));
                GlobalChattingVelocity.getPlugin().getProxy().getConsoleCommandSource().sendMessage(formatPlaceholder(player, format, Common.translate(message)));
                return;
            }

            GlobalChattingVelocity.getPlugin().getProxy().getAllPlayers().forEach(target -> target.sendMessage(formatPlaceholder(player, format, Common.color(message))));
            GlobalChattingVelocity.getPlugin().getProxy().getConsoleCommandSource().sendMessage(formatPlaceholder(player, format, Common.color(message)));
            return;
        }

        if (useHexColor) {
            GlobalChattingVelocity.getPlugin().getProxy().getAllPlayers().forEach(target -> target.sendMessage(formatPlaceholder(player, format, Common.translateHexColor(message))));
            GlobalChattingVelocity.getPlugin().getProxy().getConsoleCommandSource().sendMessage(formatPlaceholder(player, format, Common.translateHexColor(message)));
            return;
        }

        GlobalChattingVelocity.getPlugin().getProxy().getAllPlayers().forEach(target -> target.sendMessage(formatPlaceholder(player, format, PlainTextComponentSerializer.plainText().deserialize(message))));
        GlobalChattingVelocity.getPlugin().getProxy().getConsoleCommandSource().sendMessage(formatPlaceholder(player, format, PlainTextComponentSerializer.plainText().deserialize(message)));
    }

    @Nullable
    public static String getKey(Player player) {
        HashMap<Integer, String> priorityList = new HashMap<>();

        for (String s : GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getSection("globalchat").getKeys().stream().map(Object::toString).collect(Collectors.toSet())) {
            String perm = GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getString("globalchat." + s + ".permission");
            if (perm == null || perm.isEmpty()) {
                priorityList.put(GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getInt("globalchat." + s + ".priority"), s);
            } else if (player.hasPermission(perm)) {
                priorityList.put(GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getInt("globalchat." + s + ".priority"), s);
            }
        }

        if (priorityList.isEmpty()) {
            return null;
        }

        int priority = Collections.max(priorityList.keySet());
        return priorityList.get(priority);
    }

    private static Component formatPlaceholder(CommandSource sender, String format, Component message) {
        if (sender instanceof Player player) {
            return Common.translate(format.replace("{player}", player.getUsername())
                            .replace("{server}", GlobalChattingVelocity.getPlugin().getServerGroupManager().getServerGroup(player.getCurrentServer().orElseThrow().getServer().getServerInfo().getName()))
                            .replace("{luckperms_prefix}", getPlayerPrefix(player))
                            .replace("{luckperms_suffix}", getPlayerSuffix(player))
                            .replace("{time}", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()))
                            .replace("{date}", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())))
                    .replaceText(TextReplacementConfig.builder().matchLiteral("{message}").replacement(message).build());
        } else {
            return Common.translate(format.replace("{player}", GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getString("console.name"))
                            .replace("{server}", GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getString("console.server"))
                            .replace("{time}", DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()))
                            .replace("{date}", DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())))
                    .replaceText(TextReplacementConfig.builder().matchLiteral("{message}").replacement(message).build());
        }
    }

    private static String getPlayerPrefix(Player player) {
        if (!HookManager.isLuckPermsEnable()) return "";
        String prefix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
        return prefix == null ? "" : prefix;
    }

    private static String getPlayerSuffix(Player player) {
        if (!HookManager.isLuckPermsEnable()) return "";
        String suffix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getSuffix();
        return suffix == null ? "" : suffix;
    }

}
