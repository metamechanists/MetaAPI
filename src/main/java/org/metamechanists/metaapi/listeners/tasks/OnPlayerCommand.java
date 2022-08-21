package org.metamechanists.metaapi.listeners.tasks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.ExecuteCommand;

public class OnPlayerCommand implements Listener {

    public void checkRequirement(String completer, Task task, Requirement requirement, String command) {
        // Check if the requirement is relevant to this listener
        if (requirement instanceof ExecuteCommand executeCommand) {

            // Check the requirement entity type matches
            String requirementCommand = executeCommand.getCommand();

            if (requirementCommand.equals(command)) {

                // Increment the objective
                TaskStorage.updateProgress(completer, task, requirement, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) throws NoSuchMethodException{
        //Get Important Variables
        Player player = event.getPlayer();
        String command = event.getMessage();
        String uuid = player.getUniqueId().toString();

        // Check player task
        TaskStorage.checkTask(uuid, this.getClass().getMethod("checkRequirement",
                String.class, Task.class, Requirement.class, String.class), this, command);

        // Check server task
        TaskStorage.checkTask(TaskStorage.SERVER_TASK_KEY, this.getClass().getMethod("checkRequirement",
                String.class, Task.class, Requirement.class, String.class), this, command);
    }
}
