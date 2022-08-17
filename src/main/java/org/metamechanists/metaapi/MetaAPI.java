package org.metamechanists.metaapi;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metaapi.config.Config;
import org.metamechanists.metaapi.config.ResourceLoader;
import org.metamechanists.metaapi.listeners.quests.OnBlockInteract;
import org.metamechanists.metaapi.listeners.quests.OnKillMob;
import org.metamechanists.metaapi.listeners.quests.OnMultiBlockCraft;
import org.metamechanists.metaapi.listeners.quests.OnPlayerCommand;
import org.metamechanists.metaapi.listeners.quests.OnPlayerCraft;
import org.metamechanists.metaapi.listeners.quests.OnPlayerJoin;
import org.metamechanists.metaapi.util.Log;
import org.metamechanists.metaapi.util.PluginStorage;
import org.metamechanists.metaapi.util.TextUtil;


@SuppressWarnings("unused")
public class MetaAPI {

    @Getter
    public static JavaPlugin instance;

    public static void initialize(JavaPlugin plugin) {
        instance = plugin;
        PluginStorage.initialize(instance);
        Log.initialize();
        ResourceLoader.initialize();
        Config.initialize();
        TextUtil.initialise();
        addTaskListeners();
    }

    public static void addTaskListeners() {
        PluginManager manager = instance.getServer().getPluginManager();

        manager.registerEvents(new OnBlockInteract(), instance);
        manager.registerEvents(new OnKillMob(), instance);
        manager.registerEvents(new OnMultiBlockCraft(), instance);
        manager.registerEvents(new OnPlayerCommand(), instance);
        manager.registerEvents(new OnPlayerCraft(), instance);
        manager.registerEvents(new OnPlayerJoin(), instance);
    }
}
