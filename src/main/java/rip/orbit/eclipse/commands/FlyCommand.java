package rip.orbit.eclipse.commands;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FlyCommand {

    @Command(names = { "fly" }, permission = "uhub.fly", description = "Toggle a player's fly mode")
    public static void fly(Player sender, @Parameter(name = "player", defaultValue = "self") Player target) {
        if (!sender.equals(target) && !sender.hasPermission("uhub.fly.other")) {
            sender.sendMessage(ChatColor.RED + "No permission to set other player's fly mode.");
            return;
        }
        target.setAllowFlight(!target.getAllowFlight());
        if (!sender.equals(target)) {
            sender.sendMessage(target.getDisplayName() + ChatColor.WHITE + " is " + (!target.getAllowFlight() ? ChatColor.RED + "no longer " : ChatColor.GREEN + "now ") + ChatColor.WHITE + "allowed to fly.");
        }
        target.sendMessage(ChatColor.WHITE + "You are " + (!target.getAllowFlight() ? ChatColor.RED + "no longer " : ChatColor.GREEN + "now ") + ChatColor.WHITE + "allowed to fly.");
    }
}
