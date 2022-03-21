package com.only.deathmanager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class ConfigHelper {

    private String name;
    private JavaPlugin plugin;

    private FileConfiguration config = null;
    private File configFile = null;

    public ConfigHelper(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(this.plugin.getDataFolder(), this.name);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultMaterialConfig = this.plugin.getResource(this.name);

        if (defaultMaterialConfig == null)
            throw new RuntimeException("Fuck.");

        Reader defConfigStream = new InputStreamReader(defaultMaterialConfig, StandardCharsets.UTF_8);

        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        config.setDefaults(defConfig);
    }
    
    public FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();

        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null)
            return;

        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(this.plugin.getDataFolder(), this.name);
        }
        if (!configFile.exists()) {
            this.plugin.saveResource(this.name, false);
        }
    }
    
}
