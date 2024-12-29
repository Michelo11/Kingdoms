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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public CompletableFuture<List<Kingdom>> getKingdoms() {
        CompletableFuture<List<Kingdom>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM kingdoms");

                ResultSet set = statement.executeQuery();
                List<Kingdom> kingdoms = new ArrayList<>();

                while (set.next()) {
                    Kingdom kingdom = new Kingdom(set.getInt("id"), set.getString("name"), UUID.fromString(set.getString("leader_id")),
                            set.getInt("level"), set.getInt("funds"), set.getInt("experience"), set.getDate("last_funds_update"));
                    kingdoms.add(kingdom);
                    KingdomsPlugin.getInstance().getKingdomManager().addKingdom(kingdom);
                }

                future.complete(kingdoms);

                set.close();
                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
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
                    Kingdom kingdom = new Kingdom(set.getInt("id"), set.getString("name"),
                            leader_id, set.getInt("level"), set.getInt("funds"),
                            set.getInt("experience"), set.getDate("last_funds_update"));
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

    public CompletableFuture<Kingdom> getKingdom(String name) {
        CompletableFuture<Kingdom> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM kingdoms WHERE name = ?");

                statement.setString(1, name);

                ResultSet set = statement.executeQuery();

                if (set.next()) {
                    Kingdom kingdom = new Kingdom(set.getInt("id"), set.getString("name"),
                            UUID.fromString(set.getString("leader_id")), set.getInt("level"),
                            set.getInt("funds"), set.getInt("experience"), set.getDate("last_funds_update"));
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
                PreparedStatement statement = connection.prepareStatement("INSERT INTO kingdoms (name, leader_id) VALUES (?, ?)");

                statement.setString(1, kingdomName);
                statement.setString(2, leaderId.toString());

                int rows = statement.executeUpdate();

                future.complete(rows > 0);

                ResultSet set = statement.getGeneratedKeys();
                set.next();
                int id = set.getInt(1);

                KingdomsPlugin.getInstance().getKingdomManager().addKingdom(new Kingdom(id, kingdomName, leaderId, 1, 0, 0, null));

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    public CompletableFuture<Boolean> deleteKingdom(String kingdomName) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM kingdoms WHERE name = ?");

                statement.setString(1, kingdomName);

                int rows = statement.executeUpdate();

                future.complete(rows > 0);

                Kingdom kingdom = KingdomsPlugin.getInstance().getKingdomManager().getKingdom(kingdomName);

                if (kingdom != null) {
                    KingdomsPlugin.getInstance().getKingdomManager().removeKingdom(kingdom);
                    KingdomsPlugin.getInstance().getTerritoryManager().removeTerritories(kingdom.getId());
                }

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    public void updateKingdom(Kingdom kingdom) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE kingdoms SET level = ?, funds = ?, experience = ?, name = ?, last_funds_update = ? WHERE id = ?");

                statement.setInt(1, kingdom.getLevel());
                statement.setInt(2, kingdom.getFunds());
                statement.setInt(3, kingdom.getExperience());
                statement.setString(4, kingdom.getName());
                statement.setDate(5, kingdom.getLastFundsUpdate());
                statement.setInt(6, kingdom.getId());

                int rows = statement.executeUpdate();

                future.complete(rows > 0);

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

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
                    Territory territory = new Territory(x, z);
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

    public CompletableFuture<List<UUID>> getKingdomMembers(String kingdomName) {
        CompletableFuture<List<UUID>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM members WHERE kingdom_name = ?");

                statement.setString(1, kingdomName);

                ResultSet set = statement.executeQuery();
                List<UUID> members = new ArrayList<>();

                while (set.next()) {
                    members.add(UUID.fromString(set.getString("uuid")));
                }

                future.complete(members);

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

                Kingdom kingdom = getKingdom(kingdomName).join();

                KingdomsPlugin.getInstance().getTerritoryManager().addTerritory(kingdom.getId(), new Territory(x, z));

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteTerritory(String kingdomName, int x, int z) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM territories WHERE kingdom_name = ? AND chunk_x = ? AND chunk_z = ?");

                statement.setString(1, kingdomName);
                statement.setInt(2, x);
                statement.setInt(3, z);

                statement.executeUpdate();

                Kingdom kingdom = getKingdom(kingdomName).join();

                KingdomsPlugin.getInstance().getTerritoryManager().removeTerritory(kingdom.getId(), new Territory(x, z));

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteTerritories(String kingdomName) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM territories WHERE kingdom_name = ?");

                statement.setString(1, kingdomName);

                statement.executeUpdate();

                Kingdom kingdom = getKingdom(kingdomName).join();

                KingdomsPlugin.getInstance().getTerritoryManager().removeTerritories(kingdom.getId());

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Map<Integer, List<Territory>>> getTerritories() {
        CompletableFuture<Map<Integer, List<Territory>>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT *, kingdoms.id AS kingdom_id FROM territories INNER JOIN kingdoms ON territories.kingdom_name = kingdoms.name");

                ResultSet set = statement.executeQuery();

                while (set.next()) {
                    int kingdomId = set.getInt("kingdom_id");
                    Territory territory = new Territory(set.getInt("chunk_x"), set.getInt("chunk_z"));
                    KingdomsPlugin.getInstance().getTerritoryManager().addTerritory(kingdomId, territory);
                }

                future.complete(KingdomsPlugin.getInstance().getTerritoryManager().getTerritories());


                set.close();
                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return future;
    }

    public void removeMembers(String kingdomName) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM members WHERE kingdom_name = ?");

                statement.setString(1, kingdomName);

                statement.executeUpdate();

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeMember(UUID playerId) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM members WHERE uuid = ?");

                statement.setString(1, playerId.toString());

                statement.executeUpdate();

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void addMember(String kingdomName, UUID playerId) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO members (kingdom_name, uuid) VALUES (?, ?)");

                statement.setString(1, kingdomName);
                statement.setString(2, playerId.toString());

                statement.executeUpdate();

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void transferMembers(String oldKingdomName, String newKingdomName) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE members SET kingdom_name = ? WHERE kingdom_name = ?");

                statement.setString(1, newKingdomName);
                statement.setString(2, oldKingdomName);

                statement.executeUpdate();

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void transferChunks(String oldKingdomName, String newKingdomName) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE territories SET kingdom_name = ? WHERE kingdom_name = ?");

                statement.setString(1, newKingdomName);
                statement.setString(2, oldKingdomName);

                statement.executeUpdate();

                Kingdom oldKingdom = getKingdom(oldKingdomName).join();
                Kingdom newKingdom = getKingdom(newKingdomName).join();

                KingdomsPlugin.getInstance().getTerritoryManager().removeTerritories(oldKingdom.getId());
                KingdomsPlugin.getInstance().getTerritoryManager().getTerritories().forEach((id, territories) -> {
                    if (id == newKingdom.getId()) {
                        territories.forEach(territory -> {
                            KingdomsPlugin.getInstance().getTerritoryManager().addTerritory(oldKingdom.getId(), territory);
                        });
                    }
                });

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void transferMembers(String oldKingdomName, String newKingdomName, List<UUID> members) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE members SET kingdom_name = ? WHERE kingdom_name = ? AND uuid = ?");

                statement.setString(1, newKingdomName);
                statement.setString(2, oldKingdomName);

                for (UUID member : members) {
                    statement.setString(3, member.toString());
                    statement.executeUpdate();
                }

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void transferChunks(String oldKingdomName, String newKingdomName, List<Territory> chunks) {
        Bukkit.getScheduler().runTaskAsynchronously(KingdomsPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE territories SET kingdom_name = ? WHERE kingdom_name = ? AND chunk_x = ? AND chunk_z = ?");

                statement.setString(1, newKingdomName);
                statement.setString(2, oldKingdomName);

                for (Territory chunk : chunks) {
                    statement.setInt(3, chunk.x());
                    statement.setInt(4, chunk.z());
                    statement.executeUpdate();
                }

                statement.close();
                provider.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}