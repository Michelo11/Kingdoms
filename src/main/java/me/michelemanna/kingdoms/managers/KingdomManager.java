package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.Territory;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KingdomManager {
    private final List<Kingdom> kingdoms = new ArrayList<>();

    public Kingdom getKingdom(String name) {
        for (Kingdom kingdom : kingdoms) {
            if (kingdom.getName().equalsIgnoreCase(name)) {
                return kingdom;
            }
        }

        return null;
    }

    public Kingdom getKingdom(int id) {
        for (Kingdom kingdom : kingdoms) {
            if (kingdom.getId() == id) {
                return kingdom;
            }
        }

        return null;
    }

    public void addKingdom(Kingdom kingdom) {
        kingdoms.add(kingdom);
        KingdomsPlugin.getInstance().getDatabase().getKingdomMembers(kingdom.getName()).thenAccept(kingdom::setMembers);
    }

    public void removeKingdom(Kingdom kingdom) {
        kingdoms.remove(kingdom);
    }

    public List<Kingdom> getKingdoms() {
        return kingdoms;
    }

    public List<Kingdom> getNearbyKingdoms(Kingdom kingdom) {
        List<Kingdom> nearbyKingdoms = new ArrayList<>();
        List<Territory> territories = KingdomsPlugin.getInstance().getTerritoryManager().getChunks(kingdom.getId());

        for (Kingdom k : kingdoms) {
            if (k.equals(kingdom)) {
                continue;
            }

            List<Territory> kTerritories = KingdomsPlugin.getInstance().getTerritoryManager().getChunks(k.getId());

            for (Territory territory : territories) {
                for (Territory kTerritory : kTerritories) {
                    if (territory.isNear(kTerritory)) {
                        nearbyKingdoms.add(k);
                        break;
                    }
                }
            }
        }

        return nearbyKingdoms;
    }

    public void addExperience(Kingdom kingdom, int amount) {
        kingdom.addExperience(amount);

        Player player = Bukkit.getPlayer(kingdom.getLeaderId());

        int requiredExperience = KingdomsPlugin.getInstance().getConfig()
                .getInt("kingdom.levels." + kingdom.getLevel() + ".experience", -1);

        if (requiredExperience == -1) return;

        if (kingdom.levelUp(requiredExperience)) {
            KingdomsPlugin.getInstance().getDatabase().updateKingdom(kingdom);

            if (player != null) {
                player.sendTitle(KingdomsPlugin.getInstance().getMessage("managers.level.level-up.title"), KingdomsPlugin.getInstance().getMessage("managers.level.level-up.subtitle").replace("%level%", String.valueOf(kingdom.getLevel())), 10, 30, 10);
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }
}