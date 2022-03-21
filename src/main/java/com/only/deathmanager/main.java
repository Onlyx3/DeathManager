package com.only.deathmanager;

import com.only.deathmanager.inventory.InventoryListener;
import com.only.deathmanager.inventory.SkullListener;
import com.only.deathmanager.inventory.SkulledPlayers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private static ConfigHelper mainConfig;

    @Override
    public void onEnable() {
        // Config Setup
        mainConfig = new ConfigHelper("config.yml", this);
        mainConfig.saveDefaultConfig();

        registerServerEvents();
        startScheduler();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SkulledPlayers.stopScheduler();
    }

    public static FileConfiguration getMainConfig() {
        return main.mainConfig.getConfig();
    }


    // Privates
    private void registerServerEvents() {
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new SkullListener(), this);
    }
    private void startScheduler() {
        SkulledPlayers.startScheduler(this);
    }
}
