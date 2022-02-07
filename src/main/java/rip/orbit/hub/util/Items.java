package rip.orbit.hub.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hub.uHub;

public class Items {

    public static ItemStack visibility(Player player) {
        short visibilityDurability;
        String visibilityName;

        if (uHub.getInstance().isHidingPlayers(player)) {
            visibilityDurability = (short) 10;
            visibilityName = Style.GOLD + Style.BOLD + "Show Players";
        } else {
            visibilityDurability = (short) 8;
            visibilityName = Style.GOLD + Style.BOLD + " Hide Players ";
        }

        return new ItemBuilder(Material.INK_SACK)
                .name(visibilityName)
                .durability(8)
                .build();
    }

    public static ItemStack selector = new ItemBuilder(Material.WATCH)
            .name(Color.msg("&6&lServer Selector"))
            .build();

    public static ItemStack profile = new ItemBuilder(Material.ENCHANTED_BOOK)
            .name(Color.msg("&6&lYour Profile"))
            .build();

    public static ItemStack hubSelector = new ItemBuilder(Material.BOOK)
            .name(Style.GOLD + Style.BOLD + "Hub Selector")
            .build();

    public static ItemStack enderbutt = new ItemBuilder(Material.ENDER_PEARL)
            .name(Color.msg("&6&LEnderpearls"))
            .build();

}