package me.kiiya.packettester.tasks;

import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FollowPet implements Runnable {

    private Player p;
    private EntityArmorStand as;
    boolean up = true;

    public FollowPet(Player p, EntityArmorStand as) {
        this.p = p;
        this.as = as;
    }

    @Override
    public void run() {
        Location loc = p.getLocation();
        if (up) {
            as.motY += 1;

            if (as.motY >= 5) {
                up = false;
            }
        } else {
            as.motY -= 1;

            if (as.motY <= -5) {
                up = true;
            }
        }

        as.yaw = loc.getYaw();
        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(as.getId(), MathHelper.floor((loc.getX()) * 32), MathHelper.floor((loc.getY() + 1.5) * 32), MathHelper.floor((loc.getZ()) * 32), (byte) as.yaw, (byte) as.pitch, false);
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook lookPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) as.motY, (byte) 0, (byte) as.yaw, (byte) 0, false);
        Utility.sendPlayersPacket(teleportPacket);
        Utility.sendPlayersPacket(lookPacket);
    }
}
