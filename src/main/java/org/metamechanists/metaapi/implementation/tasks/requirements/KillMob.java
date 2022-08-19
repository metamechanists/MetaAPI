package org.metamechanists.metaapi.implementation.tasks.requirements;

import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.metamechanists.metaapi.implementation.tasks.Requirement;

@Getter
public class KillMob extends Requirement {

    private final EntityType type;

    public KillMob(int threshold, EntityType type) {
        super(threshold);
        this.type = type;
    }
}
