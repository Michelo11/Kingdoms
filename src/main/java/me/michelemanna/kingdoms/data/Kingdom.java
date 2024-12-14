package me.michelemanna.kingdoms.data;

import java.util.UUID;

public class Kingdom {
    private final String name;
    private final UUID leaderId;
    private final int level;
    private final int funds;
    private final int experience;
    private final long createdAt;

    public Kingdom(String name, UUID leaderId, long createdAt, int level, int funds, int experience) {
        this.name = name;
        this.leaderId = leaderId;
        this.level = level;
        this.funds = funds;
        this.experience = experience;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public UUID getLeaderId() {
        return leaderId;
    }

    public long getCreatedAt() {
        return createdAt;
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
}