package xyz.oogiya.parlaimages.util;

import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import xyz.oogiya.parlaimages.ParlaImages;

public class Utils {

    public static boolean STICKS_BY_PLAYER = ParlaImages.config.getBoolean("STICKS_BY_PLAYER");

    public static boolean isBetween(double x, double y1, double y2) {
        if (x >= y1 && x <= y2) return true;
        return false;
    }

    public static BlockFace calculateWidthDirection(float playerYaw, BlockFace face) {
        float yaw = (360.0f + playerYaw) % 360.0f;
        switch (face) {
            case NORTH:
                return BlockFace.WEST;
            case SOUTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.NORTH;
            case WEST:
                return BlockFace.SOUTH;
            case UP:
            case DOWN:
                if (isBetween(yaw, 45.0, 135.0))
                    return BlockFace.NORTH;
                else if (isBetween(yaw, 135.0, 225.0))
                    return BlockFace.EAST;
                else if (isBetween(yaw, 225.0, 315.0))
                    return BlockFace.SOUTH;
                else
                    return BlockFace.WEST;
            default:
                return null;
        }
    }

    public static BlockFace calculateHeightDirection(float playerYaw, BlockFace face) {
        float yaw = (360.0f + playerYaw) % 360.0f;
        switch (face) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                return BlockFace.DOWN;
            case UP:
                if (isBetween(yaw, 45.0, 135.0))
                    return BlockFace.EAST;
                else if (isBetween(yaw, 135.0, 225.0))
                    return BlockFace.SOUTH;
                else if (isBetween(yaw, 225.0, 315.0))
                    return BlockFace.WEST;
                else
                    return BlockFace.NORTH;
            case DOWN:
                if (isBetween(yaw, 45.0, 135.0))
                    return BlockFace.WEST;
                else if (isBetween(yaw, 135.0, 225.0))
                    return BlockFace.NORTH;
                else if (isBetween(yaw, 225.0, 315.0))
                    return BlockFace.EAST;
                else
                    return BlockFace.SOUTH;
            default:
                return null;
        }
    }

    public static boolean isAxisAligned(BlockFace face) {
        switch (face) {
            case DOWN:
            case UP:
            case WEST:
            case EAST:
            case SOUTH:
            case NORTH:
                return true;
            default:
                return false;
        }
    }

    public static Rotation facingToRotation(BlockFace heightDirection, BlockFace widthDirection) {
        switch (heightDirection) {
            case WEST:
                return Rotation.CLOCKWISE_45;
            case NORTH:
                return widthDirection == BlockFace.WEST ? Rotation.CLOCKWISE : Rotation.NONE;
            case EAST:
                return Rotation.CLOCKWISE_135;
            case SOUTH:
                return widthDirection == BlockFace.WEST ? Rotation.CLOCKWISE : Rotation.NONE;
            default:
                return Rotation.NONE;
        }
    }
}
