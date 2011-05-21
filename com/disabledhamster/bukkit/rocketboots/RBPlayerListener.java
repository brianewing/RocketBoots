package com.disabledhamster.bukkit.rocketboots;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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

            if(Material.GOLD_BOOTS.equals(playerBoots) || Material.CHAINMAIL_BOOTS.equals(playerBoots)) {
                Location playerLocation = player.getLocation();
                Vector playerDirection = playerLocation.getDirection();

                int speed = 1;
                if(Material.GOLD_BOOTS.equals(playerBoots) && permissions.canUseGoldBoots(player))
                    speed = 2;
                else if(Material.CHAINMAIL_BOOTS.equals(playerBoots) && permissions.canUseChainmailBoots(player))
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
                List<Entity> nearbyEntities = player.getNearbyEntities(10, 3, 10);

                Location playerLocation = player.getLocation();
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof Player && !permissions.canLaunchPlayers(player))
                        continue;
                    
                    Location entityLocation = entity.getLocation();

                    /*
                     * This calculation is incorrect. It's supposed to calculate
                     * the Vector going "away" from the player, but the result
                     * is similar to what it's supposed to do, anyway, so I'm
                     * happy with it.
                     */

                    double dX = entityLocation.getX() - playerLocation.getX();
                    double dZ = entityLocation.getZ() - playerLocation.getZ();
                    double angle = Math.atan(dZ / dX);
                    double angleDegrees = Math.toDegrees(angle);

                    int speed = 2;

                    double speedX = speed * Math.cos(angleDegrees);
                    double speedZ = speed * Math.sin(angleDegrees);

                    if(dX < 0)
                        speedX *= -1.0;
                    if(dZ < 0)
                        speedZ *= -1.0;

                    Vector entityVelocity = entity.getVelocity();
                    entityVelocity.setX(speedX);
                    entityVelocity.setZ(speedZ);
                    entityVelocity.setY(0.7);
                    entity.setVelocity(entityVelocity);
                }
            }
        }
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if(Material.LEATHER_BOOTS.equals(Util.getPlayerBoots(player)) && permissions.canUseLeatherBoots(player)) {
            Entity entity = event.getRightClicked();

            if(!(entity instanceof Player) || permissions.canLaunchPlayers(player)) {
                // Launch the entity :)

                Vector entityVelocity = entity.getVelocity();
                entityVelocity.setY(2);
                entity.setVelocity(entityVelocity);
            }
        }
    }

}