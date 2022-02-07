package rip.orbit.hub.selector.server;

import cc.fyre.proton.menu.Button;
import cc.fyre.proton.menu.Menu;
import gg.maiko.queue.bukkit.Queue;
import gg.maiko.queue.shared.server.ServerData;
import rip.orbit.hub.selector.server.button.JoinQueueButton;
import rip.orbit.hub.uHub;
import rip.orbit.hub.util.Style;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerSelectorMenu extends Menu {

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    public ServerSelectorMenu() {

    }
    @Override
    public String getTitle(Player player) {
        return Style.DARK_GRAY + "Server Selector";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for(String key : uHub.getInstance().getConfig().getConfigurationSection("queues").getKeys(false)) {
            String pkey = "queues." + key + ".";

            String name = uHub.getInstance().getConfig().getString(pkey + "name");
            String id = uHub.getInstance().getConfig().getString(pkey + "id");
            int slot = uHub.getInstance().getConfig().getInt(pkey + "slot");
            Material iMat = Material.REDSTONE_BLOCK;
            int iData = uHub.getInstance().getConfig().getInt(pkey + "item.data");
            List<String> desc = uHub.getInstance().getConfig().getStringList(pkey + "description");

//            buttons.put(slot, new JoinQueueButton(BridgeGlobal.getServerHandler().getServer(id), name, iMat, iData, desc));
            buttons.put(slot, new JoinQueueButton(ServerData.getByName(id), name, iMat, iData, desc));
        }

        return buttons;
    }

    @Override
    public int size(Player player) {
        return 45;
    }
}
