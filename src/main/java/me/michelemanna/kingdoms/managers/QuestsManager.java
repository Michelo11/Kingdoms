package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestsManager {
    private final List<Quest> quests = new ArrayList<>();

    public void loadQuests() {
        KingdomsPlugin.getInstance().getConfig().getConfigurationSection("kingdom.quests").getKeys(false).forEach(quest -> {
            String event = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".event");
            String description = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".description");
            String name = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".name");
            String condition = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".condition");
            int experience = KingdomsPlugin.getInstance().getConfig().getInt("kingdom.quests." + quest + ".experience");

            quests.add(new Quest(event, description, name, condition, experience));
        });

        quests.forEach(Quest::register);
    }
}