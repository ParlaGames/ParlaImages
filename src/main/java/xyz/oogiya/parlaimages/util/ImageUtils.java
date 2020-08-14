package xyz.oogiya.parlaimages.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;

import java.util.List;
import java.util.Random;

public class ImageUtils {

    public static String IMAGES_DIR = "images";

    private static Random random = new Random();

    public static final int MAP_WIDTH = 128;
    public static final int MAP_HEIGHT = 128;

    public static long createRandomKey() {
        return Long.valueOf(random.nextInt(9999) + 1);
    }
    public static Image itemStackToImage(ItemStack item) {
        if (!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return null;
        List<String> lore = meta.getLore();
        for (int i = 0; i < lore.size(); i++) {
            if (HiddenStringUtils.hasHiddenString(lore.get(i))) {
               String line = HiddenStringUtils.extractHiddenString(lore.get(i));
               if (line.contains("prlm")) {
                   line = line.replace("prlm/", "");
                   Image image = Images.imageMap.get(Long.valueOf(line));
                   lore.remove(i);
                   meta.setLore(lore);
                   item.setItemMeta(meta);
                   return image;
               }
            }
        };
        return null;
    }

    public enum ImageError {
        NO_WALL,
        NO_SPACE,
        OVERLAP,
        SUCCESS
    }

}
