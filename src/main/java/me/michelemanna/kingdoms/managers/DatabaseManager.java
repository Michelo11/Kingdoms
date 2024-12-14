package me.michelemanna.kingdoms.managers;

import me.michelemanna.kingdoms.KingdomsPlugin;
import me.michelemanna.kingdoms.data.Kingdom;
import me.michelemanna.kingdoms.data.Territory;
import me.michelemanna.kingdoms.managers.providers.ConnectionProvider;
import me.michelemanna.kingdoms.managers.providers.MySQLProvider;
import me.michelemanna.kingdoms.managers.providers.SQLiteProvider;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final ConnectionProvider provider;

    public DatabaseManager(KingdomsPlugin plugin) throws SQLException, ClassNotFoundException {
        String type = plugin.getConfig().getString("database.type", "sqlite");

        if (type.equalsIgnoreCase("mysql")) {
            provider = new MySQLProvider();
        } else {
            provider = new SQLiteProvider();
        }

        provider.connect();
    }

    public void close() {
        try {
            provider.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Kingdom> getKingdom(UUID leader_id) {
        CompletableFuture<Kingdom> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM kingdoms WHERE leader_id = ?");

                statement.setString(1, leader_id.toString());

                ResultSet set = statement.executeQuery();

                if (set.next()) {
                    Kingdom kingdom = new Kingdom(set.getString("name"), leader_id, set.getLong("created_at"));
                    future.complete(kingdom);
                } else {
                    future.complete(null);
                }

                set.close();
                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    public CompletableFuture<Boolean> createKingdom(UUID leaderId, String kingdomName) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO kingdoms (name, leader_id, created_at) VALUES (?, ?, ?)");

                statement.setString(1, kingdomName);
                statement.setString(2, leaderId.toString());
                statement.setLong(3, System.currentTimeMillis());

                int rows = statement.executeUpdate();

                future.complete(rows > 0);

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    public CompletableFuture<Territory> getTerritory(int x, int z) {
        CompletableFuture<Territory> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM territories WHERE chunk_x = ? AND chunk_z = ?");

                statement.setInt(1, x);
                statement.setInt(2, z);

                ResultSet set = statement.executeQuery();

                if (set.next()) {
                    Territory territory = new Territory(x, z, set.getBoolean("protected"), set.getLong("captured_at"));
                    future.complete(territory);
                } else {
                    future.complete(null);
                }

                set.close();
                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    public void createTerritory(String kingdomName, int x, int z) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO territories (kingdom_name, chunk_x, chunk_z) VALUES (?, ?, ?)");

                statement.setString(1, kingdomName);
                statement.setInt(2, x);
                statement.setInt(3, z);

                statement.executeUpdate();

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}