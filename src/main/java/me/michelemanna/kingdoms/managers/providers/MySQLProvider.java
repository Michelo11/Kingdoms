package me.michelemanna.kingdoms.managers.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.kingdoms.KingdomsPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MySQLProvider implements ConnectionProvider {
    private HikariDataSource dataSource;

    @Override
    public void connect() throws SQLException {
        ConfigurationSection cs = KingdomsPlugin.getInstance().getConfig().getConfigurationSection("database");
        Objects.requireNonNull(cs, "Unable to find the following key: database");
        HikariConfig config = new HikariConfig();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }

        config.setJdbcUrl("jdbc:mysql://" + cs.getString("host") + ":" + cs.getString("port") + "/" + cs.getString("database"));
        config.setUsername(cs.getString("username"));
        config.setPassword(cs.getString("password"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(10000);
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(60000);
        config.setPoolName("KingdomsPool");
        config.addDataSourceProperty("useSSL", cs.getBoolean("ssl"));
        config.addDataSourceProperty("allowPublicKeyRetrieval", true);

        this.dataSource = new HikariDataSource(config);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS kingdoms(" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL UNIQUE," +
                "leader_id VARCHAR(36) NOT NULL," +
                "level INT DEFAULT 1," +
                "funds INT DEFAULT 0," +
                "last_funds_update DATE," +
                "experience INT DEFAULT 0" +
                ")");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS members(" +
                "uuid VARCHAR(36) NOT NULL," +
                "kingdom_name VARCHAR(255) NOT NULL," +
                "FOREIGN KEY (kingdom_name) REFERENCES kingdoms(name) ON DELETE CASCADE" +
                ")");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS territories(" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "kingdom_name VARCHAR(255) NOT NULL," +
                "chunk_x INT NOT NULL," +
                "chunk_z INT NOT NULL," +
                "FOREIGN KEY (kingdom_name) REFERENCES kingdoms(name) ON DELETE CASCADE" +
                ")");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS missions(" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "kingdom_name VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "status VARCHAR(255) DEFAULT 'pending'," +
                "reward INT DEFAULT 0," +
                "FOREIGN KEY (kingdom_name) REFERENCES kingdoms(name) ON DELETE CASCADE" +
                ")");

        statement.close();
        connection.close();
    }

    @Override
    public void disconnect() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}