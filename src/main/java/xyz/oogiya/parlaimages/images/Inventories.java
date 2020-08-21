package xyz.oogiya.parlaimages.images;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Inventories {

	private final File dataFolder;
	private final File inventoryFile;

	private final FileConfiguration inventoryConfig;

	private List<ImageInventory> inventoryList = new ArrayList<>();

	public Inventories(File dataFolder) {
		this.dataFolder = dataFolder;

		this.inventoryFile = new File(dataFolder, "inventories.yml");

		if (!inventoryFile.exists()) {
			try {
				inventoryFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.inventoryConfig = YamlConfiguration.loadConfiguration(this.inventoryFile);
	}

	public void save(ImageInventory imageInventory) {
		ConfigurationSection section = this.inventoryConfig.createSection(imageInventory.getUuid().toString());
		imageInventory.getImages().forEach(image -> {
			Location loc = image.getMapList().get(0).getLocation();
			section.addDefault("images:", loc.getWorld() + "," +
					loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		});
	}

	public void load() {
		for (String uuid : this.inventoryConfig.getKeys(false)) {
			ImageInventory imageInventory = new ImageInventory(UUID.fromString(uuid));
			ConfigurationSection images = this.inventoryConfig.getConfigurationSection(uuid + ".images");
			for (String location : images.getKeys(false)) {
				String loc[] = location.split(",");
				imageInventory.addImageToList(Images.imageLocationMap.get(new Location(Bukkit.getWorld(loc[0]),
						Integer.valueOf(loc[1]), Integer.valueOf(loc[2]), Integer.valueOf(loc[3]))));

			}
			this.inventoryList.add(imageInventory);
		}

	}

}
