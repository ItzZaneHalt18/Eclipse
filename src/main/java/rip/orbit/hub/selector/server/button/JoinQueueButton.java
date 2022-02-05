package rip.orbit.hub.selector.server.button;

import cc.fyre.proton.menu.Button;
import com.google.gson.JsonObject;
import gg.maiko.queue.shared.queue.Queue;
import gg.maiko.queue.shared.queue.QueuePlayer;
import gg.maiko.queue.shared.server.ServerData;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.orbit.hub.uHub;
import rip.orbit.hub.util.ItemBuilder;
import rip.orbit.hub.util.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class JoinQueueButton extends Button {

    private final ServerData serverData;
    private final String name;
    private final Material material;
    private final int durability;
    private final List<String> description;

    @Override
    public String getName(Player player) {
        return null;
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return null;
    }

    public ItemStack getButtonItem(Player player) {
        if (this.serverData == null) {
            List<String> lore = new ArrayList<>();
            for (String line : description) {
                line = line.replaceAll("%status%", serverStatus())
                        .replaceAll("%online%", "0");
                lore.add(Style.GRAY + line);
            }

            return new ItemBuilder(material)
                    .name(Style.RED + Style.BOLD + name)
                    .durability(durability)
                    .lore(lore)
                    .build();
        }

        Material material;
        List<String> description;
        int durability;

        if (this.serverData.getName().contains(uHub.getInstance().getConfig().getString("queues." + this.name + ".id")) && this.serverData.isOnline()) {
            material = Material.valueOf(uHub.getInstance().getConfig().getString("queues." + this.name + ".item.material"));
            durability = uHub.getInstance().getConfig().getInt("queues." + this.name + ".item.data");
            description = uHub.getInstance().getConfig().getStringList("queues." + this.name + ".description");
        } else {
            material = Material.REDSTONE_BLOCK;
            durability = 0;
            description = uHub.getInstance().getConfig().getStringList("queues." + this.name + ".description");
        }

        List<String> lore = new CopyOnWriteArrayList<>();
        PriorityQueue<QueuePlayer> queue = Queue.getByName(player.getName()).getPlayers();

        for (String line : description) {
            line = line.replaceAll("%status%", serverStatus())
                    .replaceAll("%online%", String.valueOf(serverData.isOnline()));

            lore.add(Style.GRAY + line);
        }

        String name;
        if (this.serverData.isOnline()) {
            name = Style.GREEN + Style.BOLD + this.name;
        } else if (this.serverData.isWhitelisted()) {
            name = Style.YELLOW + Style.BOLD + this.name;
        } else {
            name = Style.RED + Style.BOLD + this.name;
        }

        return new ItemBuilder(material)
                .name(name)
                .lore(lore)
                .durability(durability)
                .build();
    }


    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (this.serverData == null) {
            return;
        }

        Queue queue = Queue.getByName(player.getName());
        if (clickType == ClickType.LEFT && this.serverData.isOnline()) {
            player.closeInventory();
            player.performCommand("joinqueue " + this.serverData.getName());

        } else if (clickType == ClickType.RIGHT && this.serverData.isOnline() && queue != null) {
            JsonObject data = new JsonObject();
            data.addProperty("uuid", player.getUniqueId().toString());
        }
    }

    public String serverStatus() {
        if (serverData == null) {
            return ChatColor.RED + "Offline";
        }
        return serverData.isOnline() && !serverData.isWhitelisted() ? ChatColor.GREEN + "Online" : serverData.isWhitelisted() ? ChatColor.YELLOW + "Whitelisted" : ChatColor.RED + "Offline";
    }


}
