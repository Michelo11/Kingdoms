package me.michelemanna.kingdoms.managers.providers;

import me.michelemanna.kingdoms.KingdomsPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteProvider implements ConnectionProvider {
    private Connection connection;

    @Override
    public void connect() throws SQLException {
        File file = new File(KingdomsPlugin.getInstance().getDataFolder(), "database.db");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }

        this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        Statement statement = connection.createStatement();

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS kingdoms(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "leader_id TEXT NOT NULL, " +
                "level INTEGER DEFAULT 1, " +
                "funds INTEGER DEFAULT 0, " +
                "last_funds_update DATE, " +
                "experience INTEGER DEFAULT 0" +
                ")");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS members(" +
                "uuid TEXT NOT NULL, " +
                "kingdom_name TEXT NOT NULL, " +
                "FOREIGN KEY (kingdom_name) REFERENCES kingdoms(name) ON DELETE CASCADE" +
                ")");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS territories(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "kingdom_name TEXT NOT NULL, " +
                "chunk_x INTEGER NOT NULL, " +
                "chunk_z INTEGER NOT NULL, " +
                "FOREIGN KEY (kingdom_name) REFERENCES kingdoms(name) ON DELETE CASCADE" +
                ")");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS missions(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "kingdom_name TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "status TEXT DEFAULT 'pending', " +
                "reward INTEGER DEFAULT 0, " +
                "FOREIGN KEY (kingdom_name) REFERENCES kingdoms(name) ON DELETE CASCADE" +
                ")");

        statement.close();
    }

    @Override
    public void disconnect() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
    }
}
