package me.michelemanna.kingdoms.api;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.Quest;
import me.michelemanna.kingdoms.data.Territory;
import me.michelemanna.kingdoms.data.War;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KingdomsAPI {
    public static Kingdom getKingdom(String kingdomName) {
        return KingdomsPlugin.getInstance().getKingdomManager().getKingdom(kingdomName);
    }

    public static Kingdom getKingdom(Player player) {
        return KingdomsPlugin.getInstance().getKingdomManager().getKingdom(player);
    }

    public static Kingdom getKingdom(Chunk chunk) {
        Integer id = KingdomsPlugin.getInstance().getTerritoryManager().getKingdom(chunk);

        return id == null ? null : KingdomsPlugin.getInstance().getKingdomManager().getKingdom(id);
    }

    public static List<Kingdom> getKingdoms() {
        return KingdomsPlugin.getInstance().getKingdomManager().getKingdoms();
    }

    public static Map<Integer, List<Territory>> getTerritories() {
        return KingdomsPlugin.getInstance().getTerritoryManager().getTerritories();
    }

    public static List<Quest> getQuests() {
        return KingdomsPlugin.getInstance().getQuestsManager().getQuests();
    }

    public static List<War> getWars() {
        return KingdomsPlugin.getInstance().getWarManager().getWars();
    }

    public static War getWar(UUID player) {
        return KingdomsPlugin.getInstance().getWarManager().getWar(player);
    }

    public static War getWar(Kingdom kingdom) {
        return KingdomsPlugin.getInstance().getWarManager().getWar(kingdom);
    }
}