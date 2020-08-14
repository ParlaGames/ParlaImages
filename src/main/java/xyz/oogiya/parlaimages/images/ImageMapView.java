package xyz.oogiya.parlaimages.images;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.List;

public class ImageMapView implements MapView {

    List<MapRenderer> renderers = new ArrayList<>();
    private int mapID = 0;

    public ImageMapView(int id) {
        this.mapID = id;
    }

    @Override
    public int getId() {
        return this.mapID;
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    public Scale getScale() {
        return Scale.NORMAL;
    }

    @Override
    public void setScale(Scale scale) {

    }

    @Override
    public int getCenterX() {
        return 0;
    }

    @Override
    public int getCenterZ() {
        return 0;
    }

    @Override
    public void setCenterX(int x) {

    }

    @Override
    public void setCenterZ(int z) {

    }

    @Override
    public World getWorld() {
        return Bukkit.getWorlds().get(0);
    }

    @Override
    public void setWorld(World world) {

    }

    @Override
    public List<MapRenderer> getRenderers() {
        return this.renderers;
    }

    @Override
    public void addRenderer(MapRenderer renderer) {
        this.renderers.add(renderer);
    }

    @Override
    public boolean removeRenderer(MapRenderer renderer) {
        return this.renderers.remove(renderer);
    }

    @Override
    public boolean isTrackingPosition() {
        return false;
    }

    @Override
    public void setTrackingPosition(boolean trackingPosition) {

    }

    @Override
    public boolean isUnlimitedTracking() {
        return true;
    }

    @Override
    public void setUnlimitedTracking(boolean unlimited) {

    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void setLocked(boolean locked) {

    }
}
