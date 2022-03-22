package com.only.deathmanager.commands;

import com.only.deathmanager.inventory.SkulledPlayers;

import java.util.UUID;

public class commandRemoveSkull {
    public static void removeSkull(UUID pUUID) {
        if(SkulledPlayers.getSkulledPlayers().containsKey(pUUID)) {
            SkulledPlayers.removePlayerSkull(pUUID);
        }
    }
}
