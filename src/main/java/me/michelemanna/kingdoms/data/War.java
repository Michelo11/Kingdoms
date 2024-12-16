package me.michelemanna.kingdoms.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class War {
    private final Kingdom attacker;
    private final Kingdom defender;
    private final List<UUID> deaths = new ArrayList<>();

    public War(Kingdom attacker, Kingdom defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public Kingdom getAttacker() {
        return attacker;
    }

    public List<UUID> getDeaths() {
        return deaths;
    }

    public Kingdom getDefender() {
        return defender;
    }
}