package me.michelemanna.kingdoms.data;

import java.util.UUID;

public class Kingdom {
    private final int id;
    private String name;
    private final UUID leaderId;
    private int level;
    private int funds;
    private int experience;

    public Kingdom(int id, String name, UUID leaderId, int level, int funds, int experience) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
        this.level = level;
        this.funds = funds;
        this.experience = experience;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getFunds() {
        return funds;
    }

    public int getExperience() {
        return experience;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}