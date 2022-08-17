package org.metamechanists.implementation.quests.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.implementation.quests.Reward;
import org.metamechanists.util.ItemUtil;

public class GiveItem extends Reward {

    private final ItemStack rewardItem;
    private final int rewardItemAmount;

    public GiveItem(ItemStack rewardItem, int rewardItemAmount) {
        this.rewardItem = rewardItem;
        this.rewardItemAmount = rewardItemAmount;
    }

    @Override
    public void rewardPlayer(Player player) {
        ItemStack giveItemStack = rewardItem.clone();
        giveItemStack.setAmount(rewardItemAmount);
        ItemUtil.giveOrDropItem(player, giveItemStack);
    }
}
