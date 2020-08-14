package xyz.oogiya.parlaimages.images;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
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

    public static Map<Long, Image> imageMap = new HashMap<>();

    public Images(File dataFolder, File imageDir, Server server) {
        this.dataFolder = dataFolder;
        this.server = server;
        this.imageDir = imageDir;
    }

    public static void saveMap(Image image) {
        List<Image.MapIndexLocation> locationList = image.getMapList();
        Location loc = locationList.get(0).getLocation();
        String coord = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
        ConfigurationSection section = mapsConfig.createSection(coord);
        section.set("image", image.getFilename());
        section.set("width", image.getWidth());
        section.set("height", image.getHeight());
        section.set("widthDirection", image.getWidthDirection());
        section.set("heightDirection", image.getHeightDirection());
        section.set("world", image.getWorld());
        locationList.forEach(k -> {

            ConfigurationSection subSection = section.createSection("maps." + k.getID());
            subSection.set("i", k.getPoint().x);
            subSection.set("j", k.getPoint().y);
            subSection.set("location", k.getLocation().getBlockX() + "," + k.getLocation().getBlockY() + "," +
                            k.getLocation().getBlockZ());
        });
        if (Utils.STICKS_BY_PLAYER) section.set("UUID", image.getUUID().toString());
        section.set("placedby", image.getSetByUUID().toString());
        try {
            mapsConfig.save(mapsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMaps() {
        mapsFile = new File(dataFolder, "maps.yml");

        if (!mapsFile.exists()) {
            try {
                mapsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mapsConfig = YamlConfiguration.loadConfiguration(mapsFile);
        for (String s : mapsConfig.getKeys(false)) {
            String filename = mapsConfig.getString(s + ".image");
            BufferedImage bufferedImage = getImage(filename);
            int width = mapsConfig.getInt(s + ".width");
            int height = mapsConfig.getInt(s + ".height");
            String world = mapsConfig.getString(s + ".world");
            Image image = new Image(filename, width, height, bufferedImage);
            ConfigurationSection maps = mapsConfig.getConfigurationSection(s + ".maps");
            for (String item : maps.getKeys(false)) {
                int i = maps.getInt(item + ".i");
                int j = maps.getInt(item + ".j");

                MapView mapView = Bukkit.getMap(Integer.valueOf(item));
                mapView.addRenderer(new ImageMapRenderer(image.getImage(), i, j));
            }
        }
    }

    public static boolean isImageExists(String imageName) {
        File file = new File(imageDir, File.separatorChar + imageName);
        if (file.exists()) return true;
        return false;
    }

    public static ItemStack getImageStick(Image image) {
        ImageStick imageStick = new ImageStick(Material.STICK, image);
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
