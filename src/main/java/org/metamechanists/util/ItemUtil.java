package org.metamechanists.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


public class ItemUtil {
    public static void giveOrDropItem(Player player, ItemStack itemStack) {
        // Add items to player's inventory and get a map of the items that couldn't fit
        HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(itemStack);

        // If some items couldn't fit, drop the items at the player's location and send them a message to notify them
        if (!remainingItems.isEmpty()) {
            player.sendMessage(TextUtil.getLanguageEntry("quest.reward.no_inventory_space", player));
            for (ItemStack drop : remainingItems.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }
}
