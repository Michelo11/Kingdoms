package me.michelemanna.kingdoms;

import me.michelemanna.kingdoms.commands.KingdomCommand;
import me.michelemanna.kingdoms.listeners.PlayerListener;
import me.michelemanna.kingdoms.managers.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class KingdomsPlugin extends JavaPlugin {
    private static KingdomsPlugin instance;
    private DatabaseManager database;
    private TerritoryManager territoryManager;
    private KingdomManager kingdomManager;
    private WarManager warManager;
    private QuestsManager questsManager;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        getCommand("kingdom").setExecutor(new KingdomCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(),this);

        try {
            this.database = new DatabaseManager(this);
            this.territoryManager = new TerritoryManager();
            this.kingdomManager = new KingdomManager();
            this.warManager = new WarManager();
            this.questsManager = new QuestsManager();

            this.questsManager.loadQuests();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.database.getKingdoms();
        this.database.getTerritories();
    }

    @Override
    public void onDisable() {
        this.database.close();
    }

    public static KingdomsPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabase() {
        return database;
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages." + path, "&cMessage not found: " + path));
    }

    public TerritoryManager getTerritoryManager() {
        return territoryManager;
    }

    public KingdomManager getKingdomManager() {
        return kingdomManager;
    }

    public WarManager getWarManager() {
        return warManager;
    }

    public QuestsManager getQuestsManager() {
        return questsManager;
    }
}