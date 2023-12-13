package me.kiiya.packettester.utils;

import me.kiiya.packettester.PacketTester;
import org.bukkit.ChatColor;

public class Utility {
    // Colorize a string
    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Log a string to console
    public static void log(String s) {
        PacketTester.getInstance().getLogger().info(c(s));
    }
}
