package org.metamechanists.metaapi.listeners.tasks;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.requirements.BreakBlock;
import org.metamechanists.metaapi.implementation.tasks.requirements.PlaceBlock;

import java.util.Collection;
import java.util.Objects;

public class OnBlockInteract implements Listener {

    public static void checkRequirement(String eventName, Player player, Task task, Requirement requirement, Block block) {

        // Get SlimefunID
        Material type = block.getType();
        String slimefunID = BlockStorage.getLocationInfo(block.getLocation(), "id");

        Material requirementType = null;
        String requirementID = null;

        // If the requirement is a BreakBlock, cast the requirement and read the relevant variables
        // Do the same if it's a PlaceBlock
        if (requirement instanceof  BreakBlock breakBlock && eventName.equals("BlockBreakEvent")) {
            requirementType = breakBlock.getType();
            requirementID = breakBlock.getSlimefunID();
        } else if (requirement instanceof PlaceBlock placeBlock && eventName.equals("BlockPlaceEvent")) {
            requirementType = placeBlock.getType();
            requirementID = placeBlock.getSlimefunID();
        }

        // Check the requirement material and slimefunID matches
        if (requirementType == type && Objects.equals(requirementID, slimefunID)) {

            // Increment the objective
            TaskStorage.updateProgress(player, task, requirement, 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        //Check Requirements
        onBlockInteract(event.getEventName(), event.getBlock(), event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        //Check Requirements
        onBlockInteract(event.getEventName(), event.getBlock(), event.getPlayer());
    }

    public void onBlockInteract(String eventName, Block block, Player player) {

        //Check Requirements for given Variables
        Collection<Task> activeTasks = TaskStorage.getActiveTasks(player);
        for (Task task : activeTasks) {
            for (Requirement requirement : task.getRequirements()) {
                checkRequirement(eventName, player, task, requirement, block);
            }
        }
    }
}
