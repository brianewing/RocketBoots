package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {

    public static Material getPlayerBoots(Player player) {
		if (player.getInventory().getBoots() != null) {
			final ItemStack boots = player.getInventory().getBoots();
			if (boots.getAmount() > 0) {
				return boots.getType();
			}
		}
		return null;
	}

}