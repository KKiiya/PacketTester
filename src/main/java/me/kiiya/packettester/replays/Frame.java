package me.kiiya.packettester.replays;

import org.bukkit.Location;
import org.bukkit.entity.Item;
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
    private boolean isSneaking;
    private boolean isHitting;
    private boolean isPlacing;
    private boolean isBlocking;

    public Frame(Location location, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack itemInHand, boolean isSneaking, boolean isHitting, boolean isPlacing, boolean isBlocking) {
        this.location = location;
        this.blockLocation = null;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.itemInHand = itemInHand;
        this.pickupItem = null;
        this.dropItem = null;
        this.isSneaking = isSneaking;
        this.isHitting = isHitting;
        this.isPlacing = isPlacing;
        this.isBlocking = isBlocking;
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
        this.pickupItem = pickupItem;
    }

    public void setDropItem(Item dropItem) {
        this.dropItem = dropItem;
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

    public boolean isSneaking() {
        return isSneaking;
    }

    public boolean isHitting() {
        return isHitting;
    }

    public boolean isPlacing() {
        return isPlacing;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setHitting(boolean hitting) {
        isHitting = hitting;
    }

    public void setPlacing(boolean placing) {
        isPlacing = placing;
    }

    public void setSneaking(boolean sneaking) {
        isSneaking = sneaking;
    }

    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }
}
