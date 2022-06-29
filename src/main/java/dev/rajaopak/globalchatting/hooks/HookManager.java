package dev.rajaopak.globalchatting.hooks;

import net.md_5.bungee.api.ProxyServer;

public class HookManager {

    public static boolean isLiteBansEnable() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("LiteBans") != null;
    }

    /*public static boolean isAdvancedBanEnable() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("AdvancedBan") != null;
    }*/

}
