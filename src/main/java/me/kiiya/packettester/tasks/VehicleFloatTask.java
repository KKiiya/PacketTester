package me.kiiya.packettester.tasks;

import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;

public class VehicleFloatTask implements Runnable {

    private EntityArmorStand as;
    private boolean up = true;

    public VehicleFloatTask(EntityArmorStand as) {
        this.as = as;
    }

    @Override
    public void run() {
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

        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) as.motX, (byte) as.motY, (byte) as.motZ, (byte) as.yaw, (byte) as.pitch, false);
        Utility.sendPlayersPacket(velocityPacket);
    }
}
