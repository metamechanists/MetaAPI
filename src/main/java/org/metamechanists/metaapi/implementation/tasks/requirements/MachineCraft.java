package org.metamechanists.metaapi.implementation.tasks.requirements;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metaapi.implementation.tasks.Requirement;

@Getter
public class MachineCraft extends Requirement {

    private final ItemStack item;

    public MachineCraft(int threshold, ItemStack item) {
        super(threshold);
        this.item = item;
    }
}
