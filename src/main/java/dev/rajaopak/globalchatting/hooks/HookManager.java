package dev.rajaopak.globalchatting.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;

public class HookManager {

    public static boolean isLiteBansEnable() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("LiteBans") != null;
    }

    public static boolean isAdvancedBanEnable() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("AdvancedBan") != null;
    }

    public static boolean isLuckPermsEnable() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms") != null;
    }

    public static LuckPerms getLuckperms() {
        if (isLuckPermsEnable()) {
            return LuckPermsProvider.get();
        }

        return null;
    }

}
