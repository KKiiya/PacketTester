package me.kiiya.packettester.packets;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.events.PacketReadEvent;
import me.kiiya.packettester.events.PacketWriteEvent;
import me.kiiya.packettester.tasks.ArmorStandSpinTask;
import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

// This is an example class to listen to packets.
public class PacketListener implements Listener {
    // Packets are handled by the server, so we need to register a listener to the server.
    // This class is registered in InjectorHandler.java.
    // Info about packets: https://wiki.vg/Protocol || Outdated (E.j: You can't find PacketPlayInSteerVehicle).
    // How to find packets: import net.minecraft.server.<version>.<packet>;
    // Example: import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
    // Note: You can't find packets in the Spigot API, you need to find them in the Minecraft server API.

    // SINGLETON INSTANCE
    protected PacketListener() {
        Utility.log("&aPacket listener registered.");
    }

    // EVENTS CRATED BY ME (CHECK me.kiiya.packettester.events)

    // PacketWriteEvent is called when a packet is sent to the client.
    @EventHandler
    public void onPacketWrite(PacketWriteEvent e) {
        // Utility.log("&bPacket write: " + e.getPacket().getClass().getSimpleName());
    }

    private final HashMap<Player, Integer> time = new HashMap<>();

    // PacketReadEvent is called when a packet is received from the client.
    @EventHandler
    public void onPacketRead(PacketReadEvent e) {
        // Utility.log("&bPacket read: " + e.getPacket().getClass().getSimpleName());

        Player p = e.getPlayer();
        Object pac = e.getPacket();

        // This is an example of how to listen to a packet.
        if (pac instanceof PacketPlayInSteerVehicle) {
            EntityArmorStand as = Utility.getVehicle(p);
            time.putIfAbsent(p, 0);

            PacketPlayInSteerVehicle packet = (PacketPlayInSteerVehicle) pac;

            // PacketPlayInSteerVehicle is the packet that is sent when a player is riding an entity.
            // PacketPlayInSteerVehicle has 4 fields:
            // a() - left/right (-0.98/0.98) | If the player is holding A or D.
            // b() - forward/backward (-0.98/0.98) | If the player is holding W or S.
            // c() - jump (true/false) | If the player is jumping (trying to jump inside the vehicle).
            // d() - dismount (true/false) | If the player is crouching.
            // Example:
            float sides = packet.a(); // left/right (-0.98/0.98)
            float forward = packet.b(); // forward/backward (-0.98/0.98)
            boolean space = packet.c(); // jump (true/false)
            boolean unmount = packet.d(); // dismount (true/false)

            // Calculate the direction vector using the yaw value
            double dirX = Math.cos(Math.toRadians(as.yaw));
            double dirZ = Math.sin(Math.toRadians(as.yaw));

            if (sides < 0) {
                // RIGHT
                as.yaw += 6;
                PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(as);
                PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) 0, (byte) 0, (byte) as.yaw, (byte) 0, false);
                Utility.sendPlayersPacket(teleportPacket);
                Utility.sendPlayersPacket(velocityPacket);
            } else if (sides > 0) {
                // LEFT
                as.yaw -= 6;
                PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(as);
                PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) 0, (byte) 0, (byte) as.yaw, (byte) 0, false);
                Utility.sendPlayersPacket(teleportPacket);
                Utility.sendPlayersPacket(velocityPacket);
            } else {
                // CENTER
            }

            if (forward == 0) {
                as.motY = 0.8 * Math.sin(time.get(p));
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.15F, 0.45F);
                PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) as.motY, (byte) 0, (byte) 0, (byte) 0, false);
                Utility.sendPlayersPacket(velocityPacket);

            }
            else if (forward > 0) {
                // FORWARD
                as.motX = dirX * 25;
                as.motZ = dirZ * 25;
                double nextX = as.locX + (as.motX/32);
                double nextZ = as.locZ + (as.motZ/32);
                as.setLocation(nextX, as.locY, nextZ, as.yaw, as.pitch);
                PacketPlayOutEntity.PacketPlayOutRelEntityMove velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(as.getId(), (byte) as.motX, (byte) 0, (byte) as.motZ, true);
                Utility.sendPlayersPacket(velocityPacket);
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.15F, 0.75F);
            }
            else if (forward < 0) {
                // BACKWARDS
                as.motX = -dirX * 25;
                as.motZ = -dirZ * 25;
                double nextX = as.locX + (as.motX/32);
                double nextZ = as.locZ + (as.motZ/32);
                as.setLocation(nextX, as.locY, nextZ, as.yaw, as.pitch);
                PacketPlayOutEntity.PacketPlayOutRelEntityMove velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(as.getId(), (byte) as.motX, (byte) 0, (byte) as.motZ, true);
                Utility.sendPlayersPacket(velocityPacket);
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.1F, 0.45F);
            }

            if (space) p.getWorld().playSound(new Location(p.getWorld(), as.locX, as.locY, as.locZ), Sound.ORB_PICKUP, 0.15F, 0.45F);

            if (unmount) {
                if (sides == 0) {
                    int taskid = Bukkit.getScheduler().runTaskTimer(PacketTester.getInstance(), new ArmorStandSpinTask(as, p), 0L, 1L).getTaskId();
                    Bukkit.getScheduler().runTaskLater(PacketTester.getInstance(), () -> {
                        Bukkit.getScheduler().cancelTask(taskid);
                        as.motX = 0;
                        as.motY = 0;
                        as.motZ = 0;
                        PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(as.getId(), 0, 0, 0);
                        Utility.sendPlayersPacket(velocityPacket);
                    }, 60L);
                }

                // This will prevent the packet from being read.
                e.setCancelled(true);
            }
        }
        time.put(p, time.get(p) + 1);
        // End of example.

    }
}
