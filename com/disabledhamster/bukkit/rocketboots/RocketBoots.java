package com.disabledhamster.bukkit.rocketboots;

import org.bukkit.plugin.java.JavaPlugin;

public class RocketBoots extends JavaPlugin {

    @Override
    public void onDisable() {
        this.getLogger().info("RocketBoots v" + this.getDescription().getVersion() + " disabled!");
    }

    @Override
    public void onEnable() {
        RBConfiguration config = new RBConfiguration(this);
        this.getServer().getPluginManager().registerEvents(new RBPlayerListener(config), this);
        this.getServer().getPluginManager().registerEvents(new RBEntityListener(config), this);
        this.getCommand("rocketboots").setExecutor(new RocketBootsCommand(config));
        this.getLogger().info("RocketBoots v" + this.getDescription().getVersion() + " enabled!");
    }

}