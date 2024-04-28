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

    }
}
