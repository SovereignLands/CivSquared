package org.civsquared.core.command;

import com.mojang.brigadier.tree.CommandNode;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

@Getter
public class BrigadierCommand {
    private final String name, description;
    private CommandNode<BrigadierCommandContext> node = null;

    public BrigadierCommand(@NonNull String name, @NonNull String description) {
        this.name = name;
        this.description = description;
    }

    public void setNode(CommandNode<BrigadierCommandContext> node) {
        this.node = node;
    }
}
