package dev.rajaopak.globalchatting.velocity.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.IOException;

public class ReloadCommand {

    public static BrigadierCommand reloadCommand(final GlobalChattingVelocity plugin) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("globachattingreload")
                .requires(source -> source.hasPermission("globalchatting.reload"))
                .executes(context -> {

                    context.getSource().sendMessage(Component.text("Reloading the configuration...", NamedTextColor.YELLOW));
                    if(!GlobalChattingVelocity.getPlugin().reloadConfigs()) {
                        context.getSource().sendMessage(Component.text("Failed to reload the config.", NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }
                    context.getSource().sendMessage(Component.text("Configuration reloaded!", NamedTextColor.GREEN));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        return new BrigadierCommand(node);
    }
}
