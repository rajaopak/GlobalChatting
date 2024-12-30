package dev.rajaopak.globalchatting.velocity.hooks;

import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class HookManager {

    public static boolean isLiteBansEnable() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("LiteBans").isPresent();
    }

    public static boolean isAdvancedBanEnable() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("AdvancedBan").isPresent();
    }

    public static boolean isLuckPermsEnable() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("LuckPerms").isPresent();
    }

    public static LuckPerms getLuckperms() {
        if (isLuckPermsEnable()) {
            return LuckPermsProvider.get();
        }

        return null;
    }

}
