package me.michelemanna.kingdoms.api.events;

import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.Territory;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ClaimTerritoryEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Kingdom kingdom;
    private final Territory territory;

    public ClaimTerritoryEvent(Player player, Kingdom kingdom, Territory territory) {
        super(player);
        this.kingdom = kingdom;
        this.territory = territory;
    }

    public Kingdom getKingdom() {
        return kingdom;
    }

    public Territory getTerritory() {
        return territory;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
