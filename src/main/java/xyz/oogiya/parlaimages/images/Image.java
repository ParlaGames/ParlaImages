package xyz.oogiya.parlaimages.images;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Image implements ConfigurationSerializable {

    private String filename;
    private int width;
    private int height;
    private double scale;
    private BufferedImage image;

    public Image(String filename) {
        this.filename = filename;
        this.image = Images.getImage(this.filename);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.scale = 1;
    }

    public Image(String filename, double scale) {
        this.filename = filename;
        this.scale = scale;
        this.image = Images.getImage(filename);
        this.width = (int)(this.image.getWidth() * scale);
        this.height = (int)(this.image.getHeight() * scale);
    }

    public Image(String filename, int width, int height) {
        this.filename = filename;
        this.width = width;
        this.height = height;
        this.image = Images.getImage(this.filename);
    }

    public BufferedImage getImage() { return this.image; }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("image", this.filename);
        map.put("width", this.width);
        map.put("height", this.height);
        map.put("scale", this.scale);

        return map;
    }


    public String getFilename() { return this.filename; }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public double scale() { return this.scale; }
}
