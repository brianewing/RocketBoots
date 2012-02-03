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
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class RBPlayerListener implements Listener {

    private final RBConfiguration config;

    // list of players that can't fly until they next sneak
    // (for when player stops in mid-air, to stop them immediately flying off the block)
    private final List<Player> haltedPlayers = new ArrayList<Player>();

    public RBPlayerListener(RBConfiguration config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.isSneaking() && !this.haltedPlayers.contains(player)) {
            final Material playerBoots = Util.getPlayerBoots(player);
            if ((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player))) {
                if (this.config.playerEnabled(player)) {
                    final Location playerLocation = player.getLocation();
                    final Vector playerDirection = playerLocation.getDirection();
                    int speed = 1;
                    if (Material.GOLD_BOOTS.equals(playerBoots)) {
                        speed = 2;
                    } else if (Material.CHAINMAIL_BOOTS.equals(playerBoots)) {
                        speed = 6;
                    }
                    playerDirection.multiply(speed);
                    player.setVelocity(playerDirection);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (this.haltedPlayers.contains(player)) {
            this.haltedPlayers.remove(player);
        }
        if (player.isSneaking() && this.config.playerEnabled(player)) {
            final Material playerBoots = Util.getPlayerBoots(player);
            if (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player)) {
                final Vector playerDirection = player.getLocation().getDirection();
                final double speedMultiplier = this.config.diamondBootsSpeedMultiplier();
                final double verticalSpeed = this.config.diamondBootsVerticalSpeed();
                playerDirection.multiply(speedMultiplier);
                playerDirection.setY(verticalSpeed);
                player.setVelocity(playerDirection);
            } else if (Material.IRON_BOOTS.equals(playerBoots) && Permissions.canUseIronBoots(player)) {
                final List<Entity> nearbyEntities = player.getNearbyEntities(12, 5, 12);
                final Location playerLocation = player.getLocation();
                for (final Entity entity : nearbyEntities) {
                    if ((entity instanceof Player) && !Permissions.canLaunchPlayers(player)) {
                        continue;
                    } else if (!(entity instanceof LivingEntity)) {
                        continue;
                    }
                    final Location entityLocation = entity.getLocation();
                    // sk89q's Vector class makes this easy ;)
                    final Vector vectorAway = entityLocation.toVector().subtract(playerLocation.toVector());
                    final double verticalSpeed = this.config.ironBootsVerticalSpeed();
                    vectorAway.setY(verticalSpeed);
                    entity.setVelocity(vectorAway);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (Material.LEATHER_BOOTS.equals(Util.getPlayerBoots(player)) && Permissions.canUseLeatherBoots(player) && this.config.playerEnabled(player)) {
            final Entity entity = event.getRightClicked();
            if (entity instanceof LivingEntity) {
                if (!(entity instanceof Player) || Permissions.canLaunchPlayers(player)) {
                    final Vector entityVelocity = entity.getVelocity();

                    final double launchSpeed = this.config.leatherBootsSpeed();
                    entityVelocity.setY(launchSpeed);
                    entity.setVelocity(entityVelocity);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!this.config.preventFlightKick()) {
            return;
        }
        if (event.getReason().equals("Flying is not enabled on this server")) {
            final Player player = event.getPlayer();
            final Material playerBoots = Util.getPlayerBoots(player);
            if ((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Action.RIGHT_CLICK_AIR.equals(event.getAction())) {
            final Player player = event.getPlayer();
            final Material playerBoots = Util.getPlayerBoots(player);
            final ItemStack itemInHand = player.getItemInHand();
            if (Material.FEATHER.equals(itemInHand.getType()) && Permissions.canUseFeather(player)) {
                if ((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                    final Location playerLocation = player.getLocation();
                    final Block blockBelow = playerLocation.getBlock().getRelative(BlockFace.DOWN);
                    if (player.isSneaking()) {
                        this.haltedPlayers.add(player); // players in this list can't fly until they toggle sneak
                    }
                    player.sendBlockChange(blockBelow.getLocation(), Material.GLASS, (byte) 0);
                    player.setVelocity(new Vector(0, 0, 0));
                    player.teleport(playerLocation);
                }
            }
        }
    }
}