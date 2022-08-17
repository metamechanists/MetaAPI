package org.metamechanists.util;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginStorage {

    private static JavaPlugin plugin;
    private static SlimefunAddon addon;

    public static void initialize(JavaPlugin _plugin) {
        plugin = _plugin;
        addon = (SlimefunAddon) _plugin;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static SlimefunAddon getAddon() {
        return addon;
    }
}
