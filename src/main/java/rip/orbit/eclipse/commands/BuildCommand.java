package rip.orbit.eclipse.commands;

import cc.fyre.proton.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import rip.orbit.eclipse.Eclipse;
import rip.orbit.nebula.util.CC;

public class BuildCommand {

    @Command(names = {"build", "buildmode"}, permission = "uhub.admin", description = "Toggle build mode")
    public static void buildmode(Player sender) {
        if (sender.hasMetadata("build")) {
            sender.removeMetadata("build", Eclipse.getInstance());
            sender.sendMessage(CC.translate("&6Build&7: &cDisabled"));
        } else {
            sender.setMetadata("build", new FixedMetadataValue(Eclipse.getInstance(), "build"));
            sender.sendMessage(CC.translate("&6Build&7: &aEnabled"));
        }
    }
}
