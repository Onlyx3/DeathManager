package com.only.deathmanager.inventory;

import com.only.deathmanager.main;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class InventoryListener implements Listener {

    private final boolean keepExp;
    private final boolean respectKeepInv;
    private final boolean keepHeld;
    private final boolean[] keepSlots;

    public InventoryListener() {
        FileConfiguration config = main.getMainConfig();

        this.keepSlots = new boolean[46];

        for (int i = 0; i <= 45; i++)
            this.keepSlots[i] = false;

        List<Integer> slots = config.getIntegerList("inventory.keepSlots");

        for (int slot : slots)
            this.keepSlots[slot] = true;

        this.keepExp = config.getBoolean("inventory.keepExp");
        this.respectKeepInv = config.getBoolean("inventory.respectKeepInv");
        this.keepHeld = config.getBoolean("inventory.keepHeld");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        boolean keepInv;
/*
        if(!WorldWhitelist.checkWorld(p, 2)) { // Check for World
            return;
        }

 */
        try {
            keepInv = p.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        } catch(NullPointerException ignored) {
            keepInv = p.getWorld().getGameRuleDefault(GameRule.KEEP_INVENTORY);
        }
        // If he has Skull
        if (SkulledPlayers.getSkulledPlayers().containsKey(p.getUniqueId())) {
            e.setKeepInventory(false);
            SkullListener.skullKillEvent(e);
            return;
        }

        SkullListener.skullKillEvent(e);

        if (this.respectKeepInv && keepInv)
            return;

        e.setKeepLevel(this.keepExp);
        e.setKeepInventory(true);
        e.getDrops().clear();

        PlayerInventory inv = p.getInventory();

        for (int slot = 0; slot <= 45; slot++) {
            if (this.keepSlots[slot])
                continue;

            if (this.keepHeld && inv.getHeldItemSlot() == slot)
                continue;

            ItemStack is = inv.getItem(slot);

            if (is == null)
                continue;

            e.getDrops().add(is);
            inv.clear(slot);
        }
    }

}
