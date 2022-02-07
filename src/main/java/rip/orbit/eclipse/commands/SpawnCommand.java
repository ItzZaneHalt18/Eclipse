package rip.orbit.eclipse.commands;

import rip.orbit.eclipse.Eclipse;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpawnCommand {

    @Command(names = { "spawn" }, permission = "", description = "Teleports you to spawn!")
    public static void spawn(Player player) {
        player.teleport(Eclipse.getInstance().getSpawnLocation());
        player.sendMessage(ChatColor.GREEN + "Teleported!");
    }
}