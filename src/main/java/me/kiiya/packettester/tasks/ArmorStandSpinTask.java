package me.kiiya.packettester.tasks;

import me.kiiya.packettester.PacketTester;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import java.util.LinkedList;

// IGNORE THIS CLASS
public class ArmorStandSpinTask implements Runnable {
    public static final LinkedList<Player> spinningPlayers = new LinkedList<>();
    private final ArmorStand as;
    private final Player p;

    public ArmorStandSpinTask(ArmorStand as, Player p) {
        this.as = as;
        this.p = p;
        spinningPlayers.add(p);
    }

    @Override
    public void run() {
        int z = 0;
        while (spinningPlayers.contains(p)) {
            for (int i = 0; i < 3; i++) {
                for (int x = 0; x < 60; x++) {
                    as.setHeadPose(as.getHeadPose().add(0, -0.005, 0));
                }
                z++;
                if (z == 3) {
                    Bukkit.getScheduler().runTaskLater(PacketTester.getInstance(), () -> {
                        spinningPlayers.remove(p);
                    }, 40L);
                }
            }
        }
    }
}
