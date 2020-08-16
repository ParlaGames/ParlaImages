package xyz.oogiya.parlaimages.tasks;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.FutureTask;

public class RemoveImageTask extends Task {

    private final Image image;
    private final File file;

    public RemoveImageTask(Image image, File file) {
        this.image = image;
        this.file = file;
    }

    @Override
    public void run() {
        FileConfiguration mapsConfig = YamlConfiguration.loadConfiguration(this.file);
        Location loc = image.getMapList().get(0).getLocation();
        String coord = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
        mapsConfig.set(coord, null);
        try {
            mapsConfig.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Images.imageList.remove(image);
    }

    @Override
    public void onComplete() {

        //image.resetMapList();
    }

    @Override
    public boolean checkForDuplicate() {
        return false;
    }
}
