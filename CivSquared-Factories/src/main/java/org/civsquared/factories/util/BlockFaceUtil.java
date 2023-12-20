package org.civsquared.factories.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.List;

public class BlockFaceUtil {
    public static int blockFaceToAngle(final BlockFace face) {
        return switch (face) {
            case EAST -> -90;
            case NORTH -> 180;
            case WEST -> 90;
            default -> 0;
        };
    }

    public static BlockFace angleToBlockFace(final int angle) {
        return switch (angle) {
            case -90 -> BlockFace.EAST;
            case 180, -180 -> BlockFace.NORTH;
            case 90 -> BlockFace.WEST;
            case 0 -> BlockFace.SOUTH;
            default -> null;
        };
    }

    public static int angleBetweenBlockFaces(final BlockFace from, final BlockFace to) {
        var A = blockFaceToAngle(to) - blockFaceToAngle(from);

        if (A > 180 || A < -180) {
            final var D = A > 180 ? A - 180 : A + 180;
            A = -(A - D) + D;
        }

        return A;
    }

    public static BlockFace rotateBlockFace(final BlockFace face, final int angle) {
        var A = blockFaceToAngle(face) + angle;

        if (A > 180 || A < -180) {
            final var D = A > 180 ? A - 180 : A + 180;
            A = -(A - D) + D;
        }

        return angleToBlockFace(A);
    }

    public static BlockFace getClosestFace(float direction) {

        direction = direction % 360;

        if (direction < 0)
            direction += 360;

        direction = Math.round(direction / 45);

        return switch ((int) direction) {
            case 1 -> BlockFace.NORTH_WEST;
            case 2 -> BlockFace.NORTH;
            case 3 -> BlockFace.NORTH_EAST;
            case 4 -> BlockFace.EAST;
            case 5 -> BlockFace.SOUTH_EAST;
            case 6 -> BlockFace.SOUTH;
            case 7 -> BlockFace.SOUTH_WEST;
            default -> BlockFace.WEST;
        };
    }

    /**
     * Gets the face of the block the player is currently targeting.
     *
     * @param player The player who's targeted blocks BlockFace is to be checked.
     * @return The face of the targeted block, or null if the targeted block is non-occluding.
     */
    public static BlockFace getTargetBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        if (lastTwoTargetBlocks.size() != 2) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }
}
