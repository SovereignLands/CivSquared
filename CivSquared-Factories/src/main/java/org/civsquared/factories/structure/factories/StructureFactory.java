package org.civsquared.factories.structure.factories;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.civsquared.factories.structure.Structure;

@FunctionalInterface
public interface StructureFactory {
    Structure create(Location location, BlockFace face);
}
