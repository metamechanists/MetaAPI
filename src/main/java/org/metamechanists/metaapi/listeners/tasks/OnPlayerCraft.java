package org.metamechanists.metaapi.listeners.tasks;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.requirements.Craft;

import java.util.Collection;
import java.util.List;

public class OnPlayerCraft implements Listener {

    public static void checkRequirement(Player player, Task task, Requirement requirement, ItemStack result) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof Craft craft) {

            //Get the Requirement Stack to Check With
            ItemStack requirementItem = craft.getItem().clone();
            requirementItem.setAmount(1);

            //Get the Result Stack to Check With
            ItemStack resultItem = result.clone();
            resultItem.setAmount(1);

            //Is the Required Item a Slimefun Item
            boolean isRequirementSlimefunItem = (SlimefunItem.getByItem(resultItem) != null);
            //If Both are Vanilla Items and Equal
            boolean vanillaPassed = (!isRequirementSlimefunItem && requirementItem == resultItem);
            //If Both are Slimefun Items and Equal
            boolean slimefunPassed = isRequirementSlimefunItem && SlimefunItem.getByItem(resultItem) == SlimefunItem.getByItem(requirementItem);

            if (vanillaPassed || slimefunPassed) {

                // Increment the objective
                TaskStorage.updateProgress(player, task, requirement, result.getAmount());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(CraftItemEvent event) {
        //Get Important Variables
        List<HumanEntity> players = event.getViewers();
        ItemStack result = event.getInventory().getResult();

        // Check if each active task involves this event
        for (HumanEntity entity : players) {
            Player player = (Player) entity;
            Collection<Task> activeTasks = TaskStorage.getActiveTasks(player);
            for (Task task : activeTasks) {
                for (Requirement requirement : task.getRequirements()) {
                    checkRequirement(player, task, requirement, result);
                }
            }
        }
    }
}
