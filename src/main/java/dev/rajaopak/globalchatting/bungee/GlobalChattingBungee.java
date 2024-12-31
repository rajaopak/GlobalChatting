package dev.rajaopak.globalchatting.bungee;

import dev.rajaopak.globalchatting.bungee.command.GlobalChatMuteCommand;
import dev.rajaopak.globalchatting.bungee.command.GlobalChattingCommand;
import dev.rajaopak.globalchatting.bungee.command.ReloadCommand;
import dev.rajaopak.globalchatting.bungee.config.ConfigManager;
import dev.rajaopak.globalchatting.bungee.hooks.HookManager;
import dev.rajaopak.globalchatting.bungee.listener.LuckPermsListener;
import dev.rajaopak.globalchatting.bungee.manager.CooldownManager;
import dev.rajaopak.globalchatting.bungee.manager.ServerGroupManager;
import dev.rajaopak.globalchatting.bungee.metrics.Metrics;
import net.md_5.bungee.api.plugin.Plugin;

public final class GlobalChattingBungee extends Plugin {

    private static ConfigManager configManager;
    private static CooldownManager cooldownManager;
    private static ServerGroupManager serverGroupManager;

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public static ServerGroupManager getServerGroupManager() {
        return serverGroupManager;
    }

    public static boolean reloadConfigs() {
        if (getConfigManager().reloadConfig()) {
            getServerGroupManager().reload();
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        configManager = new ConfigManager(this);
        cooldownManager = new CooldownManager();
        serverGroupManager = new ServerGroupManager();

        if (HookManager.isLuckPermsEnable()) {
            new LuckPermsListener(this);
        }

        this.getProxy().getPluginManager().registerCommand(this, new GlobalChattingCommand());
        this.getProxy().getPluginManager().registerCommand(this, new GlobalChatMuteCommand());
        this.getProxy().getPluginManager().registerCommand(this, new ReloadCommand());

        new Metrics(this, 15432);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getCooldownManager().clearCooldowns();
    }
}
