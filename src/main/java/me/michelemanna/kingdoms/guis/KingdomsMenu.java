package me.michelemanna.kingdoms.guis;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.guis.items.KingdomItem;
import me.michelemanna.kingdoms.guis.items.NavigationItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class KingdomsMenu {
    public void open(Player player) {
        KingdomsPlugin.getInstance().getDatabase().getKingdoms().thenAcceptAsync(kingdoms -> {
            if (kingdoms.isEmpty()) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.no-kingdoms"));
                return;
            }


            Kingdom kingdom = KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).join();

            List<Kingdom> near = KingdomsPlugin.getInstance().getKingdomManager().getNearbyKingdoms(kingdom);

            PagedGui<Item> gui = PagedGui.items()
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# # # < # > # # #"
                    )
                    .setContent(kingdoms.stream().sorted((k1, k2) -> {
                        if (near.contains(k1) && !near.contains(k2)) return -1;
                        if (near.contains(k2) && !near.contains(k1)) return 1;
                        return 0;
                    })
                    .map(KingdomItem::new).map(item -> (Item) item).toList())
                    .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .addIngredient('<', new NavigationItem(false))
                    .addIngredient('>', new NavigationItem(true))
                    .build();

            Window window = Window.single()
                    .setViewer(player)
                    .setTitle("§6Kingdom List")
                    .setGui(gui)
                    .build();

            Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), window::open);
        });
    }
}