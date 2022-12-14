/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks.requirements;

import lombok.Getter;

import org.bukkit.Material;
import org.metamechanists.metaapi.implementation.tasks.Requirement;

@Getter
public class BreakBlock extends Requirement {

    private final Material type;
    private final String slimefunID;

    public BreakBlock(int threshold, Material type, String slimefunID) {
        super(threshold);
        this.type = type;
        this.slimefunID = slimefunID;
    }
}
