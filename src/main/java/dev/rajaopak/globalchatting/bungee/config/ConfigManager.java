package dev.rajaopak.globalchatting.bungee.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Handler;

public class ConfigManager {

    private final GlobalChattingBungee plugin;
    private YamlDocument configuration;

    public ConfigManager(GlobalChattingBungee plugin) {
        this.plugin = plugin;

        try {
            loadConfig();
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot load the Config!");
            e.printStackTrace();
        }
    }

    public void loadConfig() throws IOException {
        try {
            configuration = YamlDocument.create(
                    new File(plugin.getDataFolder(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    /*UpdaterSettings.builder().setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()*/
                    UpdaterSettings.builder().setKeepAll(true).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());

            configuration.update();
            configuration.save();
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create/load plugin config! The plugin will shutdown...");
            plugin.getProxy().getPluginManager().unregisterCommands(plugin);
            plugin.getProxy().getPluginManager().unregisterListeners(plugin);
            plugin.getProxy().getScheduler().cancel(plugin);
            plugin.getProxy().getPluginManager().getPlugin("GlobalChatting").onDisable();
            Arrays.stream(plugin.getLogger().getHandlers()).forEach(Handler::close);
            throw new RuntimeException(e);
        }
    }

    public void reloadConfig() {
        try {
            configuration.reload();
            configuration.save();
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Cannot reload the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            configuration.save();
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().severe("Cannot save the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public YamlDocument getConfiguration() {
        return configuration;
    }

    public boolean isMuted() {
        return configuration.getBoolean("is-muted");
    }
}
