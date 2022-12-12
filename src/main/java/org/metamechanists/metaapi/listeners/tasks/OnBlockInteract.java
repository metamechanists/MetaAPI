/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

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
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.BreakBlock;
import org.metamechanists.metaapi.implementation.tasks.requirements.PlaceBlock;

import java.util.Objects;

public class OnBlockInteract implements Listener {

    public static void checkRequirement(String completer, Task task, Requirement requirement, Block block, String eventName) {
        // Get SlimefunID
        Material type = block.getType();
        String slimefunID = BlockStorage.getLocationInfo(block.getLocation(), "id");

        Material requirementType = null;
        String requirementID = null;

        // If the requirement is a BreakBlock, cast the requirement and read the relevant variables
        // Do the same if it's a PlaceBlock
        if (requirement instanceof BreakBlock breakBlock && eventName.equals("BlockBreakEvent")) {
            requirementType = breakBlock.getType();
            requirementID = breakBlock.getSlimefunID();
        } else if (requirement instanceof PlaceBlock placeBlock && eventName.equals("BlockPlaceEvent")) {
            requirementType = placeBlock.getType();
            requirementID = placeBlock.getSlimefunID();
        }

        // Check the requirement material and slimefunID matches
        if (requirementType == type && Objects.equals(requirementID, slimefunID)) {
            // Increment the objective
            TaskStorage.updateProgress(completer, task, requirement, 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) throws NoSuchMethodException {
        //Check Requirements
        onBlockInteract(event.getEventName(), event.getBlock(), event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) throws NoSuchMethodException {
        //Check Requirements
        onBlockInteract(event.getEventName(), event.getBlock(), event.getPlayer());
    }

    public void onBlockInteract(String eventName, Block block, Player player) throws NoSuchMethodException {
        //Check Requirements for given Variables
        String uuid = player.getUniqueId().toString();

        // Check player task
        TaskStorage.checkTask(uuid, this.getClass().getMethod("checkRequirement",
                String.class, Task.class, Requirement.class, Block.class, String.class), this, block, eventName);

        // Check server task
        TaskStorage.checkTask(TaskStorage.SERVER_TASK_KEY, this.getClass().getMethod("checkRequirement",
                String.class, Task.class, Requirement.class, Block.class, String.class), this, block, eventName);
    }
}
