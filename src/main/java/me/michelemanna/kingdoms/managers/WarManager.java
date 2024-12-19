package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.War;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarManager {
    private final List<War> wars = new ArrayList<>();

    public void startWar(Kingdom attacker, Kingdom defender) {
        War war = new War(attacker, defender);

        wars.add(war);
        war.start();
    }

    public void endWar(War war) {
        wars.remove(war);
    }

    public void handleKill(UUID player) {
        wars.stream()
                .filter(war -> war.getAttacker().getMembers().contains(player) || war.getDefender().getMembers().contains(player) ||
                        war.getAttacker().getLeaderId().equals(player) || war.getDefender().getLeaderId().equals(player))
                .forEach(war -> war.getDeaths().add(player));
    }
}