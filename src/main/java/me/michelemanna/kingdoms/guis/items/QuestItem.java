package me.michelemanna.kingdoms.guis.items;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.api.quests.Quest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

import java.util.Arrays;

public class QuestItem extends AsyncItem {
    public QuestItem(Quest quest, String kingdomName) {
        super(createItem(false, quest), () -> {
            boolean completed = KingdomsPlugin.getInstance().getDatabase().isCompletedQuest(quest.getId(), kingdomName).join();

            return createItem(completed, quest);
        });
    }

    private static ItemBuilder createItem(boolean completed, Quest quest) {
        return new ItemBuilder(completed ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', quest.getName()))
                .addLegacyLoreLines(Arrays.asList(
                        ChatColor.translateAlternateColorCodes('&', "&7Description: &a" + quest.getDescription()),
                        "§7Experience: §a" + quest.getExperience(),
                        completed ? "§aCompleted" : "§cNot Completed"
                ));
    }
}