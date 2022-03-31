package com.only.deathmanager.commands;

import com.only.deathmanager.skulling.SkullData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class mainCommand implements CommandExecutor {

    private final SkullData data;

    public mainCommand() {
        data = SkullData.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args[0].equalsIgnoreCase("removeskull")) {
            try {
                UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                if(data.getSkullPlayers().containsKey(uuid)) {
                    data.removePlayerSkull(uuid);
                }
                return true;
            } catch(NullPointerException e) {
                sender.sendMessage("This player doesnt exist or is not online.");
                return false;
            }
        }

        if(args [0].equalsIgnoreCase("gettimer")) {
            try {
                UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                int time = data.getSkullPlayers().get(uuid);
                sender.sendMessage(String.valueOf(time));
                return true;
            } catch(NullPointerException e) {
                sender.sendMessage("This player doesnt exist or is not online.");
                return false;
            }
        }
        return false;
    }
}
