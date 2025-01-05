package me.michelemanna.kingdoms.guis;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.api.quests.Quest;
import me.michelemanna.kingdoms.guis.items.NavigationItem;
import me.michelemanna.kingdoms.guis.items.QuestItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class QuestsMenu {
    public void open(Player player) {
        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.kingdom-not-found"));
                return;
            }

            List<Quest> quests = KingdomsPlugin.getInstance().getQuestsManager().getQuests();

            if (quests.isEmpty()) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.no-quests"));
                return;
            }

            List<Item> questItems = new ArrayList<>();

            for (Quest quest : quests) {
                Item questItem = new QuestItem(quest, kingdom.getName());
                questItems.add(questItem);
            }

            PagedGui<Item> gui = PagedGui.items()
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# # # < # > # # #"
                    )
                    .setContent(questItems)
                    .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .addIngredient('<', new NavigationItem(false))
                    .addIngredient('>', new NavigationItem(true))
                    .build();

            Window window = Window.single()
                    .setViewer(player)
                    .setTitle("§6Kingdom Levels")
                    .setGui(gui)
                    .build();

            Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), window::open);
        });
    }
}
