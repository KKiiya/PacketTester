package me.kiiya.packettester.tasks;

import me.kiiya.packettester.PacketTester;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayRecordTask implements Runnable {

    private final EntityPlayer player;
    private final List<Location> locations;

    public PlayRecordTask(EntityPlayer player, List<Location> locations) {
        this.player = player;
        this.locations = locations;
    }

    @Override
    public void run() {
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            Bukkit.getScheduler().runTaskLaterAsynchronously(PacketTester.getInstance(), () -> {
                player.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(player);
                PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(player, (byte) ((location.getYaw() * 256.0F) / 360.0F));
                PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(player.getId(), (byte) ((location.getYaw() * 256.0F) / 360.0F), (byte) ((location.getPitch() * 256.0F) / 360.0F), true);
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    PlayerConnection connection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
                    connection.sendPacket(packet);
                    connection.sendPacket(headRotation);
                    connection.sendPacket(packetPlayOutEntityLook);
                }
            }, i);
        }
    }
}
