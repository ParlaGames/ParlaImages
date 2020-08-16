package xyz.oogiya.parlaimages.tasks;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;
import xyz.oogiya.parlaimages.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class SaveImageTask extends Task {

    private final Image image;
    private final File file;
    public SaveImageTask(Image image, File mapsFile) {
        this.image = image;

        this.file = mapsFile;
    }

    @Override
    public void run() {
        FileConfiguration mapsConfig = YamlConfiguration.loadConfiguration(this.file);
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
            if (!Images.images.containsKey(k.getID())) Images.images.put(k.getID(), image);
        });
        if (Utils.STICKS_BY_PLAYER) section.set("UUID", image.getUUID().toString());
        section.set("placedby", image.getSetByUUID().toString());
        try {
            mapsConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Images.imageList.add(image);
    }

    @Override
    public void onComplete() {
    }

    @Override
    public boolean checkForDuplicate() {
        return false;
    }
}
