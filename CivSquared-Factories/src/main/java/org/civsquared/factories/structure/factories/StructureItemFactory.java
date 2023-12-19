package org.civsquared.factories.structure.factories;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface StructureItemFactory {
    ItemStack create();
}
