package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.api.events.WarEndEvent;
import me.michelemanna.kingdoms.api.events.WarStartedEvent;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.War;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarManager {
    private final List<War> wars = new ArrayList<>();

    public War getWar(Kingdom kingdom) {
        return wars.stream()
                .filter(war -> war.getAttacker().equals(kingdom) || war.getDefender().equals(kingdom))
                .findFirst()
                .orElse(null);
    }

    public void startWar(Kingdom attacker, Kingdom defender) {
        War war = new War(attacker, defender);

        wars.add(war);
        war.start();

        Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(new WarStartedEvent(new War(attacker, defender)));
        });
    }

    public void endWar(War war) {
        wars.remove(war);

        Bukkit.getScheduler().runTask(KingdomsPlugin.getInstance(), () -> {
            Bukkit.getPluginManager().callEvent(new WarEndEvent(war));
        });
    }

    public void handleKill(UUID player) {
        wars.stream()
                .filter(war -> war.getAttacker().getMembers().contains(player) || war.getDefender().getMembers().contains(player) ||
                        war.getAttacker().getLeaderId().equals(player) || war.getDefender().getLeaderId().equals(player) ||
                        war.getAlliances().stream().anyMatch(kingdom -> kingdom.getMembers().contains(player) || kingdom.getLeaderId().equals(player)))
                .forEach(war -> war.getDeaths().add(player));
    }

    public War getWar(UUID player) {
        return wars.stream()
                .filter(war -> war.getAttacker().getMembers().contains(player) || war.getDefender().getMembers().contains(player) ||
                        war.getAttacker().getLeaderId().equals(player) || war.getDefender().getLeaderId().equals(player) ||
                        war.getAlliances().stream().anyMatch(kingdom -> kingdom.getMembers().contains(player) || kingdom.getLeaderId().equals(player)))
                .findFirst()
                .orElse(null);
    }

    public List<War> getWars() {
        return wars;
    }
}