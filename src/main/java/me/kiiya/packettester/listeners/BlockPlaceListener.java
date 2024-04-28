package me.kiiya.packettester.listeners;

import me.kiiya.packettester.replays.Frame;
import me.kiiya.packettester.replays.Replay;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;

public class BlockPlaceListener implements Listener {
    public static final HashMap<Player, List<Block>> blocks = new HashMap<>();
    public static final HashMap<Player, List<Block>> originalBlocks = new HashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (Replay.recordingPlayers.contains(player)) {
            Frame frame = Replay.replays.get(player).getFrames().get(Replay.replays.get(player).getFrames().size() - 1);
            frame.setItemInHand(player.getItemInHand());
            frame.setPlacing(true);
            List<Block> blockList = blocks.get(player);
            List<Block> originalBlockList = originalBlocks.get(player);
            blockList.add(event.getBlock());
            blocks.put(player, blockList);
        }
    }

    @EventHandler
    public void onHit(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() != Action.LEFT_CLICK_AIR) return;

        if (Replay.recordingPlayers.contains(player)) {
            Frame frame = Replay.replays.get(player).getFrames().get(Replay.replays.get(player).getFrames().size() - 1);
            frame.setItemInHand(player.getItemInHand());
            frame.setHitting(true);
        }
    }
}
