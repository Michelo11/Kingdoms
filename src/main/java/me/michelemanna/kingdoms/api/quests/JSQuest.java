package me.michelemanna.kingdoms.api.quests;

import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.script.ScriptEngineManager;
import java.util.function.Consumer;

public class JSQuest extends Quest implements Listener {
    private final String code;
    private final String event;

    public JSQuest(String id,  String event, String description, String name, String code, int experience, int required) {
        super(id, description, name, experience, required);
        this.code = code;
        this.event = event;
    }

    @Override
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
}
