package me.kiiya.packettester.commands;

import me.kiiya.packettester.replays.Replay;
import me.kiiya.packettester.utils.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.ArrayList;

public class RecordingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("Usage: /recording <start/stop/play>");
            return false;
        }

        Player player = (Player) commandSender;

        if (strings[0].equalsIgnoreCase("start")) {
            Replay replay = new Replay(player, new ArrayList<>());
            replay.startRecording();
            player.sendMessage(Utility.c("&aRecording started!"));
        } else if (strings[0].equalsIgnoreCase("stop")) {
            if (Replay.replays.containsKey(player)) {
                Replay replay = Replay.replays.get(player);
                replay.stopRecording();
                player.sendMessage(Utility.c("&aRecording stopped!"));
            }
        } else if (strings[0].equalsIgnoreCase("play")) {
            if (Replay.replays.containsKey(player)) {
                Replay replay = Replay.replays.get(player);
                player.sendMessage(Utility.c("&aPlaying recording!"));
                replay.play(player);
            }
        }

        else {
            commandSender.sendMessage("Usage: /recording <start/stop/play>");
        }
        return true;
    }
}
