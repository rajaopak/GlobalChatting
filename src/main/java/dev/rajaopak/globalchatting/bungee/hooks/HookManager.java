package dev.rajaopak.globalchatting.bungee.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;

public class HookManager {

    public static boolean isLiteBansEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("LiteBans") != null;
    }

    public static boolean isAdvancedBanEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("AdvancedBan") != null;
    }

    public static boolean isLuckPermsEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms") != null;
    }

    public static boolean isPremiumVanishEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("PremiumVanish") != null;
    }

    public static LuckPerms getLuckperms() {
        if (isLuckPermsEnabled()) {
            return LuckPermsProvider.get();
        }

        return null;
    }

}
