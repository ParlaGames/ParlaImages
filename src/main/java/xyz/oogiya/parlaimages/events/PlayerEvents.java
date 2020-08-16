package xyz.oogiya.parlaimages.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import xyz.oogiya.parlaimages.ParlaImages;
import xyz.oogiya.parlaimages.images.*;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.tasks.SaveImageTask;
import xyz.oogiya.parlaimages.util.ImageUtils;
import xyz.oogiya.parlaimages.util.Utils;

import java.awt.*;


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
        image.setWidthDirection(widthDirection.toString());
        image.setHeightDirection(heightDirection.toString());
        if (image != null) {
            int xFrames = (int)(Math.round(image.getWidth() / ImageUtils.MAP_WIDTH));
            int yFrames = (int)(Math.round(image.getHeight() / ImageUtils.MAP_HEIGHT));
            BlockFace lastFace = blockFace;

            if (!isSpaceForImage(xFrames, yFrames, block, blockFace, widthDirection, heightDirection)
                    .equals(ImageUtils.ImageError.SUCCESS)) {
                return;
            }

            for (int i = 0; i < xFrames; i++) {
                for (int j = 0; j < yFrames; j++) {
                    ItemFrame itemFrame = block.getWorld().spawn(relative.getRelative(widthDirection, i)
                            .getRelative(heightDirection, j).getLocation(), ItemFrame.class);
                    itemFrame.setFacingDirection(blockFace);
                    ItemStack item = Images.getMapItem(i, j, image);
                    MapMeta meta = (MapMeta)item.getItemMeta();
                    MapView mapView = meta.getMapView();
                    itemFrame.setItem(item);
                    itemFrame.setRotation(Utils.facingToRotation(heightDirection, widthDirection));
                    mapView.getRenderers().forEach(mapView::removeRenderer);
                    mapView.addRenderer(new ImageMapRenderer(image.getImage(), i, j));
                    image.addMapLocationToArray(itemFrame.getLocation(), new Point(i, j), meta.getMapId());
                }
            }
            Images.saveMap(image);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !(e.getHand() == EquipmentSlot.HAND)) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.STICK)) {
                Image image = ImageUtils.itemStackToImage(e.getPlayer().getItemInHand());
                if (image != null) {
                    if (Utils.STICKS_BY_PLAYER && !image.getUUID().equals(e.getPlayer().getUniqueId())) return;
                    if (ParlaImages.backgroundExecutor.isFull()) {
                        e.getPlayer().sendMessage("There are too many requests right now, please wait.");
                        e.getPlayer().setItemInHand(Images.getImageStick(image));
                        e.setCancelled(true);
                        return;
                    }
                    image.setSetByUUID(e.getPlayer().getUniqueId());
                    image.setWorld(e.getPlayer().getWorld().getName());
                    placeImage(e.getClickedBlock(), e.getBlockFace(), e.getPlayer().getLocation().getYaw(),
                            image);
                    e.getPlayer().setItemInHand(null);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            MapMeta map = (MapMeta)((ItemFrame)e.getRightClicked()).getItem().getItemMeta();
            if (Images.images.containsKey(map.getMapId())) {
                if (ParlaImages.backgroundExecutor.isFull()) {
                    e.getPlayer().sendMessage("There are too many requests right now, please wait.");
                    e.setCancelled(true);
                    return;
                }
                Image image = Images.images.get(map.getMapId());
                Images.removeMap(image);
                if (image.getSetByUUID().equals(e.getPlayer().getUniqueId())) {
                    image.getMapList().forEach(v -> {
                        MapView mapView = Bukkit.getMap(v.getID());
                        mapView.getRenderers().forEach(mapView::removeRenderer);

                        World world = Bukkit.getWorld(image.getWorld());
                        world.getNearbyEntities(v.getLocation(), 1, 1, 1).forEach(entity -> {
                            if (entity.getType().equals(EntityType.ITEM_FRAME) &&
                                    entity.getLocation().getBlock().equals(v.getLocation().getBlock())) entity.remove();
                        });
                        Images.images.remove(v.getID());
                    });
                    e.getPlayer().getInventory().addItem(Images.getImageStick(image));
               }
            }
        }
    }
}
