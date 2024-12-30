package dev.rajaopak.globalchatting.bungee.command;

import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;
import dev.rajaopak.globalchatting.bungee.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class GlobalChatMuteCommand extends Command {

    public GlobalChatMuteCommand() {
        super("globalchattingmute", "globalchatting.mute", "gcmute", "gchatmute", "globalchatmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(Common.color(new TextComponent("&cYou don't have permission to use this command!")));
            return;
        }

        if (args.length == 0) {
            if (!GlobalChattingBungee.getConfigManager().getConfiguration().getBoolean("is-muted")) {
                GlobalChattingBungee.getConfigManager().getConfiguration().set("is-muted", true);
                GlobalChattingBungee.getConfigManager().saveConfig();
                ProxyServer.getInstance().getPlayers().forEach(player -> player.sendMessage(Common.color(new TextComponent("&cGlobal Chatting is now muted!"))));
                Common.log(Common.color(new TextComponent("&cGlobal Chatting is now muted!")).getText());
            } else {
                GlobalChattingBungee.getConfigManager().getConfiguration().set("is-muted", false);
                GlobalChattingBungee.getConfigManager().saveConfig();
                ProxyServer.getInstance().getPlayers().forEach(player -> player.sendMessage(Common.color(new TextComponent("&aGlobal Chatting is now unmuted!"))));
                Common.log(Common.color(new TextComponent("&aGlobal Chatting is now unmuted!")).getText());
            }
        } else {
            sender.sendMessage(Common.color(new TextComponent("&cUsage: /globalchattingmute")));
            sender.sendMessage(Common.color(new TextComponent("&6Aliases: /gcmute, /gchatmute, globalchatmute")));
        }
    }
}
