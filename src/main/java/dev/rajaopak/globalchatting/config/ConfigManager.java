package dev.rajaopak.globalchatting.config;

import dev.rajaopak.globalchatting.GlobalChatting;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private GlobalChatting plugin;
    private File configFile;
    private Configuration configuration;

    private final List<String> defaultConfig = Arrays.asList(
            "#GlobalChatting Configuration.",
            " ",
            "#mute the global chat so no one can talk. (only the player that has the permission can talk)",
            "is-muted: false",
            " ",
            "globalchat:",
            "  default:",
            " ",
            "    #permission to use this channel.",
            "    #if permission is empty, everyone can use this channel.",
            "    permission: \"globalchatting.default\"",
            " ",
            "    #this is the format of the message that will be sent to the global chat.",
            "    #placeholder: {player}, {message}, {time}, {date}.",
            "    #if format is empty, it will use the default format of plugin has.",
            "    format: \"&8[&7{date}, {time}&8] &8[&aGlobal&8] &r{player}&7: &r{message}\"",
            " ",
            "    #this settings make you whether use color or not.",
            "    #if you leave this empty or not set this, it will return false.",
            "    useColor: false",
            " ",
            "    #this setting will determine which one will be selected first.",
            "    #the higher number will be chosen first.",
            "    #if you leave this empty or not set this, it will return 0.",
            "    priority: 1",
            " ",
            "    #this setting will cooldown the message that you send to the global chat.",
            "    #if you want to disable this, set this to 0.",
            "    cooldown: 60",
            " ",
            "  staff:",
            "    permission: \"globalchatting.staff\"",
            "    format: \"&8[&7{date}, {time}&8] &8[&aGlobal&8] &8[&bStaff&8] &r{player}&7: &r{message}\"",
            "    useColor: true",
            "    priority: 5",
            "    cooldown: 0");

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
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            InputStream in = plugin.getResourceAsStream("config.yml");
            Files.copy(in, configFile.toPath());
        }

        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public void reloadConfig() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Cannot reload the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Cannot save the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public File getConfigFile() {
        return configFile;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean isMuted() {
        return configuration.getBoolean("is-muted");
    }
}
