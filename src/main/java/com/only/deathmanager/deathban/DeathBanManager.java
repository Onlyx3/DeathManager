package com.only.deathmanager.deathban;

import com.only.deathmanager.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathBanManager {

    private static final DeathBanManager obj = new DeathBanManager();
    private DeathBanManager() {

    }
    public static DeathBanManager getInstance() {
        return obj;
    }

    private Map<UUID, Long> bans = new HashMap<>();
    private File banFile;
    private final FileConfiguration fconfig = YamlConfiguration.loadConfiguration(banFile);

    public Map<UUID, Long> getBans() {
        return bans;
    }
    //TODO: Save in yml file
    private void createBanFile() {
        banFile = new File(main.getInstance().getDataFolder(), "banned.yml");
    }

    private void loadSkullFile() {
        if (banFile.exists()) {

            for(String suuid : fconfig.getKeys(true)) {
                UUID uuid = UUID.fromString(suuid);
                long value = fconfig.getLong(suuid);
                bans.put(uuid, value);
            }
        }
    }

    public void addPlayer(UUID uuid, long value) {
        bans.put(uuid, value);
        fconfig.set(String.valueOf(uuid), value);
        try {
            fconfig.save(banFile);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
    public void removePlayer(UUID uuid) {
        //TODO:
        bans.remove(uuid);
        fconfig.set(String.valueOf(uuid), null);
        try {
            fconfig.save(banFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfig() {
        return fconfig;
    }
}
