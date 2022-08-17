package org.metamechanists.metaapi.implementation.quests.requirements;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metaapi.implementation.quests.Requirement;

@Getter
public class Craft extends Requirement {

    private final ItemStack item;

    public Craft(int threshold, ItemStack item) {
        super(threshold);
        this.item = item;
    }
}
