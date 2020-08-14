package xyz.oogiya.parlaimages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oogiya.parlaimages.commands.CommandSource;
import xyz.oogiya.parlaimages.commands.ParlaCommand;
import xyz.oogiya.parlaimages.events.PlayerEvents;
import xyz.oogiya.parlaimages.events.ServerEvents;
import xyz.oogiya.parlaimages.images.Images;
import xyz.oogiya.parlaimages.util.ImageUtils;


import java.io.File;
import java.io.IOException;


public class ParlaImages extends JavaPlugin {

    private File imagesFolder;

    private Images images;

    public static FileConfiguration config;


    public void onEnable() {
        File file = new File("maps.yml");
        if (!file.exists()) saveResource("maps.yml", false);
        //Make images folder
        this.imagesFolder = new File(getDataFolder(), ImageUtils.IMAGES_DIR);
        if (!this.imagesFolder.exists()) new File(getDataFolder(), ImageUtils.IMAGES_DIR).mkdirs();

        this.images = new Images(getDataFolder(), imagesFolder, this.getServer());
        this.saveDefaultConfig();
        this.config = this.getConfig();

        Images.loadMaps();

        this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        //this.getServer().getPluginManager().registerEvents(new ServerEvents(), this);
    }
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        runCommand(sender, cmd, label, args, "xyz.oogiya.parlaimages.commands.Command");
        return false;
    }

    public void runCommand(CommandSender sender, Command cmd, String label, String[] args,
                           String commandPath) {
        CommandSource commandSource = new CommandSource(sender);

        ParlaCommand command;

        try {
            command = (ParlaCommand) this.getClassLoader().loadClass(commandPath + cmd.getName()).newInstance();
            command.run(getServer(), commandSource, cmd, label, args);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
