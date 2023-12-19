package org.civsquared.core.database;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.civsquared.core.CivSquaredPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class DatabaseManager {
    private final CivSquaredPlugin plugin;
    private Connection connection = null;

    public void connect(@NonNull DatabaseConfig config) throws DatabaseException {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://%s:%d/%s".formatted(config.getHost(),
                    config.getPort(), config.getDatabase()), config.getUsername(), config.getPassword());
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database: \"%s\".".formatted(e.getMessage()));
        }
    }

    public PreparedStatement prepared(@NonNull String query) throws DatabaseException {
        try {
            //noinspection SqlSourceToSinkFlow
            return this.connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to prepare statement.");
        }
    }
}
