package rip.orbit.hub.commands;

import cc.fyre.proton.scoreboard.ScoreboardHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import cc.fyre.proton.command.Command;
import rip.orbit.hub.uHub;

public class ConfigReloadCommand {

    @Command(names = {"uhreload"}, permission = "op", hidden = true)
    public static void config(CommandSender sender) {

        uHub.getInstance().reloadConfig();
        uHub.getInstance().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "uHub Configuration Reloaded.");
    }

}
