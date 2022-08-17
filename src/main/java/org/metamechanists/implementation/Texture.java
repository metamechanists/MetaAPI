package org.metamechanists.implementation;

import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import org.bukkit.inventory.ItemStack;

public enum Texture {

    STONE_FRAME("8102c419e1a5af50fc0ff3e9d112d9ce9e79949a8183cc45ef926b0baf367e69");

    private final String texture;

    Texture(String t) {
        texture = t;
    }

    public ItemStack asItemStack() {
        return PlayerHead.getItemStack(PlayerSkin.fromHashCode(texture));
    }
}
