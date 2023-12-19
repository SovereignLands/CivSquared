package org.civsquared.core;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.civsquared.core.command.CommandRegistry;
import org.civsquared.core.database.DatabaseConfig;
import org.civsquared.core.database.DatabaseManager;

import java.io.File;

public class CivSquaredPlugin extends JavaPlugin {
    @Getter
    private final CommandRegistry commandRegistry;
    @Getter
    private final DatabaseManager databaseManager;

    @Getter
    private YamlConfiguration commonConfig;

    public CivSquaredPlugin() {
        this.commandRegistry = new CommandRegistry(this);
        this.databaseManager = new DatabaseManager(this);
        this.commonConfig = new YamlConfiguration();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        final var logger = getLogger();

        final var commonConfigDirectory = new File("plugins/CivSquared");
        if (!commonConfigDirectory.exists())
            if (!commonConfigDirectory.mkdirs())
                logger.warning("Failed to create common configuration directory.");

        try {
            this.commonConfig.load(new File(commonConfigDirectory, "config.yml"));
        } catch (Exception e) {
            logger.severe("Failed to load common configuration file.");
            return;
        }

        try {
            this.databaseManager.connect(DatabaseConfig.fromConfig(this.commonConfig.getConfigurationSection("database")));
            logger.info("Connected to the database.");
        } catch (Exception e) {
            logger.severe("Failed to setup a connection to the database,\n%s".formatted(e.getMessage()));
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
