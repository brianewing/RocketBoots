package com.disabledhamster.bukkit.rocketboots;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class RBPlayerListener extends PlayerListener {

    private RocketBoots plugin;
    private Permissions permissions;

    public RBPlayerListener(RocketBoots plugin) {
        this.plugin = plugin;
        this.permissions = plugin.getPermissions();
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.isSneaking()) {
            Material playerBoots = Util.getPlayerBoots(player);

            if((Material.GOLD_BOOTS.equals(playerBoots) && permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && permissions.canUseChainmailBoots(player))) {
                Location playerLocation = player.getLocation();
                Vector playerDirection = playerLocation.getDirection();

                int speed = 1;
                if(Material.GOLD_BOOTS.equals(playerBoots))
                    speed = 2;
                else if(Material.CHAINMAIL_BOOTS.equals(playerBoots))
                    speed = 6;

                playerDirection.multiply(speed);
                player.setVelocity(playerDirection);
            }
        }
    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if(player.isSneaking()) {
            Material playerBoots = Util.getPlayerBoots(player);

            if(Material.DIAMOND_BOOTS.equals(playerBoots) && permissions.canUseDiamondBoots(player)) {
                Vector playerDirection = player.getLocation().getDirection();
                playerDirection.multiply(0.5);
                playerDirection.setY(0.6);
                
                player.setVelocity(playerDirection);
            }  else if(Material.IRON_BOOTS.equals(playerBoots) && permissions.canUseIronBoots(player)) {
                List<Entity> nearbyEntities = player.getNearbyEntities(12, 5, 12);

                Location playerLocation = player.getLocation();
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof Player && !permissions.canLaunchPlayers(player))
                        continue;
                    else if(!(entity instanceof LivingEntity))
                        continue;
                    
                    Location entityLocation = entity.getLocation();

                    // sk89q's Vector class makes this easy ;)
                    Vector vectorAway = entityLocation.toVector().subtract(playerLocation.toVector());
                    vectorAway.setY(1);

                    entity.setVelocity(vectorAway);
                }
            }
        }
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if(Material.LEATHER_BOOTS.equals(Util.getPlayerBoots(player)) && permissions.canUseLeatherBoots(player)) {
            Entity entity = event.getRightClicked();

            if(entity instanceof LivingEntity) {
                if(!(entity instanceof Player) || permissions.canLaunchPlayers(player)) {
                    // Launch the entity :)

                    Vector entityVelocity = entity.getVelocity();
                    entityVelocity.setY(2);
                    entity.setVelocity(entityVelocity);
                }
            }
        }
    }

}