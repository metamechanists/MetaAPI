package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;

public class OnPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get important variables
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        TaskStorage.addRootTaskIfMissing(uuid);
    }
}
