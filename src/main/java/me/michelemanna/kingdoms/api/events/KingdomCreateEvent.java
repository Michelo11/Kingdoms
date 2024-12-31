package me.michelemanna.kingdoms.api.events;

import me.michelemanna.kingdoms.data.Kingdom;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class KingdomCreateEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Kingdom kingdom;

    public KingdomCreateEvent(Player player, Kingdom kingdom) {
        super(player);
        this.kingdom = kingdom;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}