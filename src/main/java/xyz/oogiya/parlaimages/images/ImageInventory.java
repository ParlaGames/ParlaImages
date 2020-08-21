package xyz.oogiya.parlaimages.images;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import xyz.oogiya.parlaimages.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageInventory {

	private final UUID uuid;

	private List<Image> images = new ArrayList<>();

	private Inventory imageInventory;

	private final int imageInventorySlots = 1 * 9; // multiply by 9

	public ImageInventory(UUID uuid) {
		this.uuid = uuid;
		this.createInventory();
	}

	public boolean addImageToList(Image image) {
		if (this.images.size() >= this.imageInventorySlots) { return false; }
		this.images.add(image);
		return true;
	}


	public void createInventory() {
		this.imageInventory = Bukkit.createInventory(Bukkit.getPlayer(this.uuid), this.imageInventorySlots);

		if (!this.images.isEmpty()) {
			this.images.forEach(image -> {
				this.imageInventory.addItem(Images.getImageStick(image));
			});
		}
	}

	public UUID getUuid() { return this.uuid; }

	public List<Image> getImages() { return this.images; }

	public Inventory getImageInventory() { return this.imageInventory; }
}
