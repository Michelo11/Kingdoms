package me.michelemanna.kingdoms.guis;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.guis.items.LevelItem;
import me.michelemanna.kingdoms.guis.items.NavigationItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class LevelMenu {
    public void open(Player player) {
        ConfigurationSection levelsSection = KingdomsPlugin.getInstance().getConfig().getConfigurationSection("kingdom.levels");

        if (levelsSection == null || levelsSection.getKeys(false).isEmpty()) {
            player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.no-levels"));
            return;
        }

        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.kingdom-not-found"));
                return;
            }

            int currentLevel = kingdom != null ? kingdom.getLevel() : 0;

            List<Item> levelItems = new ArrayList<>();

            for (String levelKey : levelsSection.getKeys(false)) {
                ConfigurationSection levelData = levelsSection.getConfigurationSection(levelKey);
                if (levelData == null) continue;

                int level = Integer.parseInt(levelKey);
                int experience = levelData.getInt("experience", 0);
                int maxMembers = levelData.getInt("max-members", 0);
                int maxTerritories = levelData.getInt("max-territories", 0);

                Item levelItem = new LevelItem(level, experience, maxMembers, maxTerritories, level <= currentLevel);
                levelItems.add(levelItem);
            }

            PagedGui<Item> gui = PagedGui.items()
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . . . . . . #",
                            "# . . . . . . . #",
                            "# # # < # > # # #"
                    )
                    .setContent(levelItems)
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
