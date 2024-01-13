package me.kiiya.packettester.commands;

import me.kiiya.packettester.utils.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class PekoraPetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player p = (Player) commandSender;
        Utility.spawnPekoraPet(p);
        return false;
    }
}
