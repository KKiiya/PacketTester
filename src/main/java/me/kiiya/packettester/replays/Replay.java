package me.kiiya.packettester.replays;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.listeners.BlockPlaceListener;
import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Replay {
    public static final HashMap<Player, Replay> replays = new HashMap<>();
    public static final List<Player> recordingPlayers = new ArrayList<>();
    private final Player player;
    private final List<Frame> frames;
    private int recordingTaskId;

    public Replay(Player player, List<Frame> frames) {
        this.player = player;
        this.frames = frames;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public void play(Player player) {
        EntityPlayer npc = Utility.createNpc(player, frames.get(0).getLocation());
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            Bukkit.getScheduler().runTaskLaterAsynchronously(PacketTester.getInstance(), () -> {
                Location location = frame.getLocation();
                npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(npc);
                PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(npc, (byte) ((location.getYaw() * 256.0F) / 360.0F));
                PacketPlayOutEntityEquipment equipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), 0, CraftItemStack.asNMSCopy(frame.getItemInHand() == null ? new ItemStack(Material.AIR) : frame.getItemInHand()));
                PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((location.getYaw() * 256.0F) / 360.0F), (byte) ((location.getPitch() * 256.0F) / 360.0F), true);
                npc.setSneaking(frame.isSneaking());
                PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true);
                connection.sendPacket(metadataPacket);

                if (frame.isHitting()) {
                    PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(npc, frame.isHitting() ? 0 : 1);
                    connection.sendPacket(animationPacket);
                }
                if (frame.isPlacing()) {
                    Block block = BlockPlaceListener.blocks.get(player).get(0);
                    BlockPlaceListener.blocks.get(player).remove(0);
                    BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
                    PacketPlayOutBlockChange blockChangePacket = new PacketPlayOutBlockChange(((CraftWorld) block.getWorld()).getHandle(), blockPosition);
                    PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(npc, 0);
                    connection.sendPacket(blockChangePacket);
                    connection.sendPacket(animationPacket);
                }
                connection.sendPacket(packet);
                connection.sendPacket(headRotation);
                connection.sendPacket(packetPlayOutEntityLook);
                connection.sendPacket(equipmentPacket);
            }, i);
        }
    }

    public void startRecording() {
        replays.put(player, this);
        recordingPlayers.add(player);
        BlockPlaceListener.blocks.put(player, new ArrayList<>());
        recordingTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(PacketTester.getInstance(), () -> {
            frames.add(new Frame(player.getLocation(), player.isSneaking(), false, false, player.isBlocking(), player.getItemInHand()));
        }, 0, 1).getTaskId();
    }

    public void stopRecording() {
        recordingPlayers.remove(player);
        Bukkit.getScheduler().cancelTask(recordingTaskId);
    }
}
