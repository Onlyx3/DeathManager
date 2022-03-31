package com.only.deathmanager.skulling;

import com.only.deathmanager.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SkullData {

    private static final SkullData obj = new SkullData();

    private Team team;
    private File skullFile;
    private final int initialTime;

    private final Map<UUID, Integer> skullPlayers = new Hashtable<>();

    public void setPlayerSkull(UUID uuid, int value) {
        if(!skullPlayers.containsKey(uuid)) {
            team.addEntry(Bukkit.getOfflinePlayer(uuid).getName());
        }
        skullPlayers.put(uuid, value);
    }

    public void addPlayerSkull(UUID uuid, int value) {
        if(!skullPlayers.containsKey(uuid)) {
            team.addEntry(Bukkit.getOfflinePlayer(uuid).getName());
        }
        int i = skullPlayers.getOrDefault(uuid, initialTime);
        i += value;
        skullPlayers.put(uuid, i);
    }

    public void removePlayerSkull(UUID uuid) {
        team.removeEntry(Bukkit.getOfflinePlayer(uuid).getName());
        skullPlayers.remove(uuid);
    }

    //Singleton Stuff
    public static SkullData getInstance() {
        return obj;
    }

    private SkullData() { //Constructor
        setupScoreboard();
        createSkullFile();
        loadSkullFile();

        FileConfiguration config = main.getMainConfig();
        initialTime = config.getInt("skulling.initial");
    }

    private void setupScoreboard() {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        String TEAM_NAME = "SkulledPlayers";
        team = sb.getTeam(TEAM_NAME);
        if(team == null) team = sb.registerNewTeam(TEAM_NAME);
        team.setPrefix("" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "☠ ");
        team.setColor(ChatColor.DARK_RED);
    }
    private void createSkullFile() {
        skullFile = new File(main.getInstance().getDataFolder(), "skulled.yml");
    }

    private void loadSkullFile() {
        if (skullFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(skullFile);

            for (Player p : Bukkit.getOnlinePlayers()) {
                UUID uuid = p.getUniqueId();
                if(config.contains(String.valueOf(uuid))) {
                    int value = config.getInt(String.valueOf(uuid));
                    skullPlayers.put(uuid, value);
                    team.addEntry(p.getName());
                }
            }
        }
    }

    public void loadPlayer(UUID uuid) {
        if(skullPlayers.containsKey(uuid)) return;
        FileConfiguration config = YamlConfiguration.loadConfiguration(skullFile);
        int value = config.getInt(String.valueOf(uuid));
        skullPlayers.put(uuid, value);
        team.addEntry(Bukkit.getOfflinePlayer(uuid).getName());
    }
    public void saveSkullFile() {
        FileConfiguration config = new YamlConfiguration();
        skullPlayers.forEach((uuid, integer) -> config.set(String.valueOf(uuid), integer));
        try {
            config.save(skullFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void savePlayer(UUID uuid) {
        FileConfiguration config = new YamlConfiguration();
        config.set(String.valueOf(uuid), skullPlayers.get(uuid));
        skullPlayers.remove(uuid);
        try {
            config.save(skullFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Integer> getSkullPlayers() {
        return skullPlayers;
    }
}
