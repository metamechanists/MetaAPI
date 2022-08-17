package org.metamechanists.implementation.quests.requirements;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.implementation.quests.Requirement;

@Getter
public class MultiBlockCraft extends Requirement {

    private final ItemStack item;

    public MultiBlockCraft(int threshold, ItemStack item) {
        super(threshold);
        this.item = item;
    }
}
