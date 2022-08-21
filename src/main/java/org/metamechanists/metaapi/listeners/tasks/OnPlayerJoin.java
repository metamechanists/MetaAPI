package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.Tasks;

import java.util.Collection;

public class OnPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get important variables
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        // Get all the unlocked tasks
        Collection<Task> tasks = TaskStorage.getAllTasks(uuid);

        // Ensure that the player has the root task, if not add it
        if (!tasks.contains(Tasks.getRootTask()) && !tasks.contains(Tasks.getRootTask())) {
            TaskStorage.addTask(uuid, Tasks.getRootTask());
            TaskStorage.completeTask(uuid, Tasks.getRootTask());
        }

        // Check if any new tasks should be unlocked
        TaskStorage.unlockNewTasks(uuid, null);
    }
}
