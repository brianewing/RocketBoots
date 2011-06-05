package com.disabledhamster.bukkit.rocketboots;

import com.nijiko.permissions.PermissionHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Permissions {

    private RocketBoots plugin;
    private static PermissionHandler permissions;

    public Permissions(RocketBoots plugin) {
        this.plugin = plugin;

        Plugin theYetiPermissions = plugin.getServer().getPluginManager().getPlugin("Permissions");
        if (theYetiPermissions != null) {
            permissions = ((com.nijikokun.bukkit.Permissions.Permissions) theYetiPermissions).getHandler();
        }
    }

    public boolean canUseGoldBoots(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.boots.gold");
        else
            return player.isOp();
    }

    public boolean canUseLeatherBoots(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.boots.leather");
        else
            return player.isOp();
    }

    public boolean canUseChainmailBoots(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.boots.chainmail");
        else
            return player.isOp();
    }

    public boolean canUseDiamondBoots(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.boots.diamond");
        else
            return player.isOp();
    }

    public boolean canUseIronBoots(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.boots.iron");
        else
            return player.isOp();
    }

    public boolean canUseFeather(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.feather");
        else
            return player.isOp();
    }

    public boolean canLaunchPlayers(Player player) {
        if(permissions != null)
            return permissions.has(player, "rocketboots.launchPlayers");
        else
            return player.isOp();
    }

}