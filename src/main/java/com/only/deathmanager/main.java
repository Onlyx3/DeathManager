package com.only.deathmanager;

import com.only.deathmanager.commands.mainCommand;
import com.only.deathmanager.deathban.DeathBanListener;
import com.only.deathmanager.skulling.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private static ConfigHelper mainConfig;
    private static main instance;

    private SkullManager skullScheduler;

    @Override
    public void onEnable() {
        instance = this;


        // Config Setup
        mainConfig = new ConfigHelper("config.yml", this);
        mainConfig.saveDefaultConfig();

        registerServerEvents();
        registerCommands();

        skullScheduler = new SkullManager();
        skullScheduler.startScheduler();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        skullScheduler.stopScheduler();
    }

    public static FileConfiguration getMainConfig() {
        return main.mainConfig.getConfig();
    }


    // Privates
    private void registerServerEvents() {
        this.getServer().getPluginManager().registerEvents(new InventoryDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new HitListener(), this);
        this.getServer().getPluginManager().registerEvents(new DimensionListener(), this);
        this.getServer().getPluginManager().registerEvents(new SaveListener(), this);

        this.getServer().getPluginManager().registerEvents(new DeathBanListener(), this);
    }
    private void registerCommands() {
        this.getCommand("dm").setExecutor(new mainCommand());
    }

    public static main getInstance() {
        return instance;
    }

}

/*
TODO: Comments
 */