package org.metamechanists.util;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Drop {
    ItemStack item;
    int percentageChance;
    int min;
    int max;

    public Drop(ItemStack output, int percentageChance, int min, int max) {
        this.item = output;
        this.percentageChance = percentageChance;
        this.min = min;
        this.max = max;
    }

    public void spawn(Location location) {
        if (NumberUtil.getRandomNumber(0, 100) <= percentageChance) {
            // Cloned so we don't set the amount of the item attribute permanently
            // Max + 1 because the function does min <= x < max while we want min <= x <= max
            ItemStack stack = item.clone();
            stack.setAmount(NumberUtil.getRandomNumber(min, max+1));
            location.getWorld().dropItemNaturally(location, stack);
        }
    }
}