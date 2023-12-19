package org.civsquared.factories.util;

import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockFaceUtilTest {
    @Test
    void distanceBetweenBlockFaces() {
        assertEquals(-90, BlockFaceUtil.angleBetweenBlockFaces(BlockFace.NORTH, BlockFace.WEST));
        assertEquals(90, BlockFaceUtil.angleBetweenBlockFaces(BlockFace.NORTH, BlockFace.EAST));
        assertEquals(-180, BlockFaceUtil.angleBetweenBlockFaces(BlockFace.NORTH, BlockFace.SOUTH));
    }

    @Test
    void rotateBlockFace() {
        assertEquals(BlockFace.SOUTH, BlockFaceUtil.rotateBlockFace(BlockFace.NORTH, 180));
        assertEquals(BlockFace.SOUTH, BlockFaceUtil.rotateBlockFace(BlockFace.NORTH, -180));
        assertEquals(BlockFace.NORTH, BlockFaceUtil.rotateBlockFace(BlockFace.EAST, -90));
    }
}
