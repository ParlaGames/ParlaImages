package xyz.oogiya.parlaimages.images;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import xyz.oogiya.parlaimages.util.HiddenStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageStick {

    private final ItemStack itemStack;
    private final Image image;

    public ImageStick(ItemStack itemStack, Image image) {
        this.itemStack = itemStack;
        this.image = image;
        setHiddenMeta();
    }

    public ImageStick(Material stick, Image image) {
        this.itemStack = new ItemStack(stick);
        this.image = image;
        setHiddenMeta();
    }

    private void setHiddenMeta() {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<String> lore;
        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }
        lore.add(HiddenStringUtils.encodeString(String.valueOf("prlm/" +  this.image.getImageKey())));
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
    }

    public ItemStack getItemStack() { return this.itemStack; }
    public Image getImage() { return this.image; }
}
