package rip.orbit.eclipse.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import cc.fyre.proton.tab.provider.LayoutProvider;
import gg.maiko.queue.shared.server.ServerData;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import rip.orbit.eclipse.Eclipse;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;

// ┃
@AllArgsConstructor
public class TabProvider implements LayoutProvider {

    @Override
    public TabLayout provide(Player player) {
        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());

        TabLayout layout = TabLayout.create(player);
        layout.set(Tab.LEFT, 2, "&6Rank:");
        layout.set(Tab.LEFT, 3, profile.getActiveRank().getFancyName());
        layout.set(Tab.LEFT, 4, "&7");
        layout.set(Tab.LEFT, 5, "&6Expires:");
        layout.set(Tab.LEFT, 6, profile.getActiveGrant().getRemainingString() );

        layout.set(Tab.LEFT, 18,  "&9&lDiscord");
        layout.set(Tab.LEFT, 19,  "&7discord.orbit.rip");
        layout.set(Tab.MIDDLE, 18,  "&a&lStore");
        layout.set(Tab.MIDDLE, 19,  "&7donate.orbit.rip");
        layout.set(Tab.RIGHT, 18,  "&b&lTwitter");
        layout.set(Tab.RIGHT, 19,  "&7twitter.com/OrbitDotRIP");


        layout.set(Tab.MIDDLE, 0, "&6&lOrbit Network");
        if (Eclipse.getInstance().getPlayerCountBungee() == 1) {
            layout.set(Tab.MIDDLE, 1, "&f" + Eclipse.getInstance().getPlayerCountBungee() + " player connected");
        } else {
            layout.set(Tab.MIDDLE, 1, "&f" + Eclipse.getInstance().getPlayerCountBungee() + " players online");
        }

        layout.set(Tab.RIGHT, 2, "&6Name:");
        layout.set(Tab.RIGHT, 3, profile.getFancyName());
        layout.set(Tab.RIGHT, 4, "");
        layout.set(Tab.RIGHT, 5, "&6Server:");
        layout.set(Tab.RIGHT, 6, Nebula.getInstance().getConfig().getString("server.name"));

        if (ServerData.getByName("Kits") != null) {
            layout.set(Tab.LEFT, 9, "&6&lKits");
            layout.set(Tab.LEFT, 10, "&7&L┃ &6Online: &f" + ServerData.getByName("Kits").getOnlinePlayers() + "&7/&f" + ServerData.getByName("Kits").getMaximumPlayers());
            layout.set(Tab.LEFT, 11, "&7&L┃ &6Status: " + serverStatus("Kits"));
        } else {
            layout.set(Tab.LEFT, 9, "&6&lKits");
            layout.set(Tab.LEFT, 10, "&7&L┃ &6Online: &f0&7/&f500");
            layout.set(Tab.LEFT, 11, "&7&L┃ &6Status: " + serverStatus("Kits"));
        }

        if (ServerData.getByName("HCF") != null) {
            layout.set(Tab.MIDDLE, 9, "&6&lHCF");
            layout.set(Tab.MIDDLE, 10, "&7&L┃ &6Online: &f" + ServerData.getByName("HCF").getOnlinePlayers() + "&7/&f" + ServerData.getByName("HCF").getMaximumPlayers());
            layout.set(Tab.MIDDLE, 11, "&7&L┃ &6Status: " + serverStatus("HCF"));
        } else {
            layout.set(Tab.MIDDLE, 9, "&6&lHCF");
            layout.set(Tab.MIDDLE, 10, "&7&L┃ &6Online: &f0&7/&f500");
            layout.set(Tab.MIDDLE, 11, "&7&L┃ &6Status: " + serverStatus("HCF"));
        }

        if (ServerData.getByName("Practice") != null) {
            layout.set(Tab.RIGHT, 9, "&6&lPractice");
            layout.set(Tab.RIGHT, 10, "&7&L┃ &6Online: &f" + ServerData.getByName("Practice").getOnlinePlayers() + "&7/&f" + ServerData.getByName("Practice").getMaximumPlayers());
            layout.set(Tab.RIGHT, 11, "&7&L┃ &6Status: " + serverStatus("Practice"));
        } else {
            layout.set(Tab.MIDDLE, 9, "&6&lPractice");
            layout.set(Tab.MIDDLE, 10, "&7&L┃ &6Online: &f0&7/&f500");
            layout.set(Tab.MIDDLE, 11, "&7&L┃ &6Status: " + serverStatus("Practice"));
        }

        return layout;
    }

    public String serverStatus(String string) {
        ServerData server = ServerData.getByName(string);

        if (server == null) {
            return ChatColor.RED + "Offline";
        }

        if (server.isWhitelisted()) {
            return ChatColor.YELLOW + "Whitelisted";
        } else if (!server.isOnline()) {
            return ChatColor.RED + "Offline";
        } else {
            return ChatColor.GREEN + "Online";
        }
    }
}
