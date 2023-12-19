package org.civsquared.core.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class DatabaseConfig {
    @NonNull
    private final String host;
    private final int port;

    @NonNull
    private final String username;

    private final String password;

    @NonNull
    private final String database;

    public static DatabaseConfig fromConfig(@NonNull ConfigurationSection databaseConfig) {
        return new DatabaseConfig(
                Objects.requireNonNull(databaseConfig.getString("host")),
                databaseConfig.getInt("port"),
                Objects.requireNonNull(databaseConfig.getString("username")),
                databaseConfig.getString("password"),
                Objects.requireNonNull(databaseConfig.getString("database"))
        );
    }
}
