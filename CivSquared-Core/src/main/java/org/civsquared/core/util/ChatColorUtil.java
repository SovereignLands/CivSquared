package org.civsquared.core.util;

import org.bukkit.ChatColor;

public class ChatColorUtil {
    /**
     * Translates a string using the "&" character as a color code into the proper character.
     *
     * @param string The string to translate.
     * @return The output string.
     */
    public static String colorify(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
