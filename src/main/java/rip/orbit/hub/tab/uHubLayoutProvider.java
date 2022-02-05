package rip.orbit.hub.tab;

import cc.fyre.proton.tab.construct.TabLayout;
import cc.fyre.proton.tab.provider.LayoutProvider;
import org.bukkit.entity.Player;
import rip.orbit.hub.uHub;

import java.util.function.BiConsumer;

public final class uHubLayoutProvider implements LayoutProvider {

    static final int MAX_TAB_Y = 20;
    private static boolean testing = true;

    private final BiConsumer<Player, TabLayout> onlinePlayersLayoutProvider = new OnlinePlayersLayoutProvider();

    @Override
    public TabLayout provide(Player player) {
        if (uHub.getInstance() == null) return TabLayout.create(player);
        TabLayout tabLayout = TabLayout.create(player);

            onlinePlayersLayoutProvider.accept(player, tabLayout);

        return tabLayout;
    }

}