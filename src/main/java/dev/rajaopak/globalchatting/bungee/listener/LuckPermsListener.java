package dev.rajaopak.globalchatting.bungee.listener;

import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;
import dev.rajaopak.globalchatting.bungee.hooks.HookManager;
import net.luckperms.api.event.user.UserFirstLoginEvent;

public class LuckPermsListener {

    public LuckPermsListener(GlobalChattingBungee globalChatting) {
        HookManager.getLuckperms().getEventBus().subscribe(globalChatting, UserFirstLoginEvent.class, this::onJoin);
    }

    public void onJoin(UserFirstLoginEvent e) {
        if (GlobalChattingBungee.getConfigManager().getConfiguration().getBoolean("cooldown-on-first-login")) {
            GlobalChattingBungee.getCooldownManager().setCooldown(e.getUniqueId(), GlobalChattingBungee.getConfigManager().getConfiguration().getInt("first-login-cooldown"));
        }
    }
}
