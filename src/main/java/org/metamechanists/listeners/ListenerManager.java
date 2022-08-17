package org.metamechanists.listeners;

import org.bukkit.plugin.PluginManager;
import org.metamechanists.MetaAPI;
import org.metamechanists.listeners.quests.OnBlockInteract;
import org.metamechanists.listeners.quests.OnKillMob;
import org.metamechanists.listeners.quests.OnMultiBlockCraft;
import org.metamechanists.listeners.quests.OnPlayerCommand;
import org.metamechanists.listeners.quests.OnPlayerCraft;
import org.metamechanists.listeners.quests.OnPlayerJoin;

public class ListenerManager {

    public ListenerManager() {

        MetaAPI instance = MetaAPI.getInstance();
        PluginManager manager = MetaAPI.getPluginManager();

        manager.registerEvents(new OnBlockInteract(), instance);
        manager.registerEvents(new OnKillMob(), instance);
        manager.registerEvents(new OnMultiBlockCraft(), instance);
        manager.registerEvents(new OnPlayerCommand(), instance);
        manager.registerEvents(new OnPlayerCraft(), instance);
        manager.registerEvents(new OnPlayerJoin(), instance);
    }
}
