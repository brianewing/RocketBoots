package com.disabledhamster.bukkit.rocketboots.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.disabledhamster.bukkit.rocketboots.RBConfiguration;
import com.disabledhamster.bukkit.rocketboots.RocketBoots;

public class RocketBootsCommand implements CommandExecutor {

    private final RBConfiguration config;

    public RocketBootsCommand(RocketBoots plugin) {
        this.config = plugin.getRBConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be used in-game");
            return true;
        }
        final Player player = (Player) sender;
        String enabledMessage;
        if ((args.length > 0) && this.config.playersCanDisable()) {
            final boolean enabled = args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on");

            this.config.setPlayerEnabled(player, enabled);
            enabledMessage = enabled ? "now enabled" : "now disabled";
        } else {
            enabledMessage = this.config.playerEnabled(player) ? "enabled" : "disabled";
        }
        player.sendMessage("RocketBoots are " + enabledMessage + " for you");
        return true;
    }

}