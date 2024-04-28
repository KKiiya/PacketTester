package me.kiiya.packettester;

import me.kiiya.packettester.commands.PekoraPetCommand;
import me.kiiya.packettester.commands.RecordingCommand;
import me.kiiya.packettester.commands.VehicleDismountCommand;
import me.kiiya.packettester.commands.VehicleSummonCommand;
import me.kiiya.packettester.packets.Injector;
import me.kiiya.packettester.packets.InjectorHandler;
import org.bukkit.plugin.java.JavaPlugin;

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
