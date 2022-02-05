package rip.orbit.hub.commands;

import rip.orbit.hub.uHub;
import cc.fyre.proton.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpawnCommand {

    @Command(names = { "spawn" }, permission = "", description = "Teleports you to spawn!")
    public static void spawn(Player player) {
        player.teleport(uHub.getInstance().getSpawnLocation());
        player.sendMessage(ChatColor.GREEN + "Teleported!");
    }
}