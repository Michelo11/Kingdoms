package me.michelemanna.kingdoms.api.events;

import me.michelemanna.kingdoms.data.War;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final War war;

    public WarEndEvent(War war) {
        this.war = war;
    }

    public War getWar() {
        return war;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
