package com.disabledhamster.bukkit.rocketboots;

import com.disabledhamster.bukkit.rocketboots.commands.RocketBootsCommand;
import java.util.logging.Logger;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketBoots extends JavaPlugin {

    private Permissions permissions;
    private RBConfiguration config;
    
    private static final Logger logger = Logger.getLogger("RocketBoots");

    private RBPlayerListener playerListener;
    private RBEntityListener entityListener;

    public void onDisable() {}

    public void onEnable() {
        permissions = new Permissions(this);
        config = new RBConfiguration(this);

        playerListener = new RBPlayerListener(this);
        entityListener = new RBEntityListener(this);

        getServer().getPluginManager().registerEvent(Type.PLAYER_TOGGLE_SNEAK, playerListener, Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_MOVE, playerListener, Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT_ENTITY, playerListener, Priority.Monitor, this);
        getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);

        getServer().getPluginManager().registerEvent(Type.PLAYER_KICK, playerListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Normal, this);

        RocketBootsCommand rocketBootsCommand = new RocketBootsCommand(this);
        getCommand("rocketboots").setExecutor(rocketBootsCommand);
        getCommand("rb").setExecutor(rocketBootsCommand);

        logger.info("RocketBoots v" + getDescription().getVersion() + " enabled!");
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public RBConfiguration getConfig() {
        return config;
    }

    public Logger getLogger() {
        return logger;
    }

}