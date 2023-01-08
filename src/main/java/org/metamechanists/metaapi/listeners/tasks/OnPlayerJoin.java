/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;

public class OnPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        // Get important variables
        final Player player = event.getPlayer();
        final String uuid = player.getUniqueId().toString();

        // If the player or the server is missing the root task, add said root task
        TaskStorage.addRootTasksIfMissing(uuid);

        // Check this here instead of in an initialization function because we need MetaCore or another API user to load first
        // If another API user doesn't load first, there will be no root task to check
        TaskStorage.addRootTasksIfMissing(TaskStorage.SERVER_TASK_KEY);
    }
}
