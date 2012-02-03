package com.disabledhamster.bukkit.rocketboots.commands;

import com.disabledhamster.bukkit.rocketboots.RBConfiguration;
import com.disabledhamster.bukkit.rocketboots.RocketBoots;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RocketBootsCommand implements CommandExecutor {

    private RBConfiguration config;

    public RocketBootsCommand(RocketBoots plugin) {
        this.config = plugin.getRBConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("This command must be used in-game");
            return true;
        }

        Player player = (Player)sender;
        String enabledMessage;

        if(args.length > 0 && config.playersCanDisable()) {
            boolean enabled = args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on");

            config.setPlayerEnabled(player, enabled);
            enabledMessage = enabled ? "now enabled" : "now disabled";
        } else {
            enabledMessage = config.playerEnabled(player) ? "enabled" : "disabled";
        }

        player.sendMessage("RocketBoots are " + enabledMessage + " for you");
        return true;
    }

}