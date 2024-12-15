package me.michelemanna.kingdoms.guis.items;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.Particles;
import com.cryptomorin.xseries.particles.XParticle;
import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.List;

public class KingdomTerritoryItem extends AbstractItem {
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.MAP)
                .setDisplayName("§6Show territories");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            List<Chunk> chunks = KingdomsPlugin.getInstance().getTerritoryManager().getChunks(kingdom.getId()).stream()
                    .map(territory -> player.getWorld().getChunkAt(territory.x(), territory.z()))
                    .toList();

            for (Chunk chunk : chunks) {
                World world = chunk.getWorld();
                Location first = chunk.getBlock(0, world.getMinHeight(), 0).getLocation();
                Location last = chunk.getBlock(15, world.getMaxHeight(), 15).getLocation();
                Particles.cube(first, last, 1, ParticleDisplay.of(XParticle.HAPPY_VILLAGER).onlyVisibleTo(player));
            }
        });
    }
}