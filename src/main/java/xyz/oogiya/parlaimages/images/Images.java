package xyz.oogiya.parlaimages.images;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import xyz.oogiya.parlaimages.ParlaImages;
import xyz.oogiya.parlaimages.tasks.LoadImagesTask;
import xyz.oogiya.parlaimages.tasks.RemoveImageTask;
import xyz.oogiya.parlaimages.tasks.SaveImageTask;
import xyz.oogiya.parlaimages.util.Utils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

public class Images {

    public static File dataFolder;

    private static File imageDir;

    private static File mapsFile;

    private static FileConfiguration mapsConfig;

    private static Server server;

    public static List<Image> imageList = new ArrayList<>();

    public static Map<Integer, Image> images = new HashMap<>();

    public static Map<Long, Image> imageMap = new HashMap<>();

    public static Map<Location, Image> imageLocationMap = new HashMap<>();

    public Images(File dataFolder, File imageDir, Server server) {
        this.dataFolder = dataFolder;
        this.server = server;
        this.imageDir = imageDir;

        this.mapsFile = new File(dataFolder, "maps.yml");

        if (!mapsFile.exists()) {
            try {
                mapsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void saveMap(Image image) {
        ParlaImages.backgroundExecutor.addTask(new SaveImageTask(image, mapsFile));
    }

    public static void removeMap(Image image) {
        ParlaImages.backgroundExecutor.addTask(new RemoveImageTask(image, mapsFile));
    }

    public static void loadMaps() {
        ParlaImages.backgroundExecutor.addTask(new LoadImagesTask(mapsFile));
    }

    public static boolean isImageExists(String imageName) {
        File file = new File(imageDir, File.separatorChar + imageName);
        if (file.exists()) return true;
        return false;
    }

    public static ItemStack getImageStick(Image image) {
        ImageStick imageStick = new ImageStick(image);
        return imageStick.getItemStack();
    }

    public static ItemStack getMapItem(int i, int j, Image image) {
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        if (imageList.contains(image)) {
            MapMeta meta = (MapMeta)item.getItemMeta();
            meta.setMapId(image.getMapViewIDByPoint(new Point(i, j)));
            item.setItemMeta(meta);
            return item;
        }
        MapView map = server.createMap(server.getWorld(image.getWorld()));
        map.getRenderers().forEach(map::removeRenderer);
        map.addRenderer(new ImageMapRenderer(image.getImage(), i, j));

        MapMeta meta = ((MapMeta)item.getItemMeta());
        meta.setMapView(map);
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
