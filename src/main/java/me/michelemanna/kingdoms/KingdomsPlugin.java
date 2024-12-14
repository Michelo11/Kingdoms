package me.michelemanna.kingdoms;

import me.michelemanna.kingdoms.commands.KingdomCommand;
import me.michelemanna.kingdoms.managers.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class KingdomsPlugin extends JavaPlugin {
    private static KingdomsPlugin instance;
    private DatabaseManager database;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        getCommand("kingdom").setExecutor(new KingdomCommand());

        try {
            this.database = new DatabaseManager(this);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
}