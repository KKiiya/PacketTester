package me.kiiya.packettester.utils;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.tasks.CraftArmorRotate;
import me.kiiya.packettester.tasks.VehicleFloatTask;
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

import java.util.HashMap;

public class Utility {
    private static final HashMap<Player, EntityArmorStand> playerVehicle = new HashMap<>();
    private static final HashMap<EntityArmorStand, CraftArmorStand> craftArmorStand = new HashMap<>();
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
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook teleportPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, false);
        sendPlayersPacket(spawnPacket);
        sendPlayersPacket(metadataPacket);
        sendPlayersPacket(equipmentPacket);
        sendPlayersPacket(teleportPacket);
        Bukkit.getScheduler().runTaskTimer(PacketTester.getInstance(), new CraftArmorRotate(p, as, loc), 0, 1);
    }

    public static void spawnVehicle(Player p) {
        Location loc = p.getLocation();
        EntityArmorStand as = new EntityArmorStand(((CraftPlayer) p).getHandle().getWorld(), loc.getX(), loc.getY()-1, loc.getZ());
        CraftArmorStand a = new CraftArmorStand((CraftServer) PacketTester.getInstance().getServer(), as);
        a.setGravity(false);
        a.setBasePlate(false);
        a.setVisible(false);
        a.setCustomName("kart");
        a.setCustomNameVisible(false);
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(as);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(as.getId(), 4, CraftItemStack.asNMSCopy(new ItemStack(Material.FURNACE)));
        sendPlayersPacket(spawnPacket);
        sendPlayersPacket(metadataPacket);
        sendPlayersPacket(equipmentPacket);
        playerVehicle.put(p, as);
        craftArmorStand.put(as, a);
        getOnVehicle(p);
        Bukkit.getScheduler().runTaskTimer(PacketTester.getInstance(), new VehicleFloatTask(as), 0, 1);
    }

    public static void getOnVehicle(Player p) {
        EntityArmorStand as = playerVehicle.get(p);
        CraftArmorStand a = craftArmorStand.get(as);
        a.setPassenger(p);
    }

    public static EntityArmorStand getVehicle(Player p) {
        return playerVehicle.get(p);
    }

    public static CraftArmorStand getCraftArmorStand(EntityArmorStand as) {
        return craftArmorStand.get(as);
    }
}
