package me.michelemanna.kingdoms.guis;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.guis.items.KingdomItem;
import me.michelemanna.kingdoms.guis.items.NavigationItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

public class KingdomsMenu {
    public void open(Player player) {
        KingdomsPlugin.getInstance().getDatabase().getKingdoms().thenAccept(kingdoms -> {
            if (kingdoms.isEmpty()) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.no-kingdoms"));
                return;
            }

            PagedGui<Item> gui = PagedGui.items()
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# # # < # > # # #"
                    )
                    .setContent(kingdoms.stream().map(KingdomItem::new).map(item -> (Item) item).toList())
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