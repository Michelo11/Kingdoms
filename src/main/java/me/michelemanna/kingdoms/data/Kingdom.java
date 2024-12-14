package me.michelemanna.kingdoms.data;

import java.util.UUID;

public class Kingdom {
    private final String name;
    private final UUID leaderId;
    private final long createdAt;

    public Kingdom(String name, UUID leaderId, long createdAt) {
        this.name = name;
        this.leaderId = leaderId;
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
}