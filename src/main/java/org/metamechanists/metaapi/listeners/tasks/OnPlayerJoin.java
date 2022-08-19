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
        //Get important Variables
        Player player = event.getPlayer();
        Collection<Task> activeTasks = TaskStorage.getActiveTasks(player);

        //Assure that the Player has the Starting Task, if not add it
        if (! activeTasks.contains(Tasks.getRootTask())) {
            TaskStorage.addTask(player, Tasks.getRootTask());
            TaskStorage.completeTask(player, Tasks.getRootTask());
        }

        //Check if any new Tasks should be Unlocked
        TaskStorage.unlockNewTasks(player, null);
    }
}
