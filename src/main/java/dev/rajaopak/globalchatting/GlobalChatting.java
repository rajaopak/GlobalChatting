package dev.rajaopak.globalchatting;

import dev.rajaopak.globalchatting.config.ConfigManager;
import dev.rajaopak.globalchatting.metrics.Metrics;
import net.md_5.bungee.api.plugin.Plugin;

public final class GlobalChatting extends Plugin {

    private static GlobalChatting instance;
    private static ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        configManager = new ConfigManager(this);

        new Metrics(this, 15432);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GlobalChatting getInstance() {
        return instance;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
