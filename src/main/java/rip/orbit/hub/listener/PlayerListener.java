package rip.orbit.hub.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.hub.player.PlayerHotbar;
import rip.orbit.hub.selector.server.ServerSelectorMenu;
import rip.orbit.hub.uHub;
import rip.orbit.hub.util.Color;
import rip.orbit.hub.util.Items;
import rip.orbit.hub.util.Style;
import rip.orbit.nebula.Nebula;
import rip.orbit.nebula.rank.Rank;
import rip.orbit.nebula.util.CC;

import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().hasMetadata("build")) {
            if (event.getTo().getX() >= 250 || event.getTo().getZ() >= 250) {
                event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
                event.setCancelled(true);
                event.getPlayer().sendMessage(Style.RED + "You have hit the border!");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        uHub.getInstance().getHidingPlayers().remove(event.getPlayer().getUniqueId());
        uHub.getInstance().getHideCooldown().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (event.getItem().isSimilar(Items.selector)) {
                new ServerSelectorMenu().openMenu(player);
                PlayerHotbar.give(player);
            } else if (event.getItem().isSimilar(Items.profile)) {
                player.performCommand("profile");
            }
        } else if (event.getItem() != null && event.getItem().getType().equals(Material.ENDER_PEARL)) {
            event.setCancelled(true);
            player.updateInventory();
        } else {
            if (!player.hasMetadata("build")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasMetadata("build"))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().hasMetadata("build"))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        e.setCancelled(true);
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Rank rank = Nebula.getInstance().getProfileHandler().fromUuid(player.getUniqueId()).getActiveRank();

        new BukkitRunnable() {
            @Override
            public void run() {
                boolean ena = uHub.getInstance().getConfig().getBoolean("Restricted-Hub.Enabled");
                if (ena) {
                    if (rank.getPermissions().contains("orbit.donor")) {
                        sendPlayerToServer(event.getPlayer(), "Rhub");
                    }
                }
            }
        }.runTaskLater(uHub.getInstance(), 20);

        event.setJoinMessage(null);

        player.setWalkSpeed(0.4F);
        player.setFlySpeed(0.1F);

        if (player.getOpenInventory() != null) {
            player.getOpenInventory().setCursor(null);
        }

        World world = Bukkit.getServer().getWorld("world");
        Location location = new Location(world, 0.5, 75, 0.5);
        player.teleport(location);
        player.setHealth(20.0D);
        player.setSaturation(100.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setFlying(false);
        player.setAllowFlight(true);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        player.getInventory().setHeldItemSlot(0);

        player.updateInventory();

        PlayerHotbar.give(player);

        List<String> messages = uHub.getInstance().getConfig().getStringList("joinmessage");
        messages.forEach(message -> player.sendMessage(Color.msg(message)));

    }

    public static void sendPlayerToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(uHub.getInstance(), "BungeeCord", out.toByteArray());
    }



    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMoveItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.hasMetadata("build")) {
            return;
        }

        if (event.getClickedInventory() == null) return;
        if (event.getWhoClicked() instanceof Player) {
            event.setCancelled(true);
            if (event.getInventory().getTitle().equals(Style.DARK_GRAY + "Server Selector") && event.getInventory().getTitle().equals(Style.DARK_GRAY + "Select a hub to join")) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.ARROW) {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract2(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        event.getInventory().setResult(null);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }
}
