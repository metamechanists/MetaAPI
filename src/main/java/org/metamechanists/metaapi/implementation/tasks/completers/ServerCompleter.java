/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.implementation.tasks.completers;

import org.bukkit.entity.Player;
import org.metamechanists.metaapi.implementation.tasks.Completer;
import org.metamechanists.metaapi.implementation.tasks.Reward;
import org.metamechanists.metaapi.implementation.tasks.Task;

public class ServerCompleter extends Completer {

    public void grantTaskRewards(Task task, Player player) {
        // Get reward array

        Reward[] rewards = task.getRewards();

        // Go through each possible reward
        for (Reward reward : rewards) {

            // Activate reward method
            //reward.rewardPlayer(player);
        }
    }

    public void onSave() {

    }
}
