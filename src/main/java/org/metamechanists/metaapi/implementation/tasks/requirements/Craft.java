/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks.requirements;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.metaapi.implementation.tasks.Requirement;

@Getter
public class Craft extends Requirement {

    private final ItemStack item;

    public Craft(int threshold, ItemStack item) {
        super(threshold);
        this.item = item;
    }
}
