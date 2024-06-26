package me.kiiya.packettester;

import me.kiiya.packettester.commands.PekoraPetCommand;
import me.kiiya.packettester.commands.RecordingCommand;
import me.kiiya.packettester.commands.VehicleDismountCommand;
import me.kiiya.packettester.commands.VehicleSummonCommand;
import me.kiiya.packettester.listeners.BlockPlaceListener;
import me.kiiya.packettester.packets.Injector;
import me.kiiya.packettester.packets.InjectorHandler;
import net.minecraft.server.v1_8_R3.Block;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public final class PacketTester extends JavaPlugin {

    // SINGLETON INSTANCE
    private static PacketTester instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // SETTING THE SINGLETON INSTANCE
        instance = this;

        // INITIALIZING THE INJECTOR HANDLER
        //InjectorHandler.init();

        // REGISTERING COMMANDS (This is not important)
        getCommand("vehicle").setExecutor(new VehicleSummonCommand());
        getCommand("dismount").setExecutor(new VehicleDismountCommand());
        getCommand("pekorapet").setExecutor(new PekoraPetCommand());
        getCommand("recording").setExecutor(new RecordingCommand());

        // REGISTERING LISTENERS (This is not important)
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // UNINJECT ALL INJECTIONS
        InjectorHandler.getInstance().getInjections().forEach(Injector::uninject);
    }

    // GETTING THE SINGLETON INSTANCE
    public static PacketTester getInstance() {
        return instance;
    }
}
