package me.michelemanna.kingdoms.guis;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.guis.items.KingdomItem;
import me.michelemanna.kingdoms.guis.items.MemberItem;
import me.michelemanna.kingdoms.guis.items.NavigationItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

public class MembersMenu {
    private final Kingdom kingdom;

    public MembersMenu(Kingdom kingdom) {
        this.kingdom = kingdom;
    }

    public void open(Player player) {
        KingdomsPlugin.getInstance().getDatabase().getKingdomMembers(kingdom.getName()).thenAccept(members -> {
            if (members.isEmpty()) {
                player.sendMessage("§cThis kingdom has no members.");
                return;
            }

            PagedGui<Item> gui = PagedGui.items()
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# # # < # > # # #"
                    )
                    .setContent(members.stream().map(MemberItem::new).map(item -> (Item) item).toList())
                    .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .addIngredient('<', new NavigationItem(false))
                    .addIngredient('>', new NavigationItem(true))
                    .build();

            Window window = Window.single()
                    .setViewer(player)
                    .setTitle("§6" + kingdom.getName() + " Members")
                    .setGui(gui)
                    .build();

            Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), window::open);
        });
    }
}