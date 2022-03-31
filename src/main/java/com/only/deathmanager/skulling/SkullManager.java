package com.only.deathmanager.skulling;

import com.only.deathmanager.main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class SkullManager {

    private int schedulerSleep;
    private Thread schedulerThread;
    private final SkullData data;


    public SkullManager() {
        data = SkullData.getInstance();
    }

    public void startScheduler() {
        schedulerSleep = main.getMainConfig().getInt("skulling.removalTime");
        schedulerSleep *= 1000;
        if(schedulerSleep <= 0) schedulerSleep = 1000;

        schedulerThread = new Thread(() -> {
            while(true) {

                for (UUID uuid  : data.getSkullPlayers().keySet()) {
                    Integer value = data.getSkullPlayers().get(uuid);
                    if(value >= 1) {
                        Player p = Bukkit.getPlayer(uuid);
                        if(p == null) {
                            continue;
                        }
                        if (p.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                            continue;
                        }
                        data.getSkullPlayers().put(uuid, value - 1);
                    } else {
                        data.removePlayerSkull(uuid);
                    }
                }

                try {
                    Thread.sleep(schedulerSleep);
                } catch(InterruptedException ignored) {
                }
                //END
            }
        });
        schedulerThread.start();
    }

    public void stopScheduler() {
        if (schedulerThread == null) return;

        schedulerThread.interrupt();

        data.saveSkullFile();
    }
}
