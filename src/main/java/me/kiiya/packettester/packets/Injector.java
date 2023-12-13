package me.kiiya.packettester.packets;

import io.netty.channel.*;
import me.kiiya.packettester.events.PacketReadEvent;
import me.kiiya.packettester.events.PacketWriteEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

// This class is responsible for injecting players.
public class Injector {
    // Player to inject.
    private final Player player;

    // Constructor.
    protected Injector(Player p) {
        this.player = p;
        this.inject();
    }

    // Inject the player.
    private void inject() {
        // Create a new channel handler.
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            // Override channelRead and channelWrite to call the PacketReadEvent and PacketWriteEvent.
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                // Call the PacketReadEvent.
                PacketReadEvent event = new PacketReadEvent(player, msg);
                Bukkit.getPluginManager().callEvent(event);

                // If the event is cancelled, the packet won't be read.
                if (event.isCancelled()) return;

                // Call the original channelRead.
                super.channelRead(ctx, msg);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                // Call the PacketWriteEvent.
                PacketWriteEvent event = new PacketWriteEvent(player, msg);
                Bukkit.getPluginManager().callEvent(event);

                // If the event is cancelled, the packet won't be written.
                if (event.isCancelled()) return;

                // Call the original channelWrite.
                super.write(ctx, event.getPacket(), promise);
            }
        };
        // Add the channel handler to the player's channel pipeline.
        ChannelPipeline pipeline = ((CraftPlayer)this.player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", this.player.getName(), channelDuplexHandler);
    }

    // Uninject the player.
    public void uninject() {
        Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    // Get the player.
    public Player getPlayer() {
        return this.player;
    }
}
