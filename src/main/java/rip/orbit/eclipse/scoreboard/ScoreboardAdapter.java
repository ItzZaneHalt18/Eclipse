package rip.orbit.eclipse.scoreboard;

import cc.fyre.proton.scoreboard.config.ScoreboardConfiguration;
import cc.fyre.proton.scoreboard.construct.TitleGetter;
import gg.maiko.queue.shared.queue.Queue;
import gg.maiko.queue.shared.queue.QueuePlayer;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import cc.fyre.proton.util.TimeUtils;
import org.bukkit.entity.Player;
import rip.orbit.eclipse.Eclipse;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.profile.Profile;
import rip.orbit.nebula.profile.attributes.punishment.impl.Punishment;
import rip.orbit.nebula.rank.Rank;

import java.util.PriorityQueue;

@AllArgsConstructor
public class ScoreboardAdapter {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration cfg = new ScoreboardConfiguration();

        cfg.setTitleGetter(new TitleGetter() {
            @Override
            public String getTitle(Player player) {
                return Eclipse.getInstance().getConfig().getString("scoreboard.title");
            }
        });

        cfg.setScoreGetter((l, player) -> {
            Queue queue = Queue.getByPlayer(player.getUniqueId());

            Profile profile = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId());
            Rank rank = profile.getActiveRank();
            if (queue == null) {
                for (String line : Eclipse.getInstance().getConfig().getStringList("scoreboard.normal")) {
                    if (rank == null) {
                        line = line.replaceAll("%rank%", ChatColor.WHITE + "Unknown");
                    } else if (profile.getServerProfile().isVIPStatus()) {
                        line = line.replaceAll("%rank%",  profile.getServerProfile().getVipStatusColor() + "✪" + rank.getFancyName());
                    } else {
                        line = line.replaceAll("%rank%", Eclipse.getInstance().getConfig().getBoolean("scoreboard.rankcolored") ? rank.getFancyName() : rank.getName());
                    }
                    line = line.replaceAll("%players%", "" + Eclipse.getInstance().getPlayerCountBungee());
//                        line = line.replaceAll("%duration%", getTimeRemaining(profile.getCurrentGrant()));
                    l.add(line);
                }
            } else {
                for (String line : Eclipse.getInstance().getConfig().getStringList("scoreboard.queue")) {
                    if (rank == null) {
                        line = line.replaceAll("%rank%", ChatColor.WHITE + "Unknown");
                    } else {
                        line = line.replaceAll("%rank%", Eclipse.getInstance().getConfig().getBoolean("scoreboard.rankcolored") ? rank.getFancyName() : rank.getName());
                        line = line.replaceAll("%players%", "" + Eclipse.getInstance().getPlayerCountBungee());
                        line = line.replaceAll("%queue%", queue.getName()).replaceAll("%place%", queue.getPosition(player.getUniqueId()) + "").replaceAll("%total%", queue.getPlayers().size() + "");
                        line = line.replaceAll("%queue-eta%", formattedETA(player, queue));
                        l.add(line);
                    }
                }
            }
            return null;
        });

        return cfg;
    }

    public static String formattedETA(Player player, Queue queue) {
        int pos = queue.getPosition(player.getUniqueId());
        int size = queue.getPlayers().size();
        int estimated = size - pos;

        if (estimated == 0) {
            return "1 second";
        }

        return TimeUtils.formatIntoDetailedString(estimated);
    }
}