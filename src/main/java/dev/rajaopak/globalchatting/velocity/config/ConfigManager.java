package dev.rajaopak.globalchatting.velocity.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ConfigManager {

    private final GlobalChattingVelocity plugin;
    private YamlDocument configuration;

    public ConfigManager(GlobalChattingVelocity plugin) {
        this.plugin = plugin;

        try {
            loadConfig();
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot load the Config!");
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() throws IOException {
        try {
            configuration = YamlDocument.create(
                    new File(plugin.getDataDirectory().toFile(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    /*UpdaterSettings.builder().setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build()*/
                    UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("config-version"))
                            .addIgnoredRoute("1", "server-groups", '.')
                            .addIgnoredRoute("1", "globalchat", '.')
                            .addIgnoredRoute("1", "blacklist-server", '.')
                            .setKeepAll(true)
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());

            configuration.update();
            configuration.save();
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create/load plugin config! The plugin will shutdown...");
            plugin.getProxy().getPluginManager().getPlugin("globalchatting").ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
            throw new RuntimeException(e);
        }
    }

    public void reloadConfig() throws IOException {
        configuration.reload();
        configuration.save();
    }

    public void saveConfig() {
        try {
            configuration.save();
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot save the Config! There might be a problem with the config file.");
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
