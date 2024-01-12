package me.kiiya.packettester.commands;

import me.kiiya.packettester.utils.Utility;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

// IGNORE THIS CLASS
public class VehicleSummonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player p = (Player) commandSender;

        if (strings.length > 1) {
            Utility.spawnArmorStand(p);
            return true;
        }
        if (p.isInsideVehicle()) {
            p.getVehicle().eject();

            Utility.spawnVehicle(p);
        } else {
            Utility.spawnVehicle(p);
        }
        return true;
    }
}
