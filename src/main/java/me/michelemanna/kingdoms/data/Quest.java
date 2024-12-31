package me.michelemanna.kingdoms.data;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.api.events.QuestCompleteEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.script.ScriptEngineManager;
import java.util.function.Consumer;

public class Quest implements Listener {
    private final String id;
    private final String event;
    private final String description;
    private final String name;
    private final String code;
    private final int experience;
    private final int required;

    public Quest(String id,  String event, String description, String name, String code, int experience, int required) {
        this.id = id;
        this.event = event;
        this.description = description;
        this.name = name;
        this.code = code;
        this.experience = experience;
        this.required = required;
    }

    @SuppressWarnings("unchecked")
    public void register() {
        try {
            Class<Event> clazz = (Class<Event>) Class.forName(this.event);
            Bukkit.getPluginManager().registerEvent(clazz, this, EventPriority.MONITOR, (listener, event) -> {
                if (clazz.isInstance(event)) {
                    onEvent(event);
                }
            }, KingdomsPlugin.getInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(Event event) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                ScriptEngineManager provider = Bukkit.getServicesManager().getRegistration(ScriptEngineManager.class).getProvider();
                provider.put("event", event);
                provider.put("completeQuest", (Consumer<Player>) this::completeQuest);
                provider.put("incrementQuest", (Consumer<Player>) this::incrementQuest);
                provider.getEngineByName("JavaScript").eval(this.code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


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