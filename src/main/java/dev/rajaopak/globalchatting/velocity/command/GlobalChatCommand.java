package dev.rajaopak.globalchatting.velocity.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import dev.rajaopak.globalchatting.velocity.hooks.HookManager;
import dev.rajaopak.globalchatting.velocity.manager.GlobalChatManager;
import dev.rajaopak.globalchatting.velocity.util.Common;
import litebans.api.Database;
import me.leoko.advancedban.manager.PunishmentManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class GlobalChatCommand {

    public static BrigadierCommand globalChatCommand(final GlobalChattingVelocity plugin) {
        LiteralCommandNode<CommandSource> node = BrigadierCommand.literalArgumentBuilder("globalchatting")
                .executes(context -> {
                    context.getSource().sendMessage(Component.text("Usage: /globalchatting <text>", NamedTextColor.GOLD));
                    context.getSource().sendMessage(Component.text("Aliases: /gc, /gchat, /globalchat", NamedTextColor.GOLD));
                    return Command.SINGLE_SUCCESS;
                })
                .then(BrigadierCommand.requiredArgumentBuilder("text", StringArgumentType.greedyString())
                        .suggests((context, builder) -> builder.buildFuture())
                        .executes(context -> {
                            if (context.getSource() instanceof ConsoleCommandSource console) {
                                String message = context.getArgument("text", String.class);

                                GlobalChatManager.sendConsoleGlobalChat(console, message);

                                return Command.SINGLE_SUCCESS;
                            }

                            Player player = (Player) context.getSource();

                            if (plugin.getConfigManager().isMuted() && !player.hasPermission("globalchatting.bypass-muted")) {
                                player.sendMessage(Component.text("Global Chat is currently muted!", NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            if (HookManager.isLiteBansEnabled() && Database.get().isPlayerMuted(player.getUniqueId(), player.getRemoteAddress().getHostName())) {
                                player.sendMessage(Component.text("You are currently muted!", NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            if (HookManager.isAdvancedBanEnabled() && PunishmentManager.get().isMuted(player.getUniqueId().toString())) {
                                player.sendMessage(Component.text("You are currently muted!", NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            if (plugin.getCooldownManager().isCooldown(player.getUniqueId())) {
                                player.sendMessage(Component.text("&cYou need to wait " + Common.formatTime((int) plugin.getCooldownManager().getCooldown(player.getUniqueId())) + " before using Global Chat again!", NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            }

                            if (plugin.getConfigManager().getConfiguration().getStringList("blacklist-server").contains(player.getCurrentServer().orElseThrow().getServer().getServerInfo().getName()) &&
                            !player.hasPermission("globalchatting.bypass")) {
                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(plugin.getConfigManager().getConfiguration().getString("blacklist-server-message")));
                                return Command.SINGLE_SUCCESS;
                            }

                            String message = context.getArgument("text", String.class);

                            GlobalChatManager.sendPlayerGlobalChat(player, message);

                            return Command.SINGLE_SUCCESS;
                        })
                ).build();

        return new BrigadierCommand(node);
    }

}
