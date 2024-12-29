package me.michelemanna.kingdoms.data;

import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Quest implements Listener {
    private final String event;
    private final String description;
    private final String name;
    private final String condition;
    private final int experience;

    public Quest(String event, String description, String name, String condition, int experience) {
        this.event = event;
        this.description = description;
        this.name = name;
        this.condition = condition;
        this.experience = experience;
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

    public void onEvent(Event event) {}
}
