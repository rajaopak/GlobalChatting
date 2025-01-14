package dev.rajaopak.globalchatting.bungee.command;

import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;
import dev.rajaopak.globalchatting.bungee.hooks.HookManager;
import dev.rajaopak.globalchatting.bungee.manager.GlobalChatManager;
import dev.rajaopak.globalchatting.bungee.util.Common;
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
        if (GlobalChattingBungee.getConfigManager().isMuted() && !sender.hasPermission("globalchatting.bypass-muted")) {
            sender.sendMessage(Common.color(new TextComponent("&cGlobal Chat is currently muted!")));
            return;
        }

        if (HookManager.isLiteBansEnabled() && Database.get().isPlayerMuted(player.getUniqueId(), player.getAddress().getHostName())) {
            sender.sendMessage(Common.color(new TextComponent("&cYou are currently muted!")));
            return;
        }

        if (HookManager.isAdvancedBanEnabled() && PunishmentManager.get().isMuted(player.getUniqueId().toString().replace("-", ""))) {
            sender.sendMessage(Common.color(new TextComponent("&cYou are currently muted!")));
            return;
        }

        if (GlobalChattingBungee.getCooldownManager().isCooldown(player.getUniqueId())) {
            sender.sendMessage(Common.color(new TextComponent("&cYou need to wait " + Common.formatTime((int) GlobalChattingBungee.getCooldownManager().getCooldown(player.getUniqueId())) + " before using Global Chat again!")));
            return;
        }

        if (GlobalChattingBungee.getConfigManager().getConfiguration().getStringList("blacklist-server").contains(player.getServer().getInfo().getName()) &&
        !sender.hasPermission("globalchatting.bypass")) {
            sender.sendMessage(Common.color(new TextComponent(GlobalChattingBungee.getConfigManager().getConfiguration().getString("blacklist-server-message"))));
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
        sender.sendMessage(Common.color(new TextComponent("&6Aliases: /gc, /gchat, /globalchat")));
    }
}
