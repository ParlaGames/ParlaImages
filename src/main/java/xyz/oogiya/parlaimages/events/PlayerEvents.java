package xyz.oogiya.parlaimages.events;

import org.bukkit.Bukkit;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;
import xyz.oogiya.parlaimages.util.ImageUtils;

import java.awt.image.BufferedImage;

public class PlayerEvents implements Listener {

    private static boolean isBetween(double x, double y1, double y2) {
        if (x >= y1 && x <= y2) return true;
        return false;
    }

    private static BlockFace calculateWidthDirection(Player player, BlockFace face) {
        float yaw = (360.0f + player.getLocation().getYaw()) % 360.0f;
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

    private static BlockFace calculateHeightDirection(Player player, BlockFace face) {
        float yaw = (360.0f + player.getLocation().getYaw()) % 360.0f;
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

    private static boolean isAxisAligned(BlockFace face) {
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

    private static Rotation facingToRotation(BlockFace heightDirection, BlockFace widthDirection) {
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!Images.playerImages.containsKey(e.getPlayer().getUniqueId())) {
                return;
            }
            Block block = e.getClickedBlock();
            BlockFace blockFace = e.getBlockFace();
            Block relative = block.getRelative(blockFace);
            BlockFace widthDirection = calculateWidthDirection(e.getPlayer(), blockFace);
            BlockFace heightDirection = calculateHeightDirection(e.getPlayer(), blockFace);
            Image image = Images.playerImages.get(e.getPlayer().getUniqueId());

            int xFrames = (int)(Math.ceil(image.getWidth() / ImageUtils.MAP_WIDTH)) + 1;
            int yFrames = (int)(Math.ceil(image.getHeight() / ImageUtils.MAP_HEIGHT)) + 1;
            Bukkit.getLogger().info("xframes: " + xFrames + " yframes: " + yFrames);
            BlockFace lastFace = blockFace;
            for (int i = 0; i < xFrames; i++) {
                for (int j = 0; j < yFrames; j++) {
                    ItemFrame itemFrame = block.getWorld().spawn(relative.getRelative(widthDirection, i)
                                    .getRelative(heightDirection, j).getLocation(), ItemFrame.class);
                    itemFrame.setFacingDirection(blockFace);
                    itemFrame.setItem(Images.getMapItem(i, j, image));
                    itemFrame.setRotation(facingToRotation(heightDirection, widthDirection));
                }
            }
            Images.playerImages.remove(e.getPlayer().getUniqueId());
        }
    }
}
