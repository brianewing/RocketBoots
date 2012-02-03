package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class RBEntityListener implements Listener {

    private RBConfiguration config;

    public RBEntityListener(RocketBoots plugin) {
        this.config = plugin.getRBConfig();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player) {
            Player player = (Player)entity;

            if(event.getCause() == DamageCause.FALL && config.playerEnabled(player)) {
                Material playerBoots = Util.getPlayerBoots(player);

                if((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                    event.setCancelled(true);
                } else if(Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) {
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