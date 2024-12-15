package me.michelemanna.kingdoms.data;

import java.util.List;
import java.util.UUID;

public class Kingdom {
    private final int id;
    private String name;
    private final UUID leaderId;
    private int level;
    private int funds;
    private int experience;
    private List<UUID> members;

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

    public UUID getLeaderId() {
        return leaderId;
    }

    public int getExperience() {
        return experience;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> uuids) {
        this.members = uuids;
    }
}