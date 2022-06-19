package dev.rajaopak.globalchatting;

import dev.rajaopak.globalchatting.command.GlobalChatMuteCommand;
import dev.rajaopak.globalchatting.command.GlobalChattingCommand;
import dev.rajaopak.globalchatting.command.ReloadCommand;
import dev.rajaopak.globalchatting.config.ConfigManager;
import dev.rajaopak.globalchatting.manager.CooldownManager;
import dev.rajaopak.globalchatting.metrics.Metrics;
import net.md_5.bungee.api.plugin.Plugin;

public final class GlobalChatting extends Plugin {

    private static ConfigManager configManager;
    private static CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        configManager = new ConfigManager(this);
        cooldownManager = new CooldownManager();

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

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
