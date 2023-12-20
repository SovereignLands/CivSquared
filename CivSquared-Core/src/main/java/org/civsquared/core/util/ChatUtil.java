package org.civsquared.core.util;

import lombok.NonNull;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static void sendErrorMessage(@NonNull Player player, @NonNull String message) {
        player.sendMessage(
            ChatColorUtil.colorify("&c&lError &r&8| &r" + message)
        );
    }
}
