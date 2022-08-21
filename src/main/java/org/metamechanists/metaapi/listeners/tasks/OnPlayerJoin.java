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

        //If the Player or the Server is Missing the Root Task, Add it
        TaskStorage.addRootTaskIfMissing(uuid);

        // Check this here instead of in an initialization function because we need MetaCore or another API user to load first
        // If another API user doesn't load first, there will be no root task to check
        TaskStorage.addRootTaskIfMissing(TaskStorage.SERVER_TASK_KEY);
    }
}
