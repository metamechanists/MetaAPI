package org.metamechanists;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.metamechanists.config.Config;
import org.metamechanists.config.ResourceLoader;
import org.metamechanists.listeners.ListenerManager;


@SuppressWarnings("unused")
public class MetaAPI extends JavaPlugin implements Listener {

    private static MetaAPI instance;

    private ListenerManager listenerManager;

    public static PluginManager getPluginManager() {
        return instance.getServer().getPluginManager();
    }

    @Override
    public void onEnable() {
        ResourceLoader.initialize();
        Config.initialize();
    }

    @Override
    public void onDisable() {

    }

    public static MetaAPI getInstance() {
        return instance;
    }

    public static ListenerManager getListenerManager() {
        return instance.listenerManager;
    }
}
