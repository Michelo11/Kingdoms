package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
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
}