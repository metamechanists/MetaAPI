package org.metamechanists.metaapi.listeners.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import org.metamechanists.metaapi.implementation.quests.Quest;
import org.metamechanists.metaapi.implementation.quests.QuestStorage;
import org.metamechanists.metaapi.implementation.quests.Requirement;
import org.metamechanists.metaapi.implementation.quests.requirements.ExecuteCommand;

import java.util.Collection;

public class OnPlayerCommand implements Listener {

    private void checkRequirement(Player player, Quest quest, Requirement requirement, String command) {
        // Check if the requirement is relevant to this listener
        if (requirement instanceof ExecuteCommand executeCommand) {

            // Check the requirement entity type matches
            String requirementCommand = executeCommand.getCommand();

            if (requirementCommand.equals(command)) {

                // Increment the objective
                QuestStorage.updateProgress(player, quest, requirement, 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        //Get Important Variables
        Player player = event.getPlayer();
        String command = event.getMessage();

        Collection<Quest> activeQuests = QuestStorage.getActiveQuests(player);
        for (Quest quest : activeQuests) {
            for (Requirement requirement : quest.getRequirements()) {
                checkRequirement(player, quest, requirement, command);
            }
        }
    }
}
