package com.only.deathmanager.inventory;

import com.only.deathmanager.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class SkulledPlayers {

    private static final String TEAM_NAME = "SkulledPlayers";
    private static Team team;

    private static Thread scheduler;
    private static int schedulerSleep;
    private static int schedulerDecrement;

    private static File skulledFile;

    private static FileConfiguration config;

    private static final Map<UUID, Integer> skulledPlayers = new Hashtable<>();

    public static Map<UUID, Integer> getSkulledPlayers() {
        return skulledPlayers;
    }

    public static void setSkulledTag(UUID uuid) {
        team.addEntry(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public static void removeSkulledTag(UUID uuid) {
        team.removeEntry(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public static void startScheduler(JavaPlugin plugin) {
        config = main.getMainConfig();
        schedulerSleep = config.getInt("skulling.removalTime");
        schedulerDecrement = config.getInt("skulling.schedulerDecrement");

        if (schedulerSleep < 1) {
            schedulerSleep = 1000;
        }

        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        team = sb.getTeam(TEAM_NAME);

        if (team == null)
            team = sb.registerNewTeam(TEAM_NAME);

        team.setPrefix("" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "☠ ");
        team.setColor(ChatColor.DARK_RED);

        skulledFile = new File(plugin.getDataFolder(), "skulled.yml");

        if (skulledFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(skulledFile);

            for (String key : config.getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int value = config.getInt(key);

                skulledPlayers.put(uuid, value);

                team.addEntry(Bukkit.getOfflinePlayer(uuid).getName());
            }
        }

        scheduler = new Thread(() -> {
            while (true) {
                skulledPlayers.forEach((uuid, integer) -> {
                    if (integer >= 1) {
                        if (Bukkit.getPlayer(uuid).getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                            return;
                        }

                        skulledPlayers.put(uuid, integer - schedulerDecrement);
                    } else {
                        skulledPlayers.remove(uuid);
                        team.removeEntry(Bukkit.getOfflinePlayer(uuid).getName());
                    }
                });
                try {
                    Thread.sleep(schedulerSleep);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        scheduler.start();
    }

    public static void stopScheduler() {
        if (scheduler == null)
            return;

        scheduler.interrupt();

        FileConfiguration config = new YamlConfiguration();

        skulledPlayers.forEach((uuid, integer) -> config.set(String.valueOf(uuid), integer));

        try {
            config.save(skulledFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
