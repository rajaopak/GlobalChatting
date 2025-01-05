package dev.rajaopak.globalchatting.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rajaopak.globalchatting.velocity.command.GlobalChatCommand;
import dev.rajaopak.globalchatting.velocity.command.GlobalChatMuteCommand;
import dev.rajaopak.globalchatting.velocity.command.ReloadCommand;
import dev.rajaopak.globalchatting.velocity.config.ConfigManager;
import dev.rajaopak.globalchatting.velocity.hooks.HookManager;
import dev.rajaopak.globalchatting.velocity.listener.LuckPermsListener;
import dev.rajaopak.globalchatting.velocity.manager.CooldownManager;
import dev.rajaopak.globalchatting.velocity.manager.ServerGroupManager;
import dev.rajaopak.globalchatting.velocity.metrics.Metrics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "globalchatting",
        name = "GlobalChatting",
        version = "1.3",
        description = "Plugin For Chatting In BungeeCord",
        authors = {"rajaopak"},
        dependencies = {
                @Dependency(id = "litebans", optional = true),
                @Dependency(id = "advancedban", optional = true),
                @Dependency(id = "luckperms", optional = true),
                @Dependency(id = "premiumvanish", optional = true)
        })
public class GlobalChattingVelocity {

    private static GlobalChattingVelocity plugin;

    private final ProxyServer proxy;
    private final Logger logger;
    private final Metrics.Factory metricsFactory;
    private final Path dataDirectory;

    // Manager
    private ConfigManager configManager;
    private CooldownManager cooldownManager;
    private ServerGroupManager serverGroupManager;

    @Inject
    public GlobalChattingVelocity(ProxyServer proxy, Logger logger, Metrics.Factory metricsFactory, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.metricsFactory = metricsFactory;
        this.dataDirectory = dataDirectory;
        plugin = this;
    }

    public static GlobalChattingVelocity getPlugin() {
        return plugin;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        configManager = new ConfigManager(this);
        cooldownManager = new CooldownManager();
        serverGroupManager = new ServerGroupManager(this);

        if (HookManager.isLuckPermsEnabled()) {
            new LuckPermsListener(this);
        }

        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta gcMeta = commandManager.metaBuilder("globalchatting")
                .aliases("gc", "gchat", "globalchat")
                .plugin(this)
                .build();
        commandManager.register(gcMeta, GlobalChatCommand.globalChatCommand(this));

        CommandMeta gcMuteMeta = commandManager.metaBuilder("globalchattingmute")
                .aliases("gcmute", "gchatmute", "globalchatmute")
                .plugin(this)
                .build();
        commandManager.register(gcMuteMeta, GlobalChatMuteCommand.globalChatMuteCommand(this));

        CommandMeta gcReloadMeta = commandManager.metaBuilder("globalchattingreload")
                .aliases("gcreload", "gchatreload", "globalchatreload")
                .plugin(this)
                .build();
        commandManager.register(gcReloadMeta, ReloadCommand.reloadCommand(this));

        metricsFactory.make(this, 15432);
    }

    public boolean reloadConfigs() {
        try {
            GlobalChattingVelocity.getPlugin().getConfigManager().reloadConfig();
            getServerGroupManager().reload();
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot reload the Config! There might be a problem with the config file.");
            e.printStackTrace();
            return false;
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public ServerGroupManager getServerGroupManager() {
        return serverGroupManager;
    }
}
