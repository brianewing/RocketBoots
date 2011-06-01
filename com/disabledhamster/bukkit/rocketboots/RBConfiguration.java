package com.disabledhamster.bukkit.rocketboots;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

public final class RBConfiguration {

    private RocketBoots plugin;
    private Configuration config;

    public RBConfiguration(RocketBoots plugin) {
        this.plugin = plugin;

        File dataFolder = plugin.getDataFolder();
        dataFolder.mkdir();
        
        File configFile = new File(dataFolder, "config.yml");

        if(!configFile.exists())
            createDefaultConfigFile(configFile);

        this.config = new Configuration(configFile);
        this.config.load();
    }

    private void createDefaultConfigFile(File configFile) {
        try {
            configFile.createNewFile();
            
            InputStream configIn = RocketBoots.class.getResourceAsStream("/config.default.yml");
            FileOutputStream configOut = new FileOutputStream(configFile);
            
            byte[] buffer = new byte[1024];
            int read;

            while((read = configIn.read(buffer)) > 0)
                configOut.write(buffer, 0, read);

            configIn.close();
            configOut.close();
        } catch(IOException ioe) {
            plugin.getLogger().log(Level.WARNING, "Couldn't create default config file, using default values", ioe);
            return;
        }
    }

    public boolean strikeRealLightning() {
        return config.getBoolean("boots.chainmail.realLightning", true);
    }

    public int numberLightningStrikes() {
        int number = config.getInt("boots.chainmail.numLightningStrikes", 5);

        if(number < 0 || number > 20)
            return 5;
        else
            return number;
    }

    public double leatherBootsSpeed() {
        int number = config.getInt("boots.leather.launchSpeed", 20);

        if(number < 10)
            number = 10;
        else if(number > 50)
            number = 50;

        return (double)number / (double)10;
    }

    public double ironBootsVerticalSpeed() {
        int number = config.getInt("boots.iron.verticalSpeed", 10);

        if(number < 1)
            number = 1;
        else if(number > 50)
            number = 50;

        return (double)number / (double)10;
    }

    public boolean playersCanDisable() {
        return config.getBoolean("playersCanDisable", true);
    }

    public boolean playerEnabled(Player player) {
        return config.getBoolean("players." + player.getName() + ".enabled", true);
    }

    public void setPlayerEnabled(Player player, boolean enabled) {
        config.setProperty("players." + player.getName() + ".enabled", enabled);
        config.save();
    }

}