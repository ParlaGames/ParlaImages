package xyz.oogiya.parlaimages.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;

public class EntityEvents implements Listener {

    @EventHandler
    public void onFrameBreakEntity(HangingBreakByEntityEvent e) {
        if (e.getEntity().getType().equals(EntityType.ITEM_FRAME)) {
            ItemFrame itemFrame = (ItemFrame)e.getEntity();
            MapMeta meta = (MapMeta)itemFrame.getItem().getItemMeta();
            MapView map = meta.getMapView();

            if (Images.images.containsKey(map.getId())) {
                Image image = Images.images.get(map.getId());
                Images.removeMap(image);
                if (image.getSetByUUID().equals(image.getSetByUUID())) {
                    image.getMapList().forEach(v -> {
                        MapView mapView = Bukkit.getMap(v.getID());
                        mapView.getRenderers().forEach(mapView::removeRenderer);

                        World world = Bukkit.getWorld(image.getWorld());
                        world.getNearbyEntities(v.getLocation(), 1, 1, 1).forEach(entity -> {
                            if (entity.getType().equals(EntityType.ITEM_FRAME) &&
                                    entity.getLocation().getBlock().equals(v.getLocation().getBlock())) {
                                entity.remove();
                            }
                        });
                        Images.images.remove(v.getID());
                    });
                    Bukkit.getPlayer(image.getUUID()).getInventory().addItem(Images.getImageStick(image));
                }
            }
        }
    }

    @EventHandler
    public void onFrameBreak(HangingBreakEvent e) {
        if (e.getEntity().getType().equals(EntityType.ITEM_FRAME)) {
            ItemFrame itemFrame = (ItemFrame)e.getEntity();
            MapMeta meta = (MapMeta)itemFrame.getItem().getItemMeta();
            MapView map = meta.getMapView();

            if (Images.images.containsKey(map.getId())) {
                if (e.getCause().equals(HangingBreakEvent.RemoveCause.EXPLOSION)) {
                    e.setCancelled(true);
                    return;
                }
                Image image = Images.images.get(map.getId());
                Images.removeMap(image);
                if (image.getSetByUUID().equals(image.getSetByUUID())) {
                    image.getMapList().forEach(v -> {
                        MapView mapView = Bukkit.getMap(v.getID());
                        mapView.getRenderers().forEach(mapView::removeRenderer);

                        World world = Bukkit.getWorld(image.getWorld());
                        world.getNearbyEntities(v.getLocation(), 1, 1, 1).forEach(entity -> {
                            if (entity.getType().equals(EntityType.ITEM_FRAME) &&
                                    entity.getLocation().getBlock().equals(v.getLocation().getBlock())) {
                                entity.remove();
                            }
                        });
                        Images.images.remove(v.getID());
                    });
                    Bukkit.getPlayer(image.getUUID()).getInventory().addItem(Images.getImageStick(image));
                }
            }
        }
    }
}
