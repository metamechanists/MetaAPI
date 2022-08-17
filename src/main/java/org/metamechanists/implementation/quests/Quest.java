package org.metamechanists.implementation.quests;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class Quest {

    private final String id;
    private final String name;
    private final String[] precursors;
    private final Requirement[] requirements;
    private final Reward[] rewards;

    public Quest(String id, String name, String[] precursors, Requirement[] requirements, Reward[] rewards) {
        this.id = id;
        this.name = name;
        this.precursors = precursors;
        this.requirements = requirements;
        this.rewards = rewards;
    }
}
