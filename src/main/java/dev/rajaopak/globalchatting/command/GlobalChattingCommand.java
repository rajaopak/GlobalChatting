package dev.rajaopak.globalchatting.command;

import dev.rajaopak.globalchatting.GlobalChatting;
import dev.rajaopak.globalchatting.hooks.HookManager;
import dev.rajaopak.globalchatting.manager.GlobalChatManager;
import dev.rajaopak.globalchatting.util.Common;
import litebans.api.Database;
import litebans.api.Entry;
import me.leoko.advancedban.manager.PunishmentManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import static dev.rajaopak.globalchatting.util.Common.color;

public class GlobalChattingCommand extends Command {

    public GlobalChattingCommand() {
        super("globalchatting", null, "gc", "globalchat", "gchat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(color(new TextComponent("&cYou need to be a player to use this command!")));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (GlobalChatting.getConfigManager().isMuted()) {
            sender.sendMessage(color(new TextComponent("&cGlobalChatting is currently muted!")));
            return;
        }

        if (HookManager.isLiteBansEnable() && Database.get().isPlayerMuted(player.getUniqueId(), player.getAddress().getHostName())) {
            sender.sendMessage(color(new TextComponent("&cYou are currently muted!")));
            return;
        }

        /*if (HookManager.isAdvancedBanEnable() && PunishmentManager.get().isMuted(player.getUniqueId().toString())) {
            sender.sendMessage(color(new TextComponent("&cYou are currently muted!")));
            return;
        }*/

        if (GlobalChatting.getCooldownManager().isCooldown(player.getUniqueId())) {
            sender.sendMessage(color(new TextComponent("&cYou need to wait " + Common.formatTime((int) GlobalChatting.getCooldownManager().getCooldown(player.getUniqueId())) + " before using Global Chatting again!")));
            return;
        }

        if (GlobalChatting.getConfigManager().isMuted() && !sender.hasPermission("globalchatting.bypass-muted")) {
            sender.sendMessage(color(new TextComponent("&cGlobalChatting is currently muted!")));
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
        sender.sendMessage(color(new TextComponent("&cUsage: /globalchatting <message>")));
        sender.sendMessage(color(new TextComponent("&6Aliases: /gc, /globalchat, /gchat")));
    }
}
