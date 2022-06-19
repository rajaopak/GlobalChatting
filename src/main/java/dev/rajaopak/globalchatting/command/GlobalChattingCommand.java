package dev.rajaopak.globalchatting.command;

import dev.rajaopak.globalchatting.GlobalChatting;
import dev.rajaopak.globalchatting.manager.GlobalChatManager;
import dev.rajaopak.globalchatting.util.Common;
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
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (!GlobalChatting.getConfigManager().isMuted()) {
                if (!GlobalChatting.getCooldownManager().isCooldown(player.getUniqueId())) {
                    if (args.length == 0) {
                        sendHelp(player);
                    } else {
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
                } else {
                    sender.sendMessage(color(new TextComponent("&cYou need to wait " + Common.formatTime((int) GlobalChatting.getCooldownManager().getCooldown(player.getUniqueId())) + " before using Global Chatting again!")));
                }
            } else if (sender.hasPermission("globalchatting.bypass-muted")) {
                if (!GlobalChatting.getCooldownManager().isCooldown(player.getUniqueId())) {
                    if (args.length == 0) {
                        sendHelp(player);
                    } else {
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
                } else {
                    sender.sendMessage(color(new TextComponent("&cYou need to wait " + Common.formatTime((int) GlobalChatting.getCooldownManager().getCooldown(player.getUniqueId())) + " before using Global Chatting again!")));
                }
            } else {
                sender.sendMessage(color(new TextComponent("&cGlobalChatting is currently muted!")));
            }
        } else {
            sender.sendMessage(color(new TextComponent("&cYou need to be a player to use this command!")));
        }
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage(color(new TextComponent("&cUsage: /globalchatting <message>")));
        sender.sendMessage(color(new TextComponent("&6Aliases: /gc, /globalchat, /gchat")));
    }
}
