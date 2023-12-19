package org.civsquared.factories.structure;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import org.civsquared.core.command.BrigadierCommand;
import org.civsquared.core.command.BrigadierCommandContext;
import org.civsquared.factories.CivSquaredFactoriesPlugin;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

public class StructureCommand extends BrigadierCommand {
    private final CivSquaredFactoriesPlugin plugin;
    private final StructureManager manager;

    public StructureCommand(CivSquaredFactoriesPlugin plugin, StructureManager manager) {
        super("structure", "");

        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public CommandNode<BrigadierCommandContext> getNode() {
        return LiteralArgumentBuilder.<BrigadierCommandContext>literal("structure").then(
            LiteralArgumentBuilder.<BrigadierCommandContext>literal("give").then(
                RequiredArgumentBuilder.<BrigadierCommandContext, String>argument("username", string()).then(
                    RequiredArgumentBuilder.<BrigadierCommandContext, String>argument("id", string()).executes(c -> {
                        final var username = getString(c, "username");
                        final var id = getString(c, "id");

                        this.plugin.getLogger().info("%s - %s".formatted(username, id));

                        final var player = this.plugin.getServer().getPlayer(username);
                        if (player == null)
                            return 1;

                        this.manager.giveStructureItem(player, id);

                        return 1;
                    })
                )
            )
        ).build();
    }
}
