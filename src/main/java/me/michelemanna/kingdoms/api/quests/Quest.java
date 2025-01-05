package me.michelemanna.kingdoms.api.quests;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.api.events.QuestCompleteEvent;
import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import javax.script.ScriptEngineManager;
import java.util.function.Consumer;

public abstract class Quest {
    private final String id;
    private final String description;
    private final String name;
    private final int experience;
    private final int required;

    public Quest(String id, String description, String name, int experience, int required) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.experience = experience;
        this.required = required;
    }

    public abstract void register();


    public void completeQuest(Player player) {
        Kingdom kingdom = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(player);
        KingdomsPlugin.getInstance().getDatabase().completeQuest(id, kingdom.getName()).thenAccept(result -> {
           if (result) {
               player.sendTitle(KingdomsPlugin.getInstance().getMessage("data.quest.title-success"), ChatColor.translateAlternateColorCodes('&', KingdomsPlugin.getInstance().getMessage("data.quest.subtitle-success").replace("%name%", name)), 10, 30, 10);
               player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

               KingdomsPlugin.getInstance().getKingdomManager().addExperience(kingdom, experience);

               Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), () -> {
                   Bukkit.getPluginManager().callEvent(new QuestCompleteEvent(player, this));
               });
           }
        });
    }

    public void incrementQuest(Player player) {
        Kingdom kingdom = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(player);

        KingdomsPlugin.getInstance().getDatabase().getQuestCount(id, kingdom.getName()).thenAccept(count -> {
            count++;

            if (count < required) {
                KingdomsPlugin.getInstance().getDatabase().incrementQuest(id, kingdom.getName());
            }

            if (count == required) {
                completeQuest(player);
            }
        });
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getExperience() {
        return experience;
    }

    public String getId() {
        return id;
    }
}