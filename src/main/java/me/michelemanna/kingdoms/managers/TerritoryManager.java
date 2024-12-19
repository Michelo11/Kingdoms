package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.data.Territory;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerritoryManager {
    private final Map<Integer, List<Territory>> territories = new HashMap<>();

    public List<Territory> getChunks(Integer id) {
        return territories.get(id);
    }

    public Integer getKingdom(Chunk chunk) {
        for (Map.Entry<Integer, List<Territory>> entry : territories.entrySet()) {
            for (Territory territory : entry.getValue()) {
                if (territory.x() == chunk.getX() && territory.z() == chunk.getZ()) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    public void addTerritory(Integer id, Territory territory) {
        List<Territory> items = territories.computeIfAbsent(id, k -> new ArrayList<>());
        if (!items.contains(territory))
            items.add(territory);
    }

    public void removeTerritories(Integer id) {
        territories.remove(id);
    }

    public void removeTerritory(Integer id, Territory territory) {
        List<Territory> items = territories.get(id);
        if (items != null)
            items.remove(territory);
    }

    public Map<Integer, List<Territory>> getTerritories() {
        return territories;
    }
}