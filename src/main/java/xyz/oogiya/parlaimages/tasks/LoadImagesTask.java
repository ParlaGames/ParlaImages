package xyz.oogiya.parlaimages.tasks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.map.MapView;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.ImageMapRenderer;
import xyz.oogiya.parlaimages.images.Images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LoadImagesTask extends Task {

    private final File file;

    public LoadImagesTask(File mapsFile) {

        this.file = mapsFile;

    }

    @Override
    public void run() {
        FileConfiguration mapsConfig = YamlConfiguration.loadConfiguration(this.file);
        mapsConfig = YamlConfiguration.loadConfiguration(file);
        for (String s : mapsConfig.getKeys(false)) {
            String filename = mapsConfig.getString(s + ".image");
            BufferedImage bufferedImage = Images.getImage(filename);
            int width = mapsConfig.getInt(s + ".width");
            int height = mapsConfig.getInt(s + ".height");
            String world = mapsConfig.getString(s + ".world");
            Image image = new Image(filename, width, height, bufferedImage);
            image.setSetByUUID(UUID.fromString(mapsConfig.getString(s + ".placedby")));
            image.setUUID(UUID.fromString(mapsConfig.getString(s + ".UUID")));
            ConfigurationSection maps = mapsConfig.getConfigurationSection(s + ".maps");
            for (String item : maps.getKeys(false)) {
                int i = maps.getInt(item + ".i");
                int j = maps.getInt(item + ".j");
                int index = Integer.valueOf(item);
                MapView mapView = Bukkit.getMap(index);
                mapView.addRenderer(new ImageMapRenderer(image.getImage(), i, j));
                Images.images.put(index, image);
            }
            Images.imageList.add(image);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public boolean checkForDuplicate() {
        return false;
    }
}
