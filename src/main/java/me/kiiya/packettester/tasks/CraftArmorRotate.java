package me.kiiya.packettester.tasks;

import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CraftArmorRotate implements Runnable {
    private Player p;
    private EntityArmorStand as;
    private Location loc;
    private Location pLoc;
    private boolean up = false;
    public CraftArmorRotate(Player p, EntityArmorStand as, Location loc) {
        this.p = p;
        this.as = as;
        this.as.yaw = 0;
        this.loc = loc;
        this.pLoc = p.getLocation();
    }
    @Override
    public void run() {
        if (up) {
            if (as.yaw <= -360) {
                up = false;
            }

        } else {
            if (as.yaw >= 0) {
                up = true;
            }



        }

        PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(as.getId(), MathHelper.floor(loc.getX() * 32), MathHelper.floor(loc.getY() * 32), MathHelper.floor(loc.getZ() * 32), (byte) 0, (byte) 0, false);
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook lookPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) 0, (byte) 0, (byte) as.yaw, (byte) 0, false);
        Utility.sendPlayersPacket(teleportPacket);
        Utility.sendPlayersPacket(lookPacket);
    }
}
