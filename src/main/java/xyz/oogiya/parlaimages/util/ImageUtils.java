package xyz.oogiya.parlaimages.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.oogiya.parlaimages.images.Image;

import java.util.List;

public class ImageUtils {

    public static String IMAGES_DIR = "images";

    public static final int MAP_WIDTH = 128;
    public static final int MAP_HEIGHT = 128;

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
                   String[] decoded = line.split("/");
                   Bukkit.getLogger().info(decoded[0] + " " + decoded[1] + " " + decoded[2]);
                   Image image = new Image(decoded[0], Integer.valueOf(decoded[1]), Integer.valueOf(decoded[2]));
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
