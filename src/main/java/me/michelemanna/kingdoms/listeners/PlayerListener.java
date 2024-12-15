package me.michelemanna.kingdoms.listeners;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Integer id = KingdomsPlugin.getInstance().getTerritoryManager().getKingdom(event.getClickedBlock().getLocation().getChunk());

        if (id == null) return;

        Kingdom kingdom = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(id);

        if (kingdom == null) return;

        if (kingdom.getMembers().contains(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;

        Integer id = KingdomsPlugin.getInstance().getTerritoryManager().getKingdom(event.getTo().getChunk());

        if (id == null) return;

        Kingdom kingdom = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(id);

        if (kingdom == null) return;

        List<Chunk> chunks = KingdomsPlugin.getInstance().getTerritoryManager().getChunks(id).stream()
                .map(chunk -> event.getPlayer().getWorld().getChunkAt(chunk.x(), chunk.z()))
                .toList();

        if (chunks.contains(event.getFrom().getChunk())) return;

        String title = KingdomsPlugin.getInstance().getMessage("listeners.title").replace("%name%", kingdom.getName());
        OfflinePlayer leader = Bukkit.getOfflinePlayer(kingdom.getLeaderId());
        String subtitle = KingdomsPlugin.getInstance().getMessage("listeners.subtitle").replace("%name%", leader.getName());

        event.getPlayer().sendTitle(title, subtitle, 10, 30, 10);
    }
}
