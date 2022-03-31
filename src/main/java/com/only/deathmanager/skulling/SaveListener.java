package com.only.deathmanager.skulling;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SaveListener implements Listener {

    private final SkullData data;

    public SaveListener() {
        data = SkullData.getInstance();
    }

    @EventHandler
    public void loadOnJoin(PlayerJoinEvent e) {
        data.loadPlayer(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void saveOnLeave(PlayerQuitEvent e) {
        data.savePlayer(e.getPlayer().getUniqueId());
    }

}
