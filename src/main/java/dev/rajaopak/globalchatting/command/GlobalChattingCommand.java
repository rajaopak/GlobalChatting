package dev.rajaopak.globalchatting.command;

import dev.rajaopak.globalchatting.GlobalChatting;
import dev.rajaopak.globalchatting.hooks.HookManager;
import dev.rajaopak.globalchatting.manager.GlobalChatManager;
import dev.rajaopak.globalchatting.util.Common;
import litebans.api.Database;
import me.leoko.advancedban.manager.PunishmentManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalChattingCommand extends Command {

    public GlobalChattingCommand() {
        super("globalchatting", null, "gc", "globalchat", "gchat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            if (args.length == 0) {
                sendHelp(sender);
                return;
            }

            StringBuilder message = new StringBuilder();

            for (String s : args) {
                message.append(s).append(" ");
            }
            message = new StringBuilder(message.substring(0, message.length() - 1));

            if (message.toString().isEmpty()) {
                sendHelp(sender);
            }

            GlobalChatManager.sendConsoleGlobalChat(sender, message.toString());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (GlobalChatting.getConfigManager().isMuted() && !sender.hasPermission("globalchatting.bypass-muted")) {
            sender.sendMessage(Common.color(new TextComponent("&cGlobal Chat is currently muted!")));
            return;
        }

        if (HookManager.isLiteBansEnable() && Database.get().isPlayerMuted(player.getUniqueId(), player.getAddress().getHostName())) {
            sender.sendMessage(Common.color(new TextComponent("&cYou are currently muted!")));
            return;
        }

        if (HookManager.isAdvancedBanEnable() && PunishmentManager.get().isMuted(player.getUniqueId().toString().replace("-", ""))) {
            sender.sendMessage(Common.color(new TextComponent("&cYou are currently muted!")));
            return;
        }

        if (GlobalChatting.getCooldownManager().isCooldown(player.getUniqueId())) {
            sender.sendMessage(Common.color(new TextComponent("&cYou need to wait " + Common.formatTime((int) GlobalChatting.getCooldownManager().getCooldown(player.getUniqueId())) + " before using Global Chat again!")));
            return;
        }

        if (args.length == 0) {
            sendHelp(player);
            return;
        }

        StringBuilder message = new StringBuilder();

        for (String s : args) {
            message.append(s).append(" ");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));

        if (message.toString().isEmpty()) {
            sendHelp(player);
        }

        GlobalChatManager.sendPlayerGlobalChat(player, message.toString());
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(Common.color(new TextComponent("&cUsage: /globalchatting <message>")));
        sender.sendMessage(Common.color(new TextComponent("&6Aliases: /gc, /globalchat, /gchat")));
    }
}
