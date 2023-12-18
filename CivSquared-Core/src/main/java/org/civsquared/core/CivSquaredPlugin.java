package org.civsquared.core;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.civsquared.core.command.CommandRegistry;

public class CivSquaredPlugin extends JavaPlugin {
    @Getter
    private final CommandRegistry commandRegistry;

    public CivSquaredPlugin() {
        this.commandRegistry = new CommandRegistry(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
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
