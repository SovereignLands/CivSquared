package org.civsquared.factories.util;

import org.bukkit.block.BlockFace;

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
}
