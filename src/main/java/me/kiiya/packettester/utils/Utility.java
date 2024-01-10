package me.kiiya.packettester.utils;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.tasks.CraftArmorRotate;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utility {
    // Colorize a string
    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Log a string to console
    public static void log(String s) {
        PacketTester.getInstance().getLogger().info(c(s));
    }

    public static void sendPacket(Player p, Packet<PacketListenerPlayOut> packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendPlayersPacket(Packet<PacketListenerPlayOut> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void spawnArmorStand(Player p) {
        Location loc = p.getLocation();
        EntityArmorStand as = new EntityArmorStand(((CraftPlayer) p).getHandle().getWorld(), loc.getX(), loc.getY(), loc.getZ());
        CraftArmorStand a = new CraftArmorStand((CraftServer) PacketTester.getInstance().getServer(), as);
        a.setGravity(false);
        a.setBasePlate(false);
        a.setVisible(false);
        as.locX = loc.getX();
        as.locY = loc.getY();
        as.locZ = loc.getZ();
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(as);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(as.getId(), 4, CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_BLOCK)));
        sendPacket(p, spawnPacket);
        sendPacket(p, metadataPacket);
        sendPacket(p, equipmentPacket);
        Bukkit.getScheduler().runTaskTimer(PacketTester.getInstance(), new CraftArmorRotate(p, as, loc), 0, 1);
    }
}
