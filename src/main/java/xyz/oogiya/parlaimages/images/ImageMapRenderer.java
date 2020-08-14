package xyz.oogiya.parlaimages.images;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import xyz.oogiya.parlaimages.util.ImageUtils;

import java.awt.image.BufferedImage;

public class ImageMapRenderer extends MapRenderer {

    private BufferedImage image;
    private final int i;
    private final int j;


    public ImageMapRenderer(BufferedImage image, int i, int j) {
        this.image = image;
        this.i = i;
        this.j = j;
        imageRescale();
    }

    private void imageRescale() {
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
        this.image = image.getSubimage(x1, y1, width, height);
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if (this.image != null) {
            canvas.drawImage(0,0, image);
        }
    }
}
