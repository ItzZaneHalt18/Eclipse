package rip.orbit.hub.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import cc.fyre.proton.util.PlayerUtils;
import cc.fyre.proton.util.UUIDUtils;
import rip.orbit.hub.uHub;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.rank.Rank;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.BiConsumer;

public class OnlinePlayersLayoutProvider implements Listener, BiConsumer<Player, TabLayout> {

    private Map<UUID, String> playersMap = generateNewTreeMap();

    public OnlinePlayersLayoutProvider() {
        Bukkit.getPluginManager().registerEvents(this, uHub.getInstance());
        Bukkit.getScheduler().runTaskTimerAsynchronously(uHub.getInstance(), this::rebuildCache, 0, 20 * 5);
    }

    @Override
    public void accept(Player player, TabLayout tabLayout) {

        int x = 0;
        int y = 0;

        boolean isStaff = player.hasPermission("basic.staff");
        for (Map.Entry<UUID, String> entry : playersMap.entrySet()) {
            if (x == 3) {
                x = 0;
                y++;
            }

            if (entry.getValue() == null) {
                continue;
            }

            tabLayout.set(x++, y, entry.getValue(), getPing(entry.getKey()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        playersMap.put(event.getPlayer().getUniqueId(), getName(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playersMap.remove(event.getPlayer().getUniqueId());
    }

    private void rebuildCache() {
        TreeMap<UUID, String> newTreeMap = generateNewTreeMap();

        Bukkit.getOnlinePlayers().forEach(player -> {
            newTreeMap.put(player.getUniqueId(), getName(player.getUniqueId()));
        });

        this.playersMap = newTreeMap;
    }

    private String getName(UUID uuid) {
        Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(uuid);
        if (profile == null) {
            return null;
        }

        Rank bestDisplayRank = profile.getActiveRank();

        return bestDisplayRank.getColor() + UUIDUtils.name(uuid);
    }

    public int getPing(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? -1 : Math.max(((PlayerUtils.getPing(player) + 5) / 10) * 10, 1);
    }

    private TreeMap<UUID, String> generateNewTreeMap() {
        return new TreeMap<UUID, String>(new Comparator<UUID>() {

            @Override
            public int compare(UUID first, UUID second) {
                Profile firstProfile = Nebula.getInstance().getProfileHandler().fromUuid(first);
                Profile secondProfile = Nebula.getInstance().getProfileHandler().fromUuid(second);

                if (firstProfile != null && secondProfile != null) {
                    int compare = Integer.compare(secondProfile.getActiveRank().getWeight().get(), firstProfile.getActiveRank().getWeight().get());
                    if (compare == 0) {
                        return tieBreaker(first, second);
                    }

                    return compare;
                } else if (firstProfile != null && secondProfile == null) {
                    return -1;
                } else if (firstProfile == null && secondProfile != null) {
                    return 1;
                } else {
                    return tieBreaker(first, second);
                }
            }

        });
    }

    private int tieBreaker(UUID first, UUID second) {
        Profile firstProfile = Nebula.getInstance().getProfileHandler().fromUuid(first);
        Profile secondProfile = Nebula.getInstance().getProfileHandler().fromUuid(second);

        if (firstProfile != null && secondProfile != null) {
            String firstName = UUIDUtils.name(first);
            String secondName = UUIDUtils.name(second);
            return firstName.compareTo(secondName);
        }
        return 0;
    }
}