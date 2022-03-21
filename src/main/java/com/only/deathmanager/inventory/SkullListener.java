package com.only.deathmanager.inventory;

import com.only.deathmanager.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class SkullListener implements Listener {

    private static int perHit;
    private static int initial;
    private static int perKill;

    public SkullListener() {
        FileConfiguration config = main.getMainConfig();
        perHit = config.getInt("skulling.perHit");
        initial = config.getInt("skulling.initial");
        perKill = config.getInt("skulling.perKill");
    }

    @EventHandler
    public void skullDamageEvent(EntityDamageByEntityEvent e) {
        if (e.getEntityType() != EntityType.PLAYER || e.getDamager().getType() != EntityType.PLAYER) {
            return;
        }

        UUID attacker = e.getDamager().getUniqueId();
        UUID defender = e.getEntity().getUniqueId();

        //Return if defender has Skull
        if (SkulledPlayers.getSkulledPlayers().containsKey(defender)) {
            return;
        }

        int i = SkulledPlayers.getSkulledPlayers().getOrDefault(attacker, initial);

        i += perHit;

        SkulledPlayers.getSkulledPlayers().put(attacker, i);
        SkulledPlayers.setSkulledTag(attacker);
    }

    public static void skullKillEvent(PlayerDeathEvent e) {
        if (e.getEntityType() != EntityType.PLAYER || e.getEntity().getKiller() == null) {
            return;
        }

        UUID attacker = e.getEntity().getKiller().getUniqueId();
        UUID defender = e.getEntity().getUniqueId();

        //Return if defender has Skull
        if (SkulledPlayers.getSkulledPlayers().containsKey(defender)) {
            SkulledPlayers.getSkulledPlayers().remove(defender);
            SkulledPlayers.removeSkulledTag(defender);
            return;
        }


        int i = SkulledPlayers.getSkulledPlayers().getOrDefault(attacker, initial);

        i += perKill;

        SkulledPlayers.getSkulledPlayers().put(attacker, i);
        SkulledPlayers.setSkulledTag(attacker);
    }
}

