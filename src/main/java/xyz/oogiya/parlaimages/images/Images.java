package xyz.oogiya.parlaimages.images;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import xyz.oogiya.parlaimages.util.HiddenStringUtils;
import xyz.oogiya.parlaimages.util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Images {

    public static File dataFolder;

    public static Map<UUID, Image> playerImages = new HashMap<UUID, Image>();

    private static Server server;

    private static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    private static List<ImageStick> imageSticks = new ArrayList<>();

    public Images(File dataFolder, Server server) {
        this.dataFolder = dataFolder;
        this.server = server;
    }

    private boolean loadMaps(String filename) {
        if (!this.images.containsKey(filename.toLowerCase())) return false;

        BufferedImage image = getImage(filename);

        if (image == null) return false;

        return true;
    }

    public static ItemStack getImageStick(Image image) {
        Bukkit.getLogger().info(image.toString());
        ImageStick imageStick = new ImageStick(Material.STICK, image);
        return imageStick.getItemStack();
    }

    public static ItemStack getMapItem(int i, int j, Image image) {
        int x1 = (int)(Math.round(i * ImageUtils.MAP_WIDTH));
        int y1 = (int)(Math.round(j * ImageUtils.MAP_HEIGHT));
        int x2 = (int)(Math.round((i+1) * ImageUtils.MAP_WIDTH));
        int y2 = (int)(Math.round((j+1) * ImageUtils.MAP_HEIGHT));
        int width = ImageUtils.MAP_WIDTH;
        int height = ImageUtils.MAP_HEIGHT;
        if ((image.getWidth() / x2) < 1) {
            x1 = x2 - image.getWidth();
            width = x1;
        }
        if ((image.getHeight() / y2) < 1) {
            y1 = y2 - image.getHeight();
            height = y1;
        }
        BufferedImage bufferedImage = image.getImage().getSubimage(x1, y1, width, height);
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapView mapView = server.createMap(server.getWorlds().get(0));
        mapView.getRenderers().forEach(mapView::removeRenderer);
        mapView.addRenderer(new MapRenderer() {
            @Override
            public void render(MapView map, MapCanvas canvas, Player player) {
                canvas.drawImage(0, 0, bufferedImage);
            }
        });
        MapMeta meta = ((MapMeta)item.getItemMeta());
        meta.setMapView(mapView);
        item.setItemMeta(meta);

        return item;
    }

    public static BufferedImage getImage(String filename) {
        File file = new File(dataFolder, File.separatorChar + ImageUtils.IMAGES_DIR + File.separatorChar + filename);
        BufferedImage image = null;
        if (!file.exists()) { return null; }
        try {
            image = ImageIO.read(file);
            images.put(filename.toLowerCase(), image);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Error");
        }

        return image;
    }

}
