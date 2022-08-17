package org.metamechanists.metaapi.listeners.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.metamechanists.metaapi.implementation.quests.Quest;
import org.metamechanists.metaapi.implementation.quests.QuestStorage;
import org.metamechanists.metaapi.implementation.Quests;

import java.util.Collection;

public class OnPlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Get important Variables
        Player player = event.getPlayer();
        Collection<Quest> activeQuests = QuestStorage.getActiveQuests(player);

        //Assure that the Player has the Starting Quest, if not add it
        if (!activeQuests.contains(Quests.getRootQuest())) {
            QuestStorage.addQuest(player, Quests.getRootQuest());
            QuestStorage.completeQuest(player,Quests.getRootQuest());
        }

        //Check if any new Quests should be Unlocked
        QuestStorage.unlockNewQuests(player, null);
    }
}
