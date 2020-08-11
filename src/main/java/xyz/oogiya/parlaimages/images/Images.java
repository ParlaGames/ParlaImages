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

    private static File imageDir;
    //public static Map<UUID, Image> playerImages = new HashMap<UUID, Image>();

    private static Server server;

    public static List<Image> imageList = new ArrayList<>();

    public static Map<Long, Image> imageMap = new HashMap<>();
    //private static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    public Images(File dataFolder, File imageDir, Server server) {
        this.dataFolder = dataFolder;
        this.server = server;
        this.imageDir = imageDir;
    }

    private boolean saveMaps() {

        if (!dataFolder.exists()) return false;


        return false;
    }

    private void loadMaps(String filename) {

    }

    public static boolean isImageExists(String imageName) {
        File file = new File(imageDir, File.separatorChar + imageName);
        System.out.println(file);
        if (file.exists()) return true;
        return false;
    }

    public static ItemStack getImageStick(Image image) {
        System.out.println("3");
        ImageStick imageStick = new ImageStick(Material.STICK, image);
        System.out.println("4");
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
        File file = new File(imageDir, File.separatorChar + filename);
        BufferedImage image = null;
        if (!file.exists()) { return null; }
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Error");
        }

        return image;
    }

}
