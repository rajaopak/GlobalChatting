package dev.rajaopak.globalchatting.command;

import dev.rajaopak.globalchatting.GlobalChatting;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import static dev.rajaopak.globalchatting.util.Common.color;

public class GlobalChatMuteCommand extends Command {

    public GlobalChatMuteCommand() {
        super("globalchatmute", "globalchatting.mute", "gcmute", "gchatmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(getPermission())) {
            if (args.length == 0) {
                if (!GlobalChatting.getConfigManager().getConfiguration().getBoolean("is-muted")) {
                    GlobalChatting.getConfigManager().getConfiguration().set("is-muted", true);
                    GlobalChatting.getConfigManager().saveConfig();
                    ProxyServer.getInstance().getPlayers().forEach(player -> player.sendMessage(color(new TextComponent("&cGlobal Chatting is now muted!"))));
                } else {
                    GlobalChatting.getConfigManager().getConfiguration().set("is-muted", false);
                    GlobalChatting.getConfigManager().saveConfig();
                    ProxyServer.getInstance().getPlayers().forEach(player -> player.sendMessage(color(new TextComponent("&aGlobal Chatting is now unmuted!"))));
                }
            } else {
                sender.sendMessage(color(new TextComponent("&cUsage: /globalchatmute")));
                sender.sendMessage(color(new TextComponent("&6Aliases: /gcmute, /gchatmute")));
            }
        } else {
            sender.sendMessage(color(new TextComponent("&cYou don't have permission to use this command!")));
        }
    }
}
