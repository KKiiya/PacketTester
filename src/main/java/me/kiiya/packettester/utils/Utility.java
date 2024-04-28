package me.kiiya.packettester.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.tasks.CraftArmorRotate;
import me.kiiya.packettester.tasks.FollowPet;
import me.kiiya.packettester.tasks.VehicleFloatTask;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

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

    public static void spawnPekoraPet(Player p) {
        Location loc = p.getLocation();
        EntityArmorStand as = new EntityArmorStand(((CraftPlayer) p).getHandle().getWorld(), loc.getX(), loc.getY(), loc.getZ());
        CraftArmorStand a = new CraftArmorStand((CraftServer) PacketTester.getInstance().getServer(), as);
        a.setGravity(false);
        a.setBasePlate(false);
        a.setVisible(false);
        a.setCustomName(c("&b兎田ぺこら &fこんぺここんぺここんぺこ！ &7「" + p.getName() + "さん" + "」"));
        a.setCustomNameVisible(true);
        a.setSmall(true);
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(as);
        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(as.getId(), as.getDataWatcher(), true);
        ItemStack head = getSkull("https://textures.minecraft.net/c8bbdb00195bf569aa5fdb0f61e50bd91235ebb2f36baa4ad66b62f74e56dfa5");
        PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(as.getId(), 4, CraftItemStack.asNMSCopy(head));
        sendPlayersPacket(spawnPacket);
        sendPlayersPacket(metadataPacket);
        sendPlayersPacket(equipmentPacket);
        Bukkit.getScheduler().runTaskTimer(PacketTester.getInstance(), new FollowPet(p, as), 0, 1);
    }

    public static EntityArmorStand getVehicle(Player p) {
        return playerVehicle.get(p);
    }

    public static CraftArmorStand getCraftArmorStand(EntityArmorStand as) {
        return craftArmorStand.get(as);
    }

    public static ItemStack getSkull(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

        if (url == null || url.isEmpty())
            return skull;

        ItemMeta skullMeta = skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;

        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }

        profileField.setAccessible(true);

        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static EntityPlayer createNpc(Player p, Location loc) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "NPC");
        EntityPlayer npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        return npc;
    }
}
