/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks;

import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public abstract class Requirement implements Listener {

    private final int threshold;

    public Requirement(int threshold) {
        this.threshold = threshold;
    }
}
