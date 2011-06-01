package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;

public class RBEntityListener extends EntityListener {

    private RocketBoots plugin;
    private Permissions permissions;
    private RBConfiguration config;

    public RBEntityListener(RocketBoots plugin) {
        this.plugin = plugin;
        this.permissions = plugin.getPermissions();
        this.config = plugin.getConfig();
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player) {
            Player player = (Player)entity;

            if(event.getCause() == DamageCause.FALL && config.playerEnabled(player)) {
                Material playerBoots = Util.getPlayerBoots(player);

                if((Material.GOLD_BOOTS.equals(playerBoots) && permissions.canUseGoldBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && permissions.canUseDiamondBoots(player))) {
                    event.setCancelled(true);
                } else if(Material.CHAINMAIL_BOOTS.equals(playerBoots) && permissions.canUseChainmailBoots(player)) {
                    event.setCancelled(true);
                    Location playerLocation = player.getLocation();

                    int times = config.numberLightningStrikes();
                    boolean useRealLightning = config.strikeRealLightning();

                    for(int i=0; i<times; i++)
                        if(useRealLightning)
                            player.getWorld().strikeLightning(playerLocation);
                        else
                            player.getWorld().strikeLightningEffect(playerLocation);
                }
            }
        }
    }

}