/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.listeners.tasks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.Craft;

import java.util.List;

public class OnPlayerCraft implements Listener {

    public static void checkRequirement(final String completer, final Task task, final Requirement requirement, final ItemStack result) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof Craft craft) {

            ItemStack requirementItem = craft.getItem().clone();
            ItemStack resultItem = result.clone();

            // Allows us to directly compare both ItemStacks
            requirementItem.setAmount(1);
            resultItem.setAmount(1);

            boolean isRequirementSlimefunItem = (SlimefunItem.getByItem(resultItem) != null);
            boolean bothVanillaAndTheSame = (!isRequirementSlimefunItem && requirementItem == resultItem);
            boolean bothSlimefunAndTheSame = isRequirementSlimefunItem && SlimefunItem.getByItem(resultItem) == SlimefunItem.getByItem(requirementItem);

            if (bothVanillaAndTheSame || bothSlimefunAndTheSame) {
                TaskStorage.updateProgress(completer, task, requirement, result.getAmount());
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(final CraftItemEvent event) throws NoSuchMethodException {
        // Get important variables
        final List<HumanEntity> players = event.getViewers();
        final ItemStack result = event.getInventory().getResult();

        // For each online player
        for (HumanEntity entity : players) {
            final Player player = (Player) entity;
            final String uuid = player.getUniqueId().toString();

            // Check if any of the active tasks for said player involve this event
            TaskStorage.checkTask(uuid, this.getClass().getMethod(
                    "checkRequirement", String.class, Task.class, Requirement.class, ItemStack.class), this, result);
        }

        // Check server task
        TaskStorage.checkTask(TaskStorage.SERVER_TASK_KEY, this.getClass().getMethod(
                "checkRequirement", String.class, Task.class, Requirement.class, ItemStack.class), this, result);
    }
}
