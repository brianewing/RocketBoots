package com.disabledhamster.bukkit.rocketboots;

import com.disabledhamster.bukkit.rocketboots.commands.RocketBootsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketBoots extends JavaPlugin {

    private Permissions permissions;
    private RBConfiguration config;
    
    private RBPlayerListener playerListener;
    private RBEntityListener entityListener;

    public void onDisable() {}

    public void onEnable() {
        config = new RBConfiguration(this);

        playerListener = new RBPlayerListener(this);
        entityListener = new RBEntityListener(this);

        getServer().getPluginManager().registerEvents(playerListener, this);
        getServer().getPluginManager().registerEvents(entityListener, this);

        RocketBootsCommand rocketBootsCommand = new RocketBootsCommand(this);
        getCommand("rocketboots").setExecutor(rocketBootsCommand);
        getCommand("rb").setExecutor(rocketBootsCommand);

        getLogger().info("RocketBoots v" + getDescription().getVersion() + " enabled!");
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public RBConfiguration getRBConfig() {
        return config;
    }

}