package me.kiiya.packettester.replays;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Frame {
    private final Location location;
    private ItemStack itemInHand;
    private boolean isSneaking;
    private boolean isHitting;
    private boolean isPlacing;
    private boolean isBlocking;

    public Frame(Location location, boolean isSneaking, boolean isHitting, boolean isPlacing, boolean isBlocking, ItemStack itemInHand) {
        this.location = location;
        this.itemInHand = itemInHand;
        this.isSneaking = isSneaking;
        this.isHitting = isHitting;
        this.isPlacing = isPlacing;
        this.isBlocking = isBlocking;
    }

    public Location getLocation() {
        return location;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
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
