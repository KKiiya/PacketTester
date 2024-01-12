package me.kiiya.packettester.tasks;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import java.util.LinkedList;

// IGNORE THIS CLASS
public class ArmorStandSpinTask implements Runnable {
    public static final LinkedList<Player> spinningPlayers = new LinkedList<>();
    private final EntityArmorStand as;
    private final Player p;

    public ArmorStandSpinTask(EntityArmorStand as, Player p) {
        this.as = as;
        this.p = p;
        spinningPlayers.add(p);
    }

    @Override
    public void run() {
        int z = 0;
        as.yaw += 4;
        Utility.sendPlayersPacket(new PacketPlayOutEntityTeleport(as));
        Utility.sendPlayersPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(as.getId(), (byte) 0, (byte) 0, (byte) 0, (byte) as.yaw, (byte) 0, false));
    }
}
