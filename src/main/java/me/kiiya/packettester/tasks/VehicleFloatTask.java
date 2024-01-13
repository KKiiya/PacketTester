package me.kiiya.packettester.tasks;

import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;

public class VehicleFloatTask implements Runnable {

    private EntityArmorStand as;
    private boolean up = true;

    public VehicleFloatTask(EntityArmorStand as) {
        this.as = as;
        this.as.motY = 0;
    }

    @Override
    public void run() {
        if (up) {
            as.motY += 0.8;

            if (as.motY >= 5) {
                up = false;
            }
        } else {
            as.motY -= 0.8;

            if (as.motY <= -5) {
                up = true;
            }
        }

        PacketPlayOutEntity.PacketPlayOutRelEntityMove velocityPacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(as.getId(), (byte) 0, (byte) as.motY, (byte) 0, false);
        Utility.sendPlayersPacket(velocityPacket);
    }
}
