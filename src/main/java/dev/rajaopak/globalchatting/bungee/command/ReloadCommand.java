package dev.rajaopak.globalchatting.bungee.command;

import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;
import dev.rajaopak.globalchatting.bungee.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("globalchattingreload", "globalchatting.reload", "gcreload", "gchatreload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(Common.color(new TextComponent("&cYou don't have permission to use this command!")));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Common.color(new TextComponent("&eReloading the configuration...")));
            GlobalChattingBungee.getConfigManager().reloadConfig();
            sender.sendMessage(Common.color(new TextComponent("&aConfiguration reloaded!")));
        } else {
            sender.sendMessage(Common.color(new TextComponent("&cUsage: /globalchattingreload")));
            sender.sendMessage(Common.color(new TextComponent("&6Aliases: /gcreload, /gchatreload")));
        }
    }
}
