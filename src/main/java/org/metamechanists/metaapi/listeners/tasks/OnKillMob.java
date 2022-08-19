package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.requirements.KillMob;

import java.util.Collection;

public class OnKillMob implements Listener {

    public static void checkRequirement(Player player, Task task, Requirement requirement, LivingEntity entity) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof KillMob killMob) {

            // Check the requirement entity type matches
            if (killMob.getType() == entity.getType()) {

                // Increment the objective
                TaskStorage.updateProgress(player, task, requirement, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKillMob(EntityDeathEvent event) {
        // Get some important variables
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();

        // Make sure that the player killed the entity
        if (player == null) { return; }

        // Check if each active task involves this event
        Collection<Task> activeTasks = TaskStorage.getActiveTasks(player);
        for (Task task : activeTasks) {
            for (Requirement requirement : task.getRequirements()) {
                checkRequirement(player, task, requirement, entity);
            }
        }
    }
}
