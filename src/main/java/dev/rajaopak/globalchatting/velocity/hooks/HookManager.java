package dev.rajaopak.globalchatting.velocity.hooks;

import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class HookManager {

    public static boolean isLiteBansEnabled() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("litebans").isPresent();
    }

    public static boolean isAdvancedBanEnabled() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("advancedban").isPresent();
    }

    public static boolean isLuckPermsEnabled() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("luckperms").isPresent();
    }

    public static boolean isPremiumVanishEnabled() {
        return GlobalChattingVelocity.getPlugin().getProxy().getPluginManager().getPlugin("premiumvanish").isPresent();
    }

    public static LuckPerms getLuckperms() {
        if (isLuckPermsEnabled()) {
            return LuckPermsProvider.get();
        }

        return null;
    }

}
