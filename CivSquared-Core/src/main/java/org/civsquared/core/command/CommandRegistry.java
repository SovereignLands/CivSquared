package org.civsquared.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.civsquared.core.CivSquaredPlugin;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class CommandRegistry {
    private final CivSquaredPlugin plugin;
    private final CommandDispatcher<BrigadierCommandContext> brigadierDispatcher = new CommandDispatcher<>();

    public void register(@NonNull BrigadierCommand brigadierCommand) {
        final var logger = plugin.getLogger();

        brigadierDispatcher.getRoot().addChild(brigadierCommand.getNode());

        withCommandMap(commandMap -> {
            var command = new Command(brigadierCommand.getName()) {
                @Override
                public boolean execute(@NonNull CommandSender sender, @NonNull String command, @NonNull String[] args) {
                    try {
                        var commandString = command;
                        if (args.length > 0)
                            commandString += String.join(" ", args);
                        brigadierDispatcher.execute(commandString, new BrigadierCommandContext(sender));
                    } catch (CommandSyntaxException e) {
                        logger.severe("Failed to execute command, '%s': %s".formatted(command, e.getMessage()));
                        throw new RuntimeException(e);
                    }

                    return true;
                }
            };

            commandMap.register(brigadierCommand.getName(), command);
        });
    }

    private void withCommandMap(Consumer<CommandMap> consumer) {
        try {
            final var server = Bukkit.getServer();

            final var field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            consumer.accept((CommandMap) field.get(server));
            field.setAccessible(false);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
