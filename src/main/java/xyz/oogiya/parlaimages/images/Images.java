package xyz.oogiya.parlaimages.images;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import sun.security.krb5.Config;
import xyz.oogiya.parlaimages.util.HiddenStringUtils;
import xyz.oogiya.parlaimages.util.ImageUtils;
import xyz.oogiya.parlaimages.util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Images {

    public static File dataFolder;

    private static File imageDir;

    private static File mapsFile;
    private static FileConfiguration mapsConfig;

    public static Map<UUID, Image> playerImages = new HashMap<UUID, Image>();

    private static Server server;

    public static List<Image> imageList = new ArrayList<>();

    public static Map<Long, Image> imageMap = new HashMap<>();
    //private static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    public Images(File dataFolder, File imageDir, Server server) {
        this.dataFolder = dataFolder;
        this.server = server;
        this.imageDir = imageDir;
    }

    public static void saveMap(Image image) {
        List<Location> locationList = image.getMapLocationArray();
        Location loc = locationList.get(0);
        String coord = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
        ConfigurationSection section = mapsConfig.createSection(coord);
        section.set("image", image.getFilename());
        section.set("width", image.getWidth());
        section.set("height", image.getHeight());
        section.set("widthDirection", image.getWidthDirection());
        section.set("heightDirection", image.getHeightDirection());
        section.set("world", image.getWorld());
        for (int i = 0; i < locationList.size(); i++) {
            loc = locationList.get(i);
            section.set("coords." + String.valueOf(i), loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        }
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
        for (String item : mapsConfig.getKeys(false)) {
            if (mapsConfig.getConfigurationSection(item + ".coords") != null) {
                ConfigurationSection section = mapsConfig.getConfigurationSection(item);
                Image image = new Image(section.getString(".image"), section.getInt(".width"), section.getInt(".height"),
                        getImage(section.getString(".image")));
                image.setWidthDirection(section.getString(".widthDirection"));
                image.setHeightDirection(section.getString(".heightDirection"));
                image.setWorld(section.getString("world"));
                for (String s : section.getConfigurationSection("coords").getKeys(false)) {
                    String split[] = section.getString("coords" + "." + s).split(",");
                    image.addMapLocationToArray(new Location(Bukkit.getWorld(section.getString(".world")),
                            Double.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2])));
                }
                imageList.add(image);
            }
        }
        test();
    }

    private static void test() {
        imageList.forEach(v -> {
            World world = Bukkit.getWorld(v.getWorld());
            v.getMapLocationArray().forEach(k -> {
                //Block block = Bukkit.getWorld(v.getWorld()).getBlockAt(k);
                System.out.println(world.getNearbyEntities(k, 1, 1, 1));
            });
        });
    }

    public static boolean isImageExists(String imageName) {
        File file = new File(imageDir, File.separatorChar + imageName);
        System.out.println(file);
        if (file.exists()) return true;
        return false;
    }

    public static ItemStack getImageStick(Image image) {
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
