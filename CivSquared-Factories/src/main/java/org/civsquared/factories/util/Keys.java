package org.civsquared.factories.util;

import org.bukkit.NamespacedKey;
import org.civsquared.factories.CivSquaredFactoriesPlugin;

import javax.xml.stream.events.Namespace;

public class Keys {
    public static NamespacedKey STRUCTURE;

    public static void setup(CivSquaredFactoriesPlugin plugin) {
        STRUCTURE = new NamespacedKey(plugin, "structure");
    }
}
