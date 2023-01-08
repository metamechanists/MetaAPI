/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.metamechanists.metaapi.config.ResourceLoader;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.listeners.tasks.OnBlockInteract;
import org.metamechanists.metaapi.listeners.tasks.OnKillMob;
import org.metamechanists.metaapi.listeners.tasks.OnMachineCraft;
import org.metamechanists.metaapi.listeners.tasks.OnMultiBlockCraft;
import org.metamechanists.metaapi.listeners.tasks.OnPlayerCommand;
import org.metamechanists.metaapi.listeners.tasks.OnPlayerCraft;
import org.metamechanists.metaapi.listeners.tasks.OnPlayerJoin;
import org.metamechanists.metaapi.runnables.PlaytimeChecker;
import org.metamechanists.metaapi.util.Log;
import org.metamechanists.metaapi.util.PluginStorage;
import org.metamechanists.metaapi.util.TextUtil;

@SuppressWarnings("unused")
public class MetaAPI extends JavaPlugin {

    @Getter
    public static JavaPlugin instance;

    private final static int

    public static void initialize(JavaPlugin plugin) {
        instance = plugin;

        PluginStorage.initialize(instance);
        Log.initialize();
        ResourceLoader.initialize();
        TextUtil.initialize();
        TaskStorage.initialize();

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


        OnMachineCraft onMachineCraft = new OnMachineCraft();
        OnMachineCraft.fillMap();
        manager.registerEvents(onMachineCraft, instance);

        PlaytimeChecker playtimeChecker = new PlaytimeChecker();
        playtimeChecker.runTaskTimer(instance, 1, 60 * 20L);
    }
}
