package org.metamechanists.metaapi.implementation.tasks.requirements;

import lombok.Getter;
import org.metamechanists.metaapi.implementation.tasks.Requirement;

@Getter
public class Playtime extends Requirement {

    private final int timeNeeded;

    public Playtime(int threshold, int timeNeeded) {
        super(threshold);
        this.timeNeeded = timeNeeded;
    }
}
