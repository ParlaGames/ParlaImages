package xyz.oogiya.parlaimages.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.ImageStick;
import xyz.oogiya.parlaimages.images.Images;
import xyz.oogiya.parlaimages.util.HiddenStringUtils;
import xyz.oogiya.parlaimages.util.ImageUtils;
import xyz.oogiya.parlaimages.util.Utils;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private ImageUtils.ImageError isSpaceForImage(int x, int y, Block block, BlockFace face,
                                                  BlockFace widthDirection, BlockFace heightDirection) {
        Block b = block.getRelative(face);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Block frame = b.getRelative(widthDirection, i).getRelative(heightDirection, j);
                if (!block.getRelative(widthDirection, i).getRelative(heightDirection, j).getType().isSolid())
                    return ImageUtils.ImageError.NO_WALL;
                if (frame.getType().isSolid())
                    return ImageUtils.ImageError.NO_SPACE;
                if (!b.getWorld().getNearbyEntities(frame.getLocation().add(0.5, 0.5, 0.5), 0.5,
                        0.5, 0.5, a -> (a instanceof Hanging)).isEmpty())
                    return ImageUtils.ImageError.OVERLAP;
            }
        }
        return ImageUtils.ImageError.SUCCESS;
    }

    private void placeImage(Block block, BlockFace blockFace, float playerYaw, Image image) {
        Block relative = block.getRelative(blockFace);
        BlockFace widthDirection = Utils.calculateWidthDirection(playerYaw, blockFace);
        BlockFace heightDirection = Utils.calculateHeightDirection(playerYaw, blockFace);
        if (image != null) {
            int xFrames = (int)(Math.ceil(image.getWidth() / ImageUtils.MAP_WIDTH)) + 1;
            int yFrames = (int)(Math.ceil(image.getHeight() / ImageUtils.MAP_HEIGHT)) + 1;
            BlockFace lastFace = blockFace;

            if (!isSpaceForImage(xFrames, yFrames, block, blockFace, widthDirection, heightDirection)
                    .equals(ImageUtils.ImageError.SUCCESS)) {
                Bukkit.getLogger().info(isSpaceForImage(xFrames, yFrames, block, blockFace, widthDirection, heightDirection).toString());
                return;
            }

            for (int i = 0; i < xFrames; i++) {
                for (int j = 0; j < yFrames; j++) {
                    ItemFrame itemFrame = block.getWorld().spawn(relative.getRelative(widthDirection, i)
                            .getRelative(heightDirection, j).getLocation(), ItemFrame.class);
                    itemFrame.setFacingDirection(blockFace);
                    itemFrame.setItem(Images.getMapItem(i, j, image));
                    itemFrame.setRotation(Utils.facingToRotation(heightDirection, widthDirection));
                }
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !(e.getHand() == EquipmentSlot.HAND)) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.STICK)) {
                placeImage(e.getClickedBlock(), e.getBlockFace(), e.getPlayer().getLocation().getYaw(),
                        ImageUtils.itemStackToImage(e.getPlayer().getItemInHand()));
            }
        }
    }
}
