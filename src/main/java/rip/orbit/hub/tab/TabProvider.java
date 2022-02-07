package rip.orbit.hub.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import cc.fyre.proton.tab.provider.LayoutProvider;
import gg.maiko.queue.shared.server.ServerData;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.hub.uHub;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

// ┃
@AllArgsConstructor
public class TabProvider implements LayoutProvider {

    @Override
    public TabLayout provide(Player player) {
        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());

        TabLayout layout = TabLayout.create(player);
        layout.set(Tab.LEFT, 3, "&6Rank:");
        layout.set(Tab.LEFT, 4, profile.getActiveRank().getFancyName());

        layout.set(Tab.MIDDLE, 0, "&6&lOrbit Network");
        layout.set(Tab.MIDDLE, 1, "&f" + uHub.getInstance().getPlayerCountBungee() + " &7players online");

        layout.set(Tab.RIGHT, 3, "&6Server:");
        layout.set(Tab.RIGHT, 4, Nebula.getInstance().getConfig().getString("server.name"));

        if (ServerData.getByName("Kits") != null) {
            layout.set(Tab.LEFT, 7, "&6Kits");
            layout.set(Tab.LEFT, 8, "&7Online: &6" + ServerData.getByName("Kits").getOnlinePlayers() + "&7/&6" + ServerData.getByName("Kits").getMaximumPlayers());
            layout.set(Tab.LEFT, 9, "&7Status: &6" + serverStatus("Kits"));
        } else {
            layout.set(Tab.LEFT, 7, "&6Kits");
            layout.set(Tab.LEFT, 8, "&7Online: &60&7/&6500");
            layout.set(Tab.LEFT, 9, "&7Status: &6" + serverStatus("Kits"));
        }

        if (ServerData.getByName("HCF") != null) {
            layout.set(Tab.MIDDLE, 7, "&6HCF");
            layout.set(Tab.MIDDLE, 8, "&7Online: &6" + ServerData.getByName("HCF").getOnlinePlayers() + "&7/&6" + ServerData.getByName("HCF").getMaximumPlayers());
            layout.set(Tab.MIDDLE, 9, "&7Status: &6" + serverStatus("HCF"));
        } else {
            layout.set(Tab.MIDDLE, 7, "&6HCF");
            layout.set(Tab.MIDDLE, 8, "&7Online: &60&7/&6500");
            layout.set(Tab.MIDDLE, 9, "&7Status: &6" + serverStatus("HCF"));
        }

        if (ServerData.getByName("Practice") != null) {
            layout.set(Tab.RIGHT, 7, "&6Practice");
            layout.set(Tab.RIGHT, 8, "&7Online: &6" + ServerData.getByName("Practice").getOnlinePlayers() + "&7/&6" + ServerData.getByName("Practice").getMaximumPlayers());
            layout.set(Tab.RIGHT, 9, "&7Status: &6" + serverStatus("Practice"));
        } else {
            layout.set(Tab.MIDDLE, 7, "&6Practice");
            layout.set(Tab.MIDDLE, 7, "&7Online: &60&7/&6500");
            layout.set(Tab.MIDDLE, 9, "&7Status: &6" + serverStatus("Practice"));
        }

        return layout;
    }

    public String serverStatus(String string) {
        ServerData server = ServerData.getByName(string);

        if (server == null) {
            return ChatColor.RED + "Offline";
        }

        if (server.isWhitelisted()) {
            return ChatColor.WHITE + "Whitelisted";
        } else if (!server.isOnline()) {
            return ChatColor.RED + "Offline";
        } else {
            return ChatColor.GREEN + "Online";
        }
    }
}
