package org.metamechanists.implementation.quests;

import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNullableByDefault;


public abstract class Reward {

    @ParametersAreNullableByDefault
    public Reward() {}

    public abstract void rewardPlayer(Player player);
}
