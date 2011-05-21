package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketBoots extends JavaPlugin {

    private Permissions permissions;

    private RBPlayerListener playerListener;
    private RBEntityListener entityListener;

    public void onDisable() {}

    public void onEnable() {
        permissions = new Permissions(this);
        
        playerListener = new RBPlayerListener(this);
        entityListener = new RBEntityListener(this);
        
        getServer().getPluginManager().registerEvent(Type.PLAYER_TOGGLE_SNEAK, playerListener, Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_MOVE, playerListener, Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT_ENTITY, playerListener, Priority.Monitor, this);

        getServer().getPluginManager().registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);

        System.out.println("RocketBoots v" + getDescription().getVersion() + " enabled");
    }

    public Permissions getPermissions() {
        return permissions;
    }

}