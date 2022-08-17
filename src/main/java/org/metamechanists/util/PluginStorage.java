package org.metamechanists.util;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginStorage {

    private static JavaPlugin plugin;

    public static void initialize(JavaPlugin _plugin) {
        plugin = _plugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
