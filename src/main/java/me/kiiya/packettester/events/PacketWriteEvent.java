package me.kiiya.packettester.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

// This is the event that is called when a packet is written to the server.
public class PacketWriteEvent extends Event {
    public static final HandlerList handlers = new HandlerList();

    // The player that sent the packet.
    private final Player player;
    // The packet that was sent.
    private Object packet;
    // Whether the event is cancelled.
    private boolean cancelled = false;

    public PacketWriteEvent(Player player, Object packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    public Object getPacket() {
        return packet;
    }

    public void setPacket(Object packet) {
        this.packet = packet;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
