/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.listeners.tasks;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.MultiBlockInteractionHandler;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;

import lombok.SneakyThrows;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.MultiBlockCraft;
import org.metamechanists.metaapi.util.Log;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnMultiBlockCraft implements Listener {

    private static final BlockFace[] possibleFaces = {
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    private static Inventory findOutputChestInventory(@Nonnull Block dispenser) {

        // Loop through every face of the dispenser
        for (BlockFace face : possibleFaces) {
            Block potentialOutputChest = dispenser.getRelative(face);
            SlimefunItem slimefunItem = BlockStorage.check(potentialOutputChest);

            // Check if the target block is an output chest
            if (slimefunItem instanceof OutputChest) {

                // Return the chest's inventory
                BlockState state = PaperLib.getBlockState(potentialOutputChest, false).getState();
                if (state instanceof Chest chest) {
                    return chest.getInventory();
                }
            }
        }

        // No inventory was found
        return null;
    }

    private static Inventory[] findOutputInventories(Block block, Dispenser dispenser) {
        // If the output chest is filled, items may go to the dispenser, hence why we need to return both the dispenser and output chest
        Inventory dispenserInventory = dispenser.getInventory();
        Inventory outputChestInventory = findOutputChestInventory(block);

        // If the output chest does not exist, the only inventory available is the dispenser
        if (outputChestInventory == null) {
            return new Inventory[] { dispenserInventory };
        }

        // If the output chest does exist, both the dispenser and output chest are available
        return new Inventory[] { dispenserInventory, outputChestInventory };
    }

    private static List<ItemStack> getItems(Block block, Dispenser dispenser, boolean complete, Player player, MultiBlock multiBlock, Block mBlock) {
        // Read the items in the output inventory into a list
        Inventory[] inventories = findOutputInventories(block, dispenser);
        List<ItemStack> items = new ArrayList<>();
        for (Inventory inventory : inventories) {
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null) {
                    items.add(itemStack.clone());
                }
            }
        }
        if (complete) {
            multiBlock.getSlimefunItem().callItemHandler(
                    MultiBlockInteractionHandler.class,
                    handler -> handler.onInteract(player, multiBlock, mBlock));
        }
        return items;
    }

    @SneakyThrows
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMultiBlockCraft(MultiBlockInteractEvent event) {

        // Cancel event so Slimefun can't process it
        event.setCancelled(true);

        // Get some important variables
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        MultiBlock multiBlock = event.getMultiBlock();
        Block block = event.getClickedBlock();
        Block possibleDispenser = block.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        // If the block is not a dispenser, we shouldn't attempt to craft anything
        if (!(state instanceof Dispenser dispenser)) {
            return;
        }

        // Read the items in the output inventory into a list, let the multiblock do its thing, then read the items again
        List<ItemStack> before = getItems(block, dispenser, true, player, multiBlock, block);
        List<ItemStack> after = getItems(block, dispenser, false, null, null, null);

        // Remove all the items from the list that were already there before
        Map<ItemStack, Integer> beforeTotal = getMappedContents(before);
        Map<ItemStack, Integer> afterTotal = getMappedContents(after);

        // TODO refactor this mess
        for (ItemStack item : beforeTotal.keySet()) {
            if (afterTotal.containsKey(item)) {
                if (!afterTotal.get(item).equals(beforeTotal.get(item))) {
                    int delta = afterTotal.get(item) - beforeTotal.get(item);
                    Log.info(item + " " + delta);
                    if (delta > 0) {
                        Collection<Task> activeTasks = TaskStorage.getActiveTasks(uuid);
                        for (Task task : activeTasks) {
                            for (Requirement requirement : task.getRequirements()) {
                                if (requirement instanceof MultiBlockCraft multiBlockCraft) {
                                    if (multiBlockCraft.getItem() == item) {
                                        TaskStorage.updateProgress(uuid, task, requirement, delta);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<ItemStack, Integer> getMappedContents(List<ItemStack> itemStacks) {
        Map<ItemStack, Integer> map = new HashMap<>();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            ItemStack key = itemStack.clone();
            key.setAmount(1);
            if (map.containsKey(key)) {
                map.replace(key,map.get(key) + itemStack.getAmount());
            } else {
                map.put(key, itemStack.getAmount());
            }
        }
        Log.info("Done Mapping");
        return map;
    }
}
