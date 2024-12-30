package dev.rajaopak.globalchatting.velocity.manager;

import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private final HashMap<UUID, Long> cooldowns;

    public CooldownManager() {
        cooldowns = new HashMap<>();
    }

    public void setCooldown(UUID uuid, long seconds) {
        cooldowns.put(uuid, System.currentTimeMillis() + (seconds * 1000));
    }

    public long getCooldown(UUID uuid) {
        if (cooldowns.containsKey(uuid)) {
            return (cooldowns.get(uuid) - System.currentTimeMillis()) / 1000;
        }
        return 0;
    }

    public boolean isCooldown(UUID uuid) {
        if (!GlobalChattingVelocity.getPlugin().getProxy().getPlayer(uuid).get().hasPermission("globalchatting.bypass-cooldown")) {
            if (cooldowns.containsKey(uuid)) {
                if (cooldowns.get(uuid) > System.currentTimeMillis()) {
                    return true;
                } else {
                    removeCooldown(uuid);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void removeCooldown(UUID uuid) {
        cooldowns.remove(uuid);
    }

    public void clearCooldowns() {
        cooldowns.clear();
    }

    public HashMap<UUID, Long> getCooldowns() {
        return cooldowns;
    }

}
