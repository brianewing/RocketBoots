package com.disabledhamster.bukkit.rocketboots;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class RBPlayerListener implements Listener {

    private RBConfiguration config;
    
    // list of players that can't fly until they next sneak
    // (for when player stops in mid-air, to stop them immediately flying off the block)
    private List<Player> haltedPlayers = new ArrayList<Player>();

    public RBPlayerListener(RocketBoots plugin) {
        this.config = plugin.getRBConfig();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.isCancelled())
            return;
        
        Player player = event.getPlayer();

        if(player.isSneaking() && !haltedPlayers.contains(player)) {
            Material playerBoots = Util.getPlayerBoots(player);

            if((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player))) {
                if(config.playerEnabled(player)) {
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
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(haltedPlayers.contains(player))
            haltedPlayers.remove(player);

        if(player.isSneaking() && config.playerEnabled(player)) {
            Material playerBoots = Util.getPlayerBoots(player);

            if(Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player)) {
                Vector playerDirection = player.getLocation().getDirection();

                double speedMultiplier = config.diamondBootsSpeedMultiplier();
                double verticalSpeed = config.diamondBootsVerticalSpeed();
                
                playerDirection.multiply(speedMultiplier);
                playerDirection.setY(verticalSpeed);
                
                player.setVelocity(playerDirection);
            }  else if(Material.IRON_BOOTS.equals(playerBoots) && Permissions.canUseIronBoots(player)) {
                List<Entity> nearbyEntities = player.getNearbyEntities(12, 5, 12);

                Location playerLocation = player.getLocation();
                for(Entity entity : nearbyEntities) {
                    if(entity instanceof Player && !Permissions.canLaunchPlayers(player))
                        continue;
                    else if(!(entity instanceof LivingEntity))
                        continue;
                    
                    Location entityLocation = entity.getLocation();

                    // sk89q's Vector class makes this easy ;)
                    Vector vectorAway = entityLocation.toVector().subtract(playerLocation.toVector());

                    double verticalSpeed = config.ironBootsVerticalSpeed();
                    vectorAway.setY(verticalSpeed);

                    entity.setVelocity(vectorAway);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(event.isCancelled())
            return;

        Player player = event.getPlayer();

        if(Material.LEATHER_BOOTS.equals(Util.getPlayerBoots(player)) && Permissions.canUseLeatherBoots(player) && config.playerEnabled(player)) {
            Entity entity = event.getRightClicked();

            if(entity instanceof LivingEntity) {
                if(!(entity instanceof Player) || Permissions.canLaunchPlayers(player)) {
                    Vector entityVelocity = entity.getVelocity();

                    double launchSpeed = config.leatherBootsSpeed();
                    entityVelocity.setY(launchSpeed);
                    entity.setVelocity(entityVelocity);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if(event.isCancelled())
            return;

        if(!config.preventFlightKick())
            return;

        if(event.getReason().equals("Flying is not enabled on this server")) {
            Player player = event.getPlayer();
            
            Material playerBoots = Util.getPlayerBoots(player);
            if((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(Action.RIGHT_CLICK_AIR.equals(event.getAction())) {
            Player player = event.getPlayer();
            Material playerBoots = Util.getPlayerBoots(player);

            ItemStack itemInHand = player.getItemInHand();
            if(Material.FEATHER.equals(itemInHand.getType()) && Permissions.canUseFeather(player)) {
                if((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                    Location playerLocation = player.getLocation();
                    Block blockBelow = playerLocation.getBlock().getRelative(BlockFace.DOWN);

                    if(player.isSneaking())
                        haltedPlayers.add(player); // players in this list can't fly until they toggle sneak

                    player.sendBlockChange(blockBelow.getLocation(), Material.GLASS, (byte)0);
                    player.setVelocity(new Vector(0, 0, 0));
                    player.teleport(playerLocation);
                }
            }
        }
    }
}