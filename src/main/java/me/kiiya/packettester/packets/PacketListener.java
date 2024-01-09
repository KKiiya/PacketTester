package me.kiiya.packettester.packets;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.events.PacketReadEvent;
import me.kiiya.packettester.events.PacketWriteEvent;
import me.kiiya.packettester.tasks.ArmorStandSpinTask;
import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

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

    // PacketReadEvent is called when a packet is received from the client.
    @EventHandler
    public void onPacketRead(PacketReadEvent e) {
        // Utility.log("&bPacket read: " + e.getPacket().getClass().getSimpleName());

        Player p = e.getPlayer();
        Object pac = e.getPacket();

        // This is an example of how to listen to a packet.
        if (pac instanceof PacketPlayInSteerVehicle) {
            ArmorStand as = (ArmorStand) p.getVehicle();
            
            if (ArmorStandSpinTask.spinningPlayers.contains(p)) {
                e.setCancelled(true);
                return;
            }

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


            p.getWorld().spigot().playEffect(as.getLocation(), Effect.FLAME, 0, 0, 0, 0, 0, 0, 1, 1);
            p.getWorld().spigot().playEffect(as.getLocation(), Effect.HEART, 0, 0, 0, 0, 0, 0, 1, 1);
            p.getWorld().spigot().playEffect(as.getLocation(), Effect.SLIME, 0, 0, 0, 0, 0, 0, 1, 1);
            p.getWorld().spigot().playEffect(as.getLocation(), Effect.CLOUD, 0, 0, 0, 0, 0, 0, 1, 1);

            if (sides < 0) {
                // RIGHT
                double z = as.getHeadPose().getZ();
                if (forward == 0) as.setHeadPose(new EulerAngle(Math.sin(as.getHeadPose().getZ()), as.getHeadPose().getY() + 0.05, -0.25));
                else if (forward > 0) {
                    as.setHeadPose(new EulerAngle(0, as.getHeadPose().getY() + 0.065, -0.5));
                    as.setVelocity(new Vector(Math.cos(as.getHeadPose().getY() + Math.PI/2)*0.8, 0, Math.sin(as.getHeadPose().getY() + Math.PI/2)*0.8));
                }
                else if (forward < 0) {
                    as.setHeadPose(new EulerAngle(Math.sin(as.getHeadPose().getZ()), as.getHeadPose().getY() + 0.065, -0.25));
                    as.setVelocity(new Vector(Math.cos(as.getHeadPose().getY() + Math.PI/2)*-0.5, 0, Math.sin(as.getHeadPose().getY() + Math.PI/2)*-0.5));
                }
            } else if (sides > 0) {
                // LEFT
                double z = as.getHeadPose().getZ();
                if (forward == 0) as.setHeadPose(new EulerAngle(Math.sin(as.getHeadPose().getZ()), as.getHeadPose().getY() - 0.05, 0.25));
                else if (forward > 0) {
                    as.setHeadPose(new EulerAngle(Math.sin(as.getHeadPose().getZ()), as.getHeadPose().getY() - 0.065, 0.25));
                    as.setVelocity(new Vector(Math.cos(as.getHeadPose().getY() + Math.PI/2)*0.8, 0, Math.sin(as.getHeadPose().getY() + Math.PI/2)*0.8));
                }
                else if (forward < 0) {
                    as.setHeadPose(new EulerAngle(Math.sin(as.getHeadPose().getZ()), as.getHeadPose().getY() - 0.065, 0.25));
                    as.setVelocity(new Vector(Math.cos(as.getHeadPose().getY() + Math.PI/2)*-0.5, 0, Math.sin(as.getHeadPose().getY() + Math.PI/2)*-0.5));
                }
            } else {
                // CENTER
                /*
                double z = as.getHeadPose().getZ();
                if (as.getHeadPose().getZ() > 0) z -= 0.05;
                else if (as.getHeadPose().getZ() < 0) z += 0.05;
                else z = 0;
                as.setHeadPose(new EulerAngle(0, as.getHeadPose().getY(), z));
                 */
                as.setHeadPose(new EulerAngle(0, as.getHeadPose().getY(), 0));
                if (forward > 0) {
                    as.setVelocity(new Vector(Math.cos(as.getHeadPose().getY() + Math.PI/2)*1, 0, Math.sin(as.getHeadPose().getY() + Math.PI/2)*1));
                } else if (forward < 0) {
                    as.setVelocity(new Vector(Math.cos(as.getHeadPose().getY() + Math.PI/2)*-0.5, 0, Math.sin(as.getHeadPose().getY() + Math.PI/2)*-0.5));
                } else {
                    double y = as.getVelocity().getY();

                    y += 0.0025;
                    if (as.getVelocity().getY() >= 0.05) y -= 0.05;
                    else if (as.getVelocity().getY() <= -0.05) y += 0.05;

                    as.setVelocity(new Vector(0, y, 0));
                }
            }

            if (forward == 0) p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.15F, 0.45F);
            else if (forward > 0) p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.15F, 0.75F);
            else if (forward < 0) p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 0.1F, 0.45F);

            p.getWorld().spigot().playEffect(as.getLocation().add(0, 1D, 1), Effect.LARGE_SMOKE, 0, 0, 0, 0, 0, 0, 1, 1);



            if (space) p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);

            if (unmount) {
                if (sides == 0) {
                    Bukkit.getScheduler().runTaskAsynchronously(PacketTester.getInstance(), new ArmorStandSpinTask(as, p));
                }

                // This will prevent the packet from being read.
                e.setCancelled(true);
            }
        }
        // End of example.

    }
}
