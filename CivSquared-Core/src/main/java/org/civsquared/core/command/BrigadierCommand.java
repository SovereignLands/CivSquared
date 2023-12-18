package org.civsquared.core.command;

import com.mojang.brigadier.tree.CommandNode;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

@Getter
public abstract class BrigadierCommand {
    private final String name, description;

    BrigadierCommand(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }

    abstract CommandNode<BrigadierCommandContext> getNode();
}
