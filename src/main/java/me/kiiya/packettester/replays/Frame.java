package me.kiiya.packettester.replays;

import me.kiiya.packettester.PacketTester;
import net.minecraft.server.v1_8_R3.EntityItem;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Frame {
    private final Location location;
    private Location blockLocation;
    private ItemStack itemInHand;
    private Item dropItem;
    private Item pickupItem;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private boolean isHitting;
    private boolean isPlacing;
    private boolean isDamaged;
    private boolean isBlocking;
    private boolean isSneaking;

    public Frame(Player player) {
        this.location = player.getLocation();
        this.blockLocation = null;
        this.helmet = player.getInventory().getHelmet();
        this.chestplate = player.getInventory().getChestplate();
        this.leggings = player.getInventory().getLeggings();
        this.boots = player.getInventory().getBoots();
        this.itemInHand = player.getItemInHand();
        this.pickupItem = null;
        this.dropItem = null;
        this.isHitting = false;
        this.isPlacing = false;
        this.isDamaged = false;
        this.isSneaking = player.isSneaking();
        this.isBlocking = player.isBlocking();
    }

    public Location getLocation() {
        return location;
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

    public void setBlockLocation(Location blockLocation) {
        this.blockLocation = blockLocation;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public Item getPickupItem() {
        return pickupItem;
    }

    public Item getDropItem() {
        return dropItem;
    }

    public void setPickupItem(Item pickupItem) {
        EntityItem entityItem = new EntityItem(((CraftItem) pickupItem).getHandle().getWorld(), pickupItem.getLocation().getX(), pickupItem.getLocation().getY(), pickupItem.getLocation().getZ(), CraftItemStack.asNMSCopy(pickupItem.getItemStack()));
        this.pickupItem = new CraftItem(((CraftServer) PacketTester.getInstance().getServer()), entityItem);
    }

    public void setDropItem(Item dropItem) {
        EntityItem entityItem = new EntityItem(((CraftItem) dropItem).getHandle().getWorld(), dropItem.getLocation().getX(), dropItem.getLocation().getY(), dropItem.getLocation().getZ(), CraftItemStack.asNMSCopy(dropItem.getItemStack()));
        this.dropItem = new CraftItem(((CraftServer) PacketTester.getInstance().getServer()), entityItem);
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public void setItemInHand(ItemStack itemInHand) {
        this.itemInHand = itemInHand;
    }


    public boolean isHitting() {
        return isHitting;
    }

    public boolean isPlacing() {
        return isPlacing;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public void setHitting(boolean hitting) {
        isHitting = hitting;
    }

    public void setPlacing(boolean placing) {
        isPlacing = placing;
    }

    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    public void setSneaking(boolean sneaking) {
        isSneaking = sneaking;
    }
}
