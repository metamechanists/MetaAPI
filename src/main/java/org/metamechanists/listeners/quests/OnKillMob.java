package org.metamechanists.listeners.quests;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.metamechanists.implementation.quests.Quest;
import org.metamechanists.implementation.quests.QuestStorage;
import org.metamechanists.implementation.quests.Requirement;
import org.metamechanists.implementation.quests.requirements.KillMob;

import java.util.Collection;

public class OnKillMob implements Listener {

    public static void checkRequirement(Player player, Quest quest, Requirement requirement, LivingEntity entity) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof KillMob killMob) {

            // Check the requirement entity type matches
            if (killMob.getType() == entity.getType()) {

                // Increment the objective
                QuestStorage.updateProgress(player, quest, requirement, 1);
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

        // Check if each active quest involves this event
        Collection<Quest> activeQuests = QuestStorage.getActiveQuests(player);
        for (Quest quest : activeQuests) {
            for (Requirement requirement : quest.getRequirements()) {
                checkRequirement(player, quest, requirement, entity);
            }
        }
    }
}
