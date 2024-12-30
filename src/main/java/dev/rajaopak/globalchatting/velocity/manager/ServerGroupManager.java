package dev.rajaopak.globalchatting.velocity.manager;

import dev.rajaopak.globalchatting.velocity.GlobalChattingVelocity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServerGroupManager {

    private final GlobalChattingVelocity core;
    private final HashMap<String, List<String>> serverGroups;

    public ServerGroupManager(GlobalChattingVelocity core) {
        this.core = core;
        this.serverGroups = new HashMap<>();

        loadServerGroups();
    }

    private void loadServerGroups() {
        this.core.getConfigManager().getConfiguration().getSection("server-groups").getKeys().stream().map(Objects::toString).forEach(key -> {
            if (!isGroupExists(key)) {
                serverGroups.put(key, this.core.getConfigManager().getConfiguration().getStringList("server-groups." + key));
            }
        });
    }

    public String getServerGroup(String subServer) {
        return serverGroups.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(subServer))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(subServer);
    }

    public boolean isGroupExists(String group) {
        return this.serverGroups.containsKey(group);
    }

    public void close() {
        this.serverGroups.clear();
    }

}
