package xyz.oogiya.parlaimages.images;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import xyz.oogiya.parlaimages.util.ImageUtils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;

public class Image {

    private List<Location> mapLocationArray = new ArrayList<Location>();

    private String filename;
    private int originalWidth;
    private int originalHeight;
    private int width;
    private int height;
    private double scaleX;
    private double scaleY;
    private BufferedImage image;

    private long key;

    public Image(String filename, BufferedImage image) {
        this.filename = filename;
        this.image = image;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.scaleX = 1; this.scaleY = 1;
        this.key = ImageUtils.createRandomKey();
        addImageToMap();
    }

    public Image(String filename, double scale, BufferedImage image) {
        this.filename = filename;
        this.scaleX = scale; this.scaleY = scale;
        this.image = image;
        this.originalWidth = image.getWidth();
        this.originalHeight = image.getHeight();
        this.width = (int)(this.image.getWidth() * scale);
        this.height = (int)(this.image.getHeight() * scale);
        this.resetImageSize();
        this.key = ImageUtils.createRandomKey();
        addImageToMap();
    }

    public Image(String filename, int width, int height, BufferedImage image) {
        this.filename = filename;
        this.image = image;
        this.originalWidth = this.image.getWidth();
        this.originalHeight = this.image.getHeight();
        this.width = width;
        this.height = height;
        calculateScale();
        resetImageSize();
        this.key = ImageUtils.createRandomKey();
        addImageToMap();
    }

    private void addImageToMap() {
        while (Images.imageMap.containsKey(this.key)) this.key = ImageUtils.createRandomKey();
        Images.imageMap.put(this.key, this);
    }

    public void addMapLocationToArray(Location location) {
        this.mapLocationArray.add(location);
    }

    private void calculateScale() {
        this.scaleX = Double.valueOf(this.width) / Double.valueOf(this.originalWidth);
        this.scaleY = Double.valueOf(this.height) / Double.valueOf(this.originalHeight);
    }

    public BufferedImage getImage() { return this.image; }

    private void resetImageSize() {
        if (this.scaleX < 1 || this.scaleY < 1) {
            System.out.println(this.width);
            BufferedImage resizedImage = new BufferedImage(this.width, this.height, this.image.getType());
            AffineTransform at = new AffineTransform();
            at.scale(this.scaleX, this.scaleY);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            this.image = scaleOp.filter(this.image, resizedImage);
        }
    }

    public long getImageKey() { return this.key; }

    public String getFilename() { return this.filename; }

    public int getWidth() { return this.width; }

    public int getHeight() { return this.height; }

    public double getScaleX() { return this.scaleX; }

    public double getScaleY() { return this.scaleY; }
}
