package rip.orbit.hub.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import rip.orbit.hub.uHub;
import rip.orbit.nebula.util.CC;

public class BuildCommand {

    @Command(names = {"build", "buildmode"}, permission = "uhub.admin", description = "Toggle build mode")
    public static void buildmode(Player sender) {
        if (sender.hasMetadata("build")) {
            sender.removeMetadata("build", uHub.getInstance());
            sender.sendMessage(CC.translate("&6Build&7: &cDisabled"));
        } else {
            sender.setMetadata("build", new FixedMetadataValue(uHub.getInstance(), "build"));
            sender.sendMessage(CC.translate("&6Build&7: &aEnabled"));
        }
    }
}
