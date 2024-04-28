package me.kiiya.packettester.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordTask implements Runnable {
    public static final HashMap<Player, Integer> tasks = new HashMap<>();
    public static final HashMap<Integer, RecordTask> recordTasks = new HashMap<>();
    public static final HashMap<Player, List<Location>> recordings = new HashMap<>();
    private final Player player;
    private final List<Location> locations;

    public RecordTask(Player player) {
        this.player = player;
        this.locations = new ArrayList<>();
    }

    @Override
    public void run() {
        Location location = player.getLocation();
        locations.add(location);
    }

    public List<Location> getResult() {
        return locations;
    }
}
