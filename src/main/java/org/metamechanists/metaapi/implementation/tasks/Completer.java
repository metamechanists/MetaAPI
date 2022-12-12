/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks;

import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNullableByDefault;

public class Completer {

    @ParametersAreNullableByDefault
    public Completer() {}

    public void onSave() {}

    public void onSave(Player player) {}

    public void grantTaskRewards(Task task) {}

    public void grantTaskRewards(Task task, Player player) {}
}
