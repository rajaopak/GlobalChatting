package dev.rajaopak.globalchatting.velocity.listener;

import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;
import dev.rajaopak.globalchatting.velocity.hooks.HookManager;
import net.luckperms.api.event.user.UserFirstLoginEvent;

public class LuckPermsListener {

    public LuckPermsListener(GlobalChattingVelocity globalChatting) {
        HookManager.getLuckperms().getEventBus().subscribe(globalChatting, UserFirstLoginEvent.class, this::onJoin);
    }

    public void onJoin(UserFirstLoginEvent e) {
        if (GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getBoolean("cooldown-on-first-login")) {
            GlobalChattingVelocity.getPlugin().getCooldownManager().setCooldown(e.getUniqueId(), GlobalChattingVelocity.getPlugin().getConfigManager().getConfiguration().getInt("first-login-cooldown"));
        }
    }
}
