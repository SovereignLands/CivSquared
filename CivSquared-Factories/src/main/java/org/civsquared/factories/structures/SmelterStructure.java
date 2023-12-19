package org.civsquared.factories.structures;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.civsquared.factories.structure.Structure;
import org.civsquared.factories.structure.StructureConstructionType;
import org.civsquared.factories.structure.annotations.StructureMetadata;

@StructureMetadata(
    id = "smelter",
    model = "models/smelter.yml",
    constructionType = StructureConstructionType.AUTO
)
public class SmelterStructure extends Structure {
    public SmelterStructure(@NonNull Location location, @NonNull BlockFace face) {
        super(location, face);
    }
}
