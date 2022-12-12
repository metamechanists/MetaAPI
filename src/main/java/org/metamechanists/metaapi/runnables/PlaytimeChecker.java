/*
 * Copyright (C) 2022 Idra - All Rights Reserved
 */

package org.metamechanists.metaapi.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.metaapi.implementation.tasks.Requirement;
import org.metamechanists.metaapi.implementation.tasks.Task;
import org.metamechanists.metaapi.implementation.tasks.TaskStorage;
import org.metamechanists.metaapi.implementation.tasks.requirements.Playtime;

public class PlaytimeChecker extends BukkitRunnable {

    public static void checkRequirement(String completer, Task task, Requirement requirement, int timePlayed) {

        // Check if the requirement is relevant to this listener
        if (requirement instanceof Playtime playtime) {

            //Get the Required time
            int requiredTime = playtime.getTimeNeeded();

            //Check if the Player has enough Playtime
            if (timePlayed >= requiredTime) {

                // Increment the objective
                TaskStorage.setProgress(completer, task, requirement, 1);
            }
        }
    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Player player : world.getPlayers()) {
                //Get Important Variables
                String uuid = player.getUniqueId().toString();
                int timePlayed = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;

                //Check Player Task
                try {
                    TaskStorage.checkTask(uuid, this.getClass().getMethod(
                            "checkRequirement", String.class, Task.class, Requirement.class, int.class), this, timePlayed);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
