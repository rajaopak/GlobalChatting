package dev.rajaopak.globalchatting.listener;

import dev.rajaopak.globalchatting.GlobalChatting;
import dev.rajaopak.globalchatting.hooks.HookManager;
import net.luckperms.api.event.user.UserFirstLoginEvent;

public class LuckPermsListener {

    public LuckPermsListener(GlobalChatting globalChatting) {
        HookManager.getLuckperms().getEventBus().subscribe(globalChatting, UserFirstLoginEvent.class, this::onJoin);
    }

    public void onJoin(UserFirstLoginEvent e) {
        if (GlobalChatting.getConfigManager().getConfiguration().getBoolean("cooldown-on-first-login")) {
            GlobalChatting.getCooldownManager().setCooldown(e.getUniqueId(), GlobalChatting.getConfigManager().getConfiguration().getInt("first-login-cooldown"));
        }
    }
}
