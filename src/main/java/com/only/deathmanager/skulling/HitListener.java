package com.only.deathmanager.skulling;

import com.only.deathmanager.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitListener implements Listener {

    private final int perHit;
    private SkullData data;

    public HitListener() {
        FileConfiguration config = main.getMainConfig();
        perHit = config.getInt("skulling.perHit");
        data = SkullData.getInstance();
    }

    @EventHandler
    public void skullHitEvent(EntityDamageByEntityEvent e) {
        if (e.getEntityType() != EntityType.PLAYER || e.getDamager().getType() != EntityType.PLAYER) {
            return;
        }

        // Add skull to attacker if defender has no skull
        if(!data.getSkullPlayers().containsKey(e.getEntity().getUniqueId())) {
            data.addPlayerSkull(e.getDamager().getUniqueId(), perHit);
        }

    }
}
