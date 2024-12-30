package dev.rajaopak.globalchatting.velocity.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class GlobalChatMuteCommand {

    public static BrigadierCommand globalChatMuteCommand(final GlobalChattingVelocity plugin) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("globalchattingmute")
                .requires(source -> source.hasPermission("globalchatting.mute"))
                .executes(context -> {

                    if (!plugin.getConfigManager().getConfiguration().getBoolean("is-muted")) {
                        plugin.getConfigManager().getConfiguration().set("is-muted", true);
                        plugin.getConfigManager().saveConfig();
                        plugin.getProxy().getAllPlayers().forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize("<bold><dark_gray>[</dark_gray><yellow>GlobalChat</yellow><dark_gray>]</dark_gray></bold> <red>Global Chatting is now muted!")));
                        GlobalChattingVelocity.getPlugin().getProxy().getConsoleCommandSource().sendMessage(MiniMessage.miniMessage().deserialize("<bold><dark_gray>[</dark_gray><yellow>GlobalChat</yellow><dark_gray>]</dark_gray></bold> <red>Global Chatting is now muted!"));
                    } else {
                        plugin.getConfigManager().getConfiguration().set("is-muted", false);
                        plugin.getConfigManager().saveConfig();
                        plugin.getProxy().getAllPlayers().forEach(player -> player.sendMessage(MiniMessage.miniMessage().deserialize("<bold><dark_gray>[</dark_gray><yellow>GlobalChat</yellow><dark_gray>]</dark_gray></bold> <green>Global Chatting is now unmuted!")));
                        GlobalChattingVelocity.getPlugin().getProxy().getConsoleCommandSource().sendMessage(MiniMessage.miniMessage().deserialize("<bold><dark_gray>[</dark_gray><yellow>GlobalChat</yellow><dark_gray>]</dark_gray></bold> <green>Global Chatting is now unmuted!"));
                    }

                    return Command.SINGLE_SUCCESS;
                }).build();

        return new BrigadierCommand(node);
    }
}
