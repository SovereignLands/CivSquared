package org.civsquared.factories.structure;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.civsquared.factories.structure.annotations.StructureMetadata;
import org.civsquared.factories.structure.model.StructureModel;
import org.civsquared.factories.structure.model.StructureModelBlock;
import org.civsquared.factories.util.BlockFaceUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class Structure {
    @Getter
    private final Location location;

    @Getter
    private final BlockFace face;

    private StructureModel model;

    @Getter
    private List<Block> blocks = new ArrayList<>();

    @Getter
    private Block rootBlock;

    @Getter
    private boolean constructed = false;

    @Getter
    @Setter
    private List<StructureModelBlock> rotatedBlockDescriptors = new ArrayList<>();

    public Structure(@NonNull Location location, @NonNull BlockFace face) {
        this.location = location;
        this.face = face;
    }

    public static StructureMetadata getMetadataForClass(Class<? extends Structure> structureClass) {
        return structureClass.getDeclaredAnnotation(StructureMetadata.class);
    }

    void initialize(@NonNull StructureManager structureManager) {
        // Rotate the block descriptors origins/block-faces depending on the yaw specified in location.
        final var angleToRotate = BlockFaceUtil.angleBetweenBlockFaces(BlockFace.NORTH, face);

        this.model = structureManager.getModelRegistry().get(getMetadata().model());

        var rootBlockIndex = 0;

        for (final var blockDescriptor : this.model.getBlockDescriptors()) {
            final var newBlockDescriptor = new StructureModelBlock(
                blockDescriptor.getOffset().clone().rotateAroundY(Math.toRadians(-angleToRotate)),
                blockDescriptor.getData().clone()
            );

            if (blockDescriptor.getOffset().isZero())
                rootBlockIndex = this.model.getBlockDescriptors().indexOf(blockDescriptor);

            var data = newBlockDescriptor.getData();
            if (data instanceof Directional)
                ((Directional) data).setFacing(BlockFaceUtil.rotateBlockFace(((Directional) data).getFacing(), angleToRotate));

            this.rotatedBlockDescriptors.add(newBlockDescriptor);
        }

        // Determine if we need to "build" the entire structure, or just the root block.
        final var world = this.location.getWorld();

        if (world == null)
            return;

        switch (getMetadata().constructionType()) {
            case AUTO -> {
                int i = 0;
                for (final var blockDescriptor : this.rotatedBlockDescriptors) {
                    final var offset = blockDescriptor.getOffset();
                    final var block = world.getBlockAt(this.location.clone().add(offset));

                    if (rootBlockIndex == i++)
                        this.rootBlock = block;

                    block.setBlockData(blockDescriptor.getData());
                    this.blocks.add(block);

                    this.constructed = true;
                }
            }
            case MANUAL -> {
            }
        }
    }

    /**
     * Destroys all the blocks associated with the model.
     * Additionally, the state is set to un-constructed.
     */
    public void destroy() {
        for (final var block : this.blocks)
            block.setType(Material.AIR);

        this.constructed = false;
    }

    private StructureMetadata getMetadata() {
        return this.getClass().getDeclaredAnnotation(StructureMetadata.class);
    }
}