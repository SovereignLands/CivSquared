package org.civsquared.factories.structure.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

/**
 * Represents a potential block within a `Structure`.
 */
@Getter
@Setter
public class StructureModelBlock {
    @NonNull
    private Vector offset;

    @NonNull
    private BlockData data;

    public StructureModelBlock(
        @NonNull Vector offset,
        @NonNull BlockData data
    ) {
        this.offset = offset;
        this.data = data;
    }
}
