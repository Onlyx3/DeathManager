package com.only.deathmanager.skulling;

import com.only.deathmanager.main;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.UUID;

public class InventoryDeathListener implements Listener {

    private final boolean keepExp;
    private final boolean respectKeepInv;
    private final boolean keepHeld;
    private final boolean[] keepSlots;
    private final int perKill;
    private SkullData data;

    public InventoryDeathListener() {
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
        perKill = config.getInt("skulling.perKill");
        data = SkullData.getInstance();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        UUID uuid = p.getUniqueId();

        boolean keepInv;
        try {
            keepInv = p.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        } catch(NullPointerException ignored) {
            keepInv = false;
        }

        if(data.getSkullPlayers().containsKey(uuid)) { //If dead person has a skull, make him drop items
            e.setKeepInventory(false);
            data.removePlayerSkull(uuid);   // and remove the skull
            return;
        }

        if(e.getEntity().getKiller() != null) { // If killed by a player, give the player a skull
            UUID attacker = e.getEntity().getKiller().getUniqueId();
            data.addPlayerSkull(attacker, perKill);
        }

        if (this.respectKeepInv && keepInv) return;

        // The Inventory magic
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
