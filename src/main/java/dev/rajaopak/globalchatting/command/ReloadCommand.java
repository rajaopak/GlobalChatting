package dev.rajaopak.globalchatting.command;

import dev.rajaopak.globalchatting.GlobalChatting;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import static dev.rajaopak.globalchatting.util.Common.color;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("globalchattingreload", "globalchatting.reload", "gcreload", "gchatreload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(getPermission())) {
            if (args.length == 0) {
                sender.sendMessage(color(new TextComponent("&cReloading the configuration...")));
                GlobalChatting.getConfigManager().reloadConfig();
                sender.sendMessage(color(new TextComponent("&aConfiguration reloaded!")));
            } else {
                sender.sendMessage(color(new TextComponent("&cUsage: /globalchattingreload")));
                sender.sendMessage(color(new TextComponent("&6Aliases: /gcreload, /gchatreload")));
            }
        } else {
            sender.sendMessage(color(new TextComponent("&cYou don't have permission to use this command!")));
        }
    }
}
