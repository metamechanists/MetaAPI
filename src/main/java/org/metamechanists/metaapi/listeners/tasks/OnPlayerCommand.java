package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.requirements.ExecuteCommand;

import java.util.Collection;

public class OnPlayerCommand implements Listener {

    private void checkRequirement(Player player, Task task, Requirement requirement, String command) {
        // Check if the requirement is relevant to this listener
        if (requirement instanceof ExecuteCommand executeCommand) {

            // Check the requirement entity type matches
            String requirementCommand = executeCommand.getCommand();

            if (requirementCommand.equals(command)) {

                // Increment the objective
                TaskStorage.updateProgress(player, task, requirement, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        //Get Important Variables
        Player player = event.getPlayer();
        String command = event.getMessage();

        Collection<Task> activeTasks = TaskStorage.getActiveTasks(player);
        for (Task task : activeTasks) {
            for (Requirement requirement : task.getRequirements()) {
                checkRequirement(player, task, requirement, command);
            }
        }
    }
}
