package com.only.deathmanager.deathban;

import com.only.deathmanager.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class DeathBanListener implements Listener {

    private long time;
    private final DeathBanManager dbm;

    public DeathBanListener() {
        FileConfiguration config = main.getMainConfig();
        time = config.getLong("deathban.time");
        time *= 1000;
        dbm = DeathBanManager.getInstance();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        e.getEntity().kickPlayer("You died");
        UUID uuid = e.getEntity().getUniqueId();
        long bantime = System.currentTimeMillis()+time;
        dbm.addPlayer(uuid, bantime);
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        if(dbm.getBans().containsKey(uuid)) { //Check Hashmap

            long banTime = dbm.getBans().get(uuid);
            long currentTime = System.currentTimeMillis();

            if(isBanned(uuid, banTime, currentTime)) {
                long seconds = banTime - currentTime;
                seconds /= 1000;
                String message = "You died and are banned for " + seconds + " more seconds";
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
            } else {
                dbm.removePlayer(uuid);
                e.allow();
            }
        } else if(dbm.getFileConfig().contains(String.valueOf(uuid))) { // Check yml
            long banTime = dbm.getFileConfig().getLong(String.valueOf(uuid)); // Get time from config
            long currentTime = System.currentTimeMillis();

            if(isBanned(uuid, banTime, currentTime)) {
                dbm.getBans().put(uuid, banTime); // Put to Hashmap for faster access next time
                long seconds = banTime - currentTime;
                seconds /= 1000;
                String message = "You died and are banned for " + seconds + " more seconds";
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
            } else {
                dbm.removePlayer(uuid);
                e.allow();
            }
        }
    }
    private boolean isBanned(UUID uuid, long banTime, long currentTime) {
        return time - currentTime > 0;
    }

}
