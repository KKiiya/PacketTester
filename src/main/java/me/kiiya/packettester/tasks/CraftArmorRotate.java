package me.kiiya.packettester.tasks;

import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CraftArmorRotate implements Runnable {
    private Player p;
    private EntityArmorStand as;
    private Location loc;
    private Location pLoc;
    private boolean up = true;
    public CraftArmorRotate(Player p, EntityArmorStand as) {
        this.p = p;
        this.as = as;
        this.loc = new Location(as.getWorld().getWorld(), as.locX, as.locY, as.locZ);
        this.pLoc = p.getLocation();
    }
    @Override
    public void run() {
        as.yaw += 2.0F;
        Utility.log("Motion: " + as.motY);
        if (up) {
            as.motY += 0.1;
            if (as.motY >= 1.0) {
                up = false;
            }
        } else {
            as.motY -= 0.1;
            if (as.motY <= -1.0) {
                up = true;
            }
        }
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(as.getId(), (int) as.locX, (int) as.locY, (int) as.locZ, (byte) as.yaw, (byte) as.pitch, false);
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(as, (byte) as.yaw);
        PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(as.getId(), 0, as.motY, 0);
        Utility.sendPacket(p, teleportPacket);
        Utility.sendPacket(p, headRotationPacket);
        Utility.sendPacket(p, velocityPacket);
    }
}
