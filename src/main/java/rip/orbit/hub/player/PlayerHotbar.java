package rip.orbit.hub.player;

import rip.orbit.hub.util.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

public class PlayerHotbar {

    public static void give(Player player) {
        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());
        player.getInventory().setContents(new ItemStack[36]);
        player.getInventory().setItem(2, Items.selector);
//        player.getInventory().setItem(1, Items.visibility(player));
//        player.getInventory().setItem(4, Items.cosmetics);
        player.getInventory().setItem(4, Items.profile);
        player.getInventory().setItem(6, Items.enderbutt);
//        player.getInventory().setItem(7, Items.hubSelector);
        player.updateInventory();
    }

}