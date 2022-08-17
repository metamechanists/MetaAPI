package org.metamechanists.metaapi.implementation.quests.rewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.metamechanists.metaapi.implementation.quests.Reward;
import org.metamechanists.metaapi.util.TextUtil;

public class RunCommand extends Reward {

    public String command;

    public RunCommand(String command) {
        this.command = command;
    }

    @Override
    public void rewardPlayer(Player player) {
        // Run the command as console
        String fullCommand = TextUtil.fillPlaceholders(command, "player", player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), fullCommand);
    }
}
