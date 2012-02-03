package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.entity.Player;

public class Permissions {

    public static boolean canUseGoldBoots(Player player) {
        return player.hasPermission("rocketboots.boots.gold");
    }

    public static boolean canUseLeatherBoots(Player player) {
        return player.hasPermission("rocketboots.boots.leather");
    }

    public static boolean canUseChainmailBoots(Player player) {
        return player.hasPermission("rocketboots.boots.chainmail");
    }

    public static boolean canUseDiamondBoots(Player player) {
        return player.hasPermission("rocketboots.boots.diamond");
    }

    public static boolean canUseIronBoots(Player player) {
        return player.hasPermission("rocketboots.boots.iron");
    }

    public static boolean canUseFeather(Player player) {
        return player.hasPermission("rocketboots.feather");
    }

    public static boolean canLaunchPlayers(Player player) {
        return player.hasPermission("rocketboots.launchPlayers");
    }

}