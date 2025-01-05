package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.api.quests.JSQuest;
import me.michelemanna.kingdoms.api.quests.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestsManager {
    private final List<Quest> quests = new ArrayList<>();

    public void loadQuests() {
        KingdomsPlugin.getInstance().getConfig().getConfigurationSection("kingdom.quests").getKeys(false).forEach(quest -> {
            String event = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".event");
            String description = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".description");
            String name = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".name");
            String code = KingdomsPlugin.getInstance().getConfig().getString("kingdom.quests." + quest + ".code");
            int experience = KingdomsPlugin.getInstance().getConfig().getInt("kingdom.quests." + quest + ".experience");
            int required = KingdomsPlugin.getInstance().getConfig().getInt("kingdom.quests." + quest + ".required");

            quests.add(new JSQuest(quest, event, description, name, code, experience, required));
        });

        quests.forEach(Quest::register);
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void registerQuest(Quest quest) {
        quests.add(quest);
        quest.register();
    }
}