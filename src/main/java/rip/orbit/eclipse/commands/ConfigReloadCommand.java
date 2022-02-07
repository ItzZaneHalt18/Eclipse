package rip.orbit.eclipse.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import cc.fyre.proton.command.Command;
import rip.orbit.eclipse.Eclipse;

public class ConfigReloadCommand {

    @Command(names = {"uhreload"}, permission = "op", hidden = true)
    public static void config(CommandSender sender) {

        Eclipse.getInstance().reloadConfig();
        Eclipse.getInstance().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "uHub Configuration Reloaded.");
    }

}
