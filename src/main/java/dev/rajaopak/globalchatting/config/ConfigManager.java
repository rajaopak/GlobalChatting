package dev.rajaopak.globalchatting.config;

import dev.rajaopak.globalchatting.GlobalChatting;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private GlobalChatting plugin;
    private File configFile;
    private Configuration configuration;

    public ConfigManager(GlobalChatting plugin) {
        this.plugin = plugin;

        try {
            loadConfig();
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot load the Config!");
            e.printStackTrace();
        }

    }

    public void loadConfig() throws IOException {
        configFile = new File(plugin.getDataFolder().getPath(), "config.yml");

        if (!configFile.exists()) {
            configFile.mkdirs();
        }

        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public File getConfigFile() {
        return configFile;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
