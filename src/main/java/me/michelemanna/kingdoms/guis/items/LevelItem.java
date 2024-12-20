package me.michelemanna.kingdoms.guis.items;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Arrays;

public class LevelItem extends SimpleItem {
    public LevelItem(int level, int experience, int maxMembers, int maxTerritories, boolean unlocked) {
        super(new ItemBuilder(unlocked ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                .setDisplayName("§eLevel " + level)
                .addLegacyLoreLines(Arrays.asList(
                        "§7Experience: §a" + experience,
                        "§7Max Members: §a" + maxMembers,
                        "§7Max Territories: §a" + maxTerritories,
                        unlocked ? "§aUnlocked" : "§cLocked"
                )));
    }
}