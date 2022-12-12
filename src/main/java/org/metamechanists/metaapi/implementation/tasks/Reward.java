/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks;

import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNullableByDefault;


public abstract class Reward {

    @ParametersAreNullableByDefault
    public Reward() {}

    public void reward() {}

    public abstract void rewardPlayer(Player player);
}
