package org.metamechanists.util;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.config.ConfigUtil;
import org.metamechanists.implementation.RegisteredItems;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ItemUtil {
    public static ItemStack itemStackFromId(String id) {
        // If ID references a vanilla Material, return an ItemStack of that Material
        Material material = Material.getMaterial(id.toUpperCase());
        if (material != null) {
            return new ItemStack(material);
        }

        // If ID references an already-registered SlimefunItem, return an ItemStack of that SlimefunItem
        SlimefunItem item = SlimefunItem.getById(id.toUpperCase());
        if (item != null) {
            return item.getItem();
        }

        // If ID references a local SlimefunItem, return an ItemStack of that SlimefunItem
        SlimefunItem localItem = RegisteredItems.getById(id.toUpperCase());
        if (localItem != null) {
            return localItem.getItem();
        }

        // If none of the if statements have yet fired, the ID must be invalid
        return null;
    }

    public static Map<Character, ItemStack> getKeys(String configName, ConfigurationSection section) {
        Map<Character, ItemStack> keys = new HashMap<>();

        // Iterate through every key
        for (String keyString : section.getKeys(false)) {
            String value = Objects.requireNonNull(section.getString(keyString));

            // Log an error if the key is not a length of 1 (ie: not a character)
            if (keyString.length() != 1) {
                Log.mustBeOneCharacter(configName, section, keyString);
            }
            Character key = keyString.charAt(0);

            // Convert the item section to an ItemStack and add the key-item pair to the map
            ItemStack stack = ItemUtil.itemStackFromId(value);
            if (stack == null) {
                Log.invalidType(configName, section, keyString, value);
                return null;
            }
            keys.put(key, stack);
        }

        return keys;
    }

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
