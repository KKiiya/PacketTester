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
    public CraftArmorRotate(Player p, EntityArmorStand as, Location loc) {
        this.p = p;
        this.as = as;
        this.loc = loc;
        this.pLoc = p.getLocation();
    }
    @Override
    public void run() {
        if (up) {
            as.motY += 1;
            switch ((int) as.motY) {
                case 1:
                    as.yaw += 1;
                    break;
                case 2:
                    as.yaw += 2;
                    break;
                case 3:
                    as.yaw += 3;
                    break;
                case 4:
                    as.yaw += 4;
                    break;
                case 5:
                    as.yaw += 5;
                    break;
                case 6:
                    as.yaw += 6;
                    break;
                case 7:
                    as.yaw += 7;
                    break;
                case 8:
                    as.yaw += 8;
                    break;
            }
            if (as.motY >= 8.0) {
                up = false;
            }
        } else {
            as.motY -= 1;
            switch ((int) as.motY) {
                case 7:
                    as.yaw -= 1;
                    break;
                case 6:
                    as.yaw -= 2;
                    break;
                case 5:
                    as.yaw -= 3;
                    break;
                case 4:
                    as.yaw -= 4;
                    break;
                case 3:
                    as.yaw -= 5;
                    break;
                case 2:
                    as.yaw -= 6;
                    break;
                case 1:
                    as.yaw -= 7;
                    break;
                case 0:
                    as.yaw -= 8;
                    break;
            }
            if (as.motY <= -8.0) {
                up = true;
            }
        }
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(as.getId(), MathHelper.floor(loc.getX() * 32), MathHelper.floor(loc.getY() * 32), MathHelper.floor(loc.getZ() * 32), (byte) as.yaw, (byte) as.pitch, false);
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(as, (byte) as.yaw);
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) as.motX, (byte) as.motY, (byte) as.motZ, (byte) as.yaw, (byte) as.pitch, false);
        Utility.sendPacket(p, teleportPacket);
        Utility.sendPacket(p, headRotationPacket);
        Utility.sendPacket(p, velocityPacket);
    }
}
