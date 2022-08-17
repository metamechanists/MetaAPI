package org.metamechanists.implementation.quests.requirements;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.metamechanists.implementation.quests.Requirement;

@Getter
public class KillMob extends Requirement {

    private final EntityType type;

    public KillMob(int threshold, EntityType type) {
        super(threshold);
        this.type = type;
    }
}
