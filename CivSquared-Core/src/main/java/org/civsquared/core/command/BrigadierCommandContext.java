package org.civsquared.core.command;

import lombok.Data;
import org.bukkit.command.CommandSender;

public record BrigadierCommandContext(CommandSender sender) {
}
