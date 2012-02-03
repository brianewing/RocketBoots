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

    private final RBConfiguration config;

    public RBEntityListener(RBConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            if ((event.getCause() == DamageCause.FALL) && this.config.playerEnabled(player)) {
                final Material playerBoots = Util.getPlayerBoots(player);
                if ((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                    event.setCancelled(true);
                } else if (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) {
                    event.setCancelled(true);
                    final Location playerLocation = player.getLocation();
                    final int times = this.config.numberLightningStrikes();
                    final boolean useRealLightning = this.config.strikeRealLightning();
                    for (int i = 0; i < times; i++) {
                        if (useRealLightning) {
                            player.getWorld().strikeLightning(playerLocation);
                        } else {
                            player.getWorld().strikeLightningEffect(playerLocation);
                        }
                    }
                }
            }
        }
    }

}