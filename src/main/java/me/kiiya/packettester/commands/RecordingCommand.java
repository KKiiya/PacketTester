package me.kiiya.packettester.commands;

import me.kiiya.packettester.PacketTester;
import me.kiiya.packettester.tasks.RecordTask;
import me.kiiya.packettester.tasks.PlayRecordTask;
import me.kiiya.packettester.utils.Utility;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class RecordingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("Usage: /recording <start/stop/play>");
            return false;
        }
        Player player = (Player) commandSender;
        RecordTask recordTask = new RecordTask(player);
        int taskId = -1;
        if (strings[0].equalsIgnoreCase("start")) {
            taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(PacketTester.getInstance(), recordTask, 0, 1).getTaskId();
            RecordTask.tasks.put(player, taskId);
            RecordTask.recordTasks.put(taskId, recordTask);
        } else if (strings[0].equalsIgnoreCase("stop")) {
            if (RecordTask.tasks.containsKey(player)) {
                taskId = RecordTask.tasks.get(player);
                Bukkit.getScheduler().cancelTask(taskId);
                RecordTask finalTask = RecordTask.recordTasks.remove(taskId);
                RecordTask.tasks.put(player, -1);
                RecordTask.recordings.put(player, finalTask.getResult());
            }
        } else if (strings[0].equalsIgnoreCase("play")) {
            if (RecordTask.recordings.containsKey(player)) {
                List<Location> recordings = RecordTask.recordings.get(player);
                EntityPlayer entityPlayer = Utility.createNpc(player, recordings.get(0));
                PlayRecordTask playRecordingTask = new PlayRecordTask(entityPlayer, recordings);
                Bukkit.getScheduler().runTask(PacketTester.getInstance(), playRecordingTask);
            }
        }
        else {
            commandSender.sendMessage("Usage: /recording <start/stop/play>");
        }
        return false;
    }
}
