/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.KillMob;

public class OnKillMob implements Listener {

    public static void checkRequirement(String completer, Task task, Requirement requirement, LivingEntity entity) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof KillMob killMob) {

            // Check the requirement entity type matches
            if (killMob.getType() == entity.getType()) {

                // Increment the objective
                TaskStorage.updateProgress(completer, task, requirement, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKillMob(EntityDeathEvent event) throws NoSuchMethodException {
        // Get some important variables
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();

        // Make sure that the player killed the entity
        if (player == null) { return; }
        String uuid = player.getUniqueId().toString();

        // Check player task
        TaskStorage.checkTask(uuid, this.getClass().getMethod(
                "checkRequirement", String.class, Task.class, Requirement.class, LivingEntity.class), this, entity);

        // Check server task
        TaskStorage.checkTask(TaskStorage.SERVER_TASK_KEY, this.getClass().getMethod(
                "checkRequirement", String.class, Task.class, Requirement.class, LivingEntity.class), this, entity);
    }
}
