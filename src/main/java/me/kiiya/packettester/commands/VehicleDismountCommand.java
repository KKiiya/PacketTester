package me.kiiya.packettester.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

// IGNORE THIS CLASS
public class VehicleDismountCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (((Player) commandSender).isInsideVehicle()) {
            ((Player) commandSender).getVehicle().eject();
        }
        return false;
    }
}
