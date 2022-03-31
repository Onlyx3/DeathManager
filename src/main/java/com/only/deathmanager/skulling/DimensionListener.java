package com.only.deathmanager.skulling;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.UUID;

public class DimensionListener implements Listener {

    private final SkullData data;

    public DimensionListener() {
        data = SkullData.getInstance();
    }

    @EventHandler
    public void onNetherChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (e.getFrom().getEnvironment().equals(World.Environment.NETHER) && data.getSkullPlayers().get(uuid) <= 1) {
            data.getSkullPlayers().remove(uuid);
        }

        if(p.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            if(!data.getSkullPlayers().containsKey(uuid)) {
                data.setPlayerSkull(uuid, 1);
            }
        }
    }
}
