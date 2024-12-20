package me.michelemanna.kingdoms.data;

import me.michelemanna.kingdoms.KingdomsPlugin;

import java.util.*;

public class Kingdom {
    private final int id;
    private String name;
    private final UUID leaderId;
    private int level;
    private int experience;
    private int funds;
    private List<UUID> members = new ArrayList<>();

    public Kingdom(int id, String name, UUID leaderId, int level, int funds, int experience) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
        this.level = level;
        this.funds = funds;
        this.experience = experience;
        KingdomsPlugin.getInstance().getDatabase().getKingdomMembers(name).thenAccept(this::setMembers);
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

    public void setFunds(int funds) {
        this.funds = funds;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void addExperience(int amount) {
        this.experience += amount;
    }

    public boolean levelUp(int requiredExperience) {
        if (this.experience >= requiredExperience) {
            this.level++;
            this.experience -= requiredExperience;
            return true;
        }
        return false;
    }

    public void setMembers(List<UUID> uuids) {
        this.members = uuids;
    }

    public int getMaxMembers() {
        return KingdomsPlugin.getInstance().getConfig().getInt("kingdom.levels." + level + ".max-members", 0);
    }

    public int getMaxTerritories() {
        return KingdomsPlugin.getInstance().getConfig().getInt("kingdom.levels." + level + ".max-territories", 0);
    }

    public boolean canAddMember() {
        return members.size() < getMaxMembers();
    }

    public boolean canAddTerritory() {
        int currentTerritories = KingdomsPlugin.getInstance().getTerritoryManager().getChunks(id).size();
        return currentTerritories >= getMaxTerritories();
    }
}