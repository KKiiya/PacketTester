package me.kiiya.packettester.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

// IGNORE THIS CLASS
public class VehicleSummonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player p = (Player) commandSender;

        if (p.isInsideVehicle()) {
            p.getVehicle().eject();

            ArmorStand as = p.getWorld().spawn(p.getLocation(), ArmorStand.class);

            as.setVisible(false);
            as.setCanPickupItems(false);
            as.setCustomName("kart");
            as.setCustomNameVisible(false);
            as.setHelmet(new ItemStack(Material.FURNACE));
            as.setHeadPose(EulerAngle.ZERO);

            as.setPassenger(p);
        } else {
            ArmorStand as = p.getWorld().spawn(p.getLocation(), ArmorStand.class);

            as.setVisible(false);
            as.setCanPickupItems(false);
            as.setCustomName("kart");
            as.setCustomNameVisible(false);
            as.setHelmet(new ItemStack(Material.FURNACE));
            as.setHeadPose(EulerAngle.ZERO);

            as.setPassenger(p);
        }
        return true;
    }
}
