package com.only.deathmanager.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class mainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args[0].equalsIgnoreCase("removeskull")) {
            try {
                UUID uuid = Bukkit.getPlayer(args[1]).getUniqueId();
                commandRemoveSkull.removeSkull(uuid);
                return true;
            } catch(NullPointerException e) {
                sender.sendMessage("This player doesnt exist or is not online.");
                return false;
            }
        }
        return false;
    }
}
