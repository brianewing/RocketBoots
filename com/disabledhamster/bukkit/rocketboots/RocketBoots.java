package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.plugin.java.JavaPlugin;

import com.disabledhamster.bukkit.rocketboots.commands.RocketBootsCommand;

public class RocketBoots extends JavaPlugin {

    private Permissions permissions;
    private RBConfiguration config;

    private RBPlayerListener playerListener;
    private RBEntityListener entityListener;

    @Override
    public void onDisable() {
        this.getLogger().info("RocketBoots v" + this.getDescription().getVersion() + " disabled!");
    }

    @Override
    public void onEnable() {
        this.config = new RBConfiguration(this);

        this.playerListener = new RBPlayerListener(this);
        this.entityListener = new RBEntityListener(this);

        this.getServer().getPluginManager().registerEvents(this.playerListener, this);
        this.getServer().getPluginManager().registerEvents(this.entityListener, this);

        final RocketBootsCommand rocketBootsCommand = new RocketBootsCommand(this);
        this.getCommand("rocketboots").setExecutor(rocketBootsCommand);
        this.getCommand("rb").setExecutor(rocketBootsCommand);

        this.getLogger().info("RocketBoots v" + this.getDescription().getVersion() + " enabled!");
    }

    public Permissions getPermissions() {
        return this.permissions;
    }

    public RBConfiguration getRBConfig() {
        return this.config;
    }

}