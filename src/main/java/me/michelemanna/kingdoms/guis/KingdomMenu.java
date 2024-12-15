package me.michelemanna.kingdoms.guis;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.guis.items.KingdomExperienceItem;
import me.michelemanna.kingdoms.guis.items.KingdomFundItem;
import me.michelemanna.kingdoms.guis.items.KingdomInfoItem;
import me.michelemanna.kingdoms.guis.items.KingdomLevelItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class KingdomMenu {
    public void open(Player player) {
        KingdomsPlugin.getInstance().getDatabase().getKingdom(player.getUniqueId()).thenAccept(kingdom -> {
            if (kingdom == null) {
                player.sendMessage(KingdomsPlugin.getInstance().getMessage("guis.kingdom-not-found"));
                return;
            }

            Gui.Builder.Normal builder = Gui.normal()
                    .setStructure(
                            "# # # # # # # # #",
                            "# . . 1 2 3 . . #",
                            "# . . 4 5 6 . . #",
                            "# # # # # # # # #"
                    );

            builder.addIngredient('1', new KingdomInfoItem(kingdom));
            builder.addIngredient('2', new KingdomLevelItem(kingdom));
            builder.addIngredient('3', new KingdomFundItem(kingdom));
            builder.addIngredient('4', new KingdomExperienceItem(kingdom));

            Gui gui = builder.build();

            Window window = Window.single()
                    .setViewer(player)
                    .setTitle("§6Kingdom Menu")
                    .setGui(gui)
                    .build();

            Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), window::open);
        });
    }
}