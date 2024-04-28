package me.kiiya.packettester.replays;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
        for (Frame frame : frames) {
            int delay = frames.indexOf(frame);
            Bukkit.getScheduler().runTaskLaterAsynchronously(PacketTester.getInstance(), () -> {
                Location location = frame.getLocation();
                npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
                npc.setSneaking(frame.isSneaking());
                PacketPlayOutEntityTeleport movementPacket = new PacketPlayOutEntityTeleport(npc);
                PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(npc, (byte) ((location.getYaw() * 256.0F) / 360.0F));
                PacketPlayOutEntity.PacketPlayOutEntityLook packetPlayOutEntityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) ((location.getYaw() * 256.0F) / 360.0F), (byte) ((location.getPitch() * 256.0F) / 360.0F), true);
                PacketPlayOutEntityEquipment handEquipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), 0, CraftItemStack.asNMSCopy(frame.getItemInHand() == null ? new ItemStack(Material.AIR) : frame.getItemInHand()));
                PacketPlayOutEntityEquipment helmetEquipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), 4, CraftItemStack.asNMSCopy(frame.getHelmet() == null ? new ItemStack(Material.AIR) : frame.getHelmet()));
                PacketPlayOutEntityEquipment chestplateEquipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), 3, CraftItemStack.asNMSCopy(frame.getChestplate() == null ? new ItemStack(Material.AIR) : frame.getChestplate()));
                PacketPlayOutEntityEquipment leggingsEquipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), 2, CraftItemStack.asNMSCopy(frame.getLeggings() == null ? new ItemStack(Material.AIR) : frame.getLeggings()));
                PacketPlayOutEntityEquipment bootsEquipmentPacket = new PacketPlayOutEntityEquipment(npc.getId(), 1, CraftItemStack.asNMSCopy(frame.getBoots() == null ? new ItemStack(Material.AIR) : frame.getBoots()));
                PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(npc.getId(), npc.getDataWatcher(), true);

                if (frame.isHitting()) {
                    PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(npc, frame.isHitting() ? 0 : 1);
                    connection.sendPacket(animationPacket);
                    if (frame.getBlockLocation() != null) {
                        Block block = location.getWorld().getBlockAt(frame.getBlockLocation());
                        Bukkit.getScheduler().runTask(PacketTester.getInstance(), () -> {
                            block.setType(Material.AIR);
                        });
                        BlockPosition blockPosition = new BlockPosition(frame.getBlockLocation().getBlockX(), frame.getBlockLocation().getBlockY(), frame.getBlockLocation().getBlockZ());
                        WorldServer world = ((CraftWorld) frame.getBlockLocation().getWorld()).getHandle();
                        PacketPlayOutBlockChange blockChangePacket = new PacketPlayOutBlockChange(world, blockPosition);
                        connection.sendPacket(blockChangePacket);
                    }
                }
                if (frame.isPlacing() && frame.getBlockLocation() != null && frame.getItemInHand() != null && frame.getItemInHand().getType().isBlock()) {
                    Block block = location.getWorld().getBlockAt(frame.getBlockLocation());
                    PacketPlayOutAnimation animationPacket = new PacketPlayOutAnimation(npc, 0);
                    Bukkit.getScheduler().runTask(PacketTester.getInstance(), () -> {
                        block.setType(frame.getItemInHand().getType());
                        block.setData(frame.getItemInHand().getData().getData());
                    });
                    EntityItem drop = new EntityItem(((CraftWorld) block.getWorld()).getHandle(), block.getLocation().getX(), block.getLocation().getY(), block.getLocation().getZ(), CraftItemStack.asNMSCopy(frame.getItemInHand()));
                    CraftItem craftItem = new CraftItem(((CraftServer) PacketTester.getInstance().getServer()), drop);
                    craftItem.setPickupDelay(0);
                    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(drop, 2);
                    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(drop.getId(), drop.getDataWatcher(), true);
                    PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(drop.getId(), drop.motX, drop.motY, drop.motZ);
                    PacketPlayOutBlockChange blockChangePacket = new PacketPlayOutBlockChange(((CraftWorld) frame.getBlockLocation().getWorld()).getHandle(), new BlockPosition(frame.getBlockLocation().getBlockX(), frame.getBlockLocation().getBlockY(), frame.getBlockLocation().getBlockZ()));
                    connection.sendPacket(animationPacket);
                    connection.sendPacket(blockChangePacket);
                    connection.sendPacket(spawnEntity);
                    connection.sendPacket(entityMetadata);
                    connection.sendPacket(velocityPacket);
                }
                if (frame.getPickupItem() != null) {
                    PacketPlayOutCollect collectPacket = new PacketPlayOutCollect(frame.getPickupItem().getEntityId(), npc.getId());
                    connection.sendPacket(collectPacket);
                }
                if (frame.getDropItem() != null) {
                    EntityItem drop = new EntityItem(((CraftWorld) frame.getLocation().getWorld()).getHandle(), frame.getLocation().getX(), frame.getLocation().getY(), frame.getLocation().getZ(), CraftItemStack.asNMSCopy(frame.getDropItem().getItemStack()));
                    CraftItem craftItem = new CraftItem(((CraftServer) PacketTester.getInstance().getServer()), drop);
                    craftItem.setPickupDelay(0);
                    PacketPlayOutSpawnEntity spawnEntity = new PacketPlayOutSpawnEntity(drop, 2);
                    PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(drop.getId(), drop.getDataWatcher(), true);
                    PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(drop.getId(), drop.motX, drop.motY, drop.motZ);
                    connection.sendPacket(spawnEntity);
                    connection.sendPacket(entityMetadata);
                    connection.sendPacket(velocityPacket);
                }
                connection.sendPacket(metadataPacket);
                connection.sendPacket(movementPacket);
                connection.sendPacket(headRotation);
                connection.sendPacket(packetPlayOutEntityLook);
                connection.sendPacket(handEquipmentPacket);
                connection.sendPacket(helmetEquipmentPacket);
                connection.sendPacket(chestplateEquipmentPacket);
                connection.sendPacket(leggingsEquipmentPacket);
                connection.sendPacket(bootsEquipmentPacket);
            }, delay);
        }
    }

    public void startRecording() {
        replays.put(player, this);
        recordingPlayers.add(player);
        recordingTaskId = Bukkit.getScheduler().runTaskTimerAsynchronously(PacketTester.getInstance(), () -> {
            PlayerInventory inventory = player.getInventory();
            frames.add(new Frame(player.getLocation(), inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots(), player.getItemInHand(), player.isSneaking(), false, false, player.isBlocking()));
        }, 0, 1).getTaskId();
    }

    public void stopRecording() {
        recordingPlayers.remove(player);
        Bukkit.getScheduler().cancelTask(recordingTaskId);
    }
}
