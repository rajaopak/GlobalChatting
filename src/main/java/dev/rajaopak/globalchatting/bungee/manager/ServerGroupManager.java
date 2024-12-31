package dev.rajaopak.globalchatting.bungee.manager;

import dev.rajaopak.globalchatting.bungee.GlobalChattingBungee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServerGroupManager {

    private final HashMap<String, List<String>> serverGroups;

    public ServerGroupManager() {
        this.serverGroups = new HashMap<>();

        loadServerGroups();
    }

    private void loadServerGroups() {
        if (GlobalChattingBungee.getConfigManager().getConfiguration().getSection("server-groups") == null) return;

        GlobalChattingBungee.getConfigManager().getConfiguration().getSection("server-groups").getKeys().stream().map(Objects::toString).forEach(key -> {
            if (!isGroupExists(key)) {
                serverGroups.put(key, GlobalChattingBungee.getConfigManager().getConfiguration().getStringList("server-groups." + key));
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

    public void reload() {
        close();
        loadServerGroups();
    }

    public void close() {
        this.serverGroups.clear();
    }

}
