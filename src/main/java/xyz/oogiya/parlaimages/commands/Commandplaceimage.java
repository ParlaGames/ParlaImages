package xyz.oogiya.parlaimages.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class Commandplaceimage extends ParlaCommand {
    public Commandplaceimage() {
        super("PlaceImage");
    }

    private void removePlayerFromMap(UUID uuid) {
        if (Images.playerImages.containsKey(uuid))
            Images.playerImages.remove(uuid);
    }

    @Override
    public void run(Server server, CommandSource commandSource, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            BufferedImage bufferedImage = Images.getImage(args[0]);
            removePlayerFromMap(commandSource.getPlayer().getUniqueId());
            Images.playerImages.put(commandSource.getPlayer().getUniqueId(), new Image(args[0], bufferedImage.getWidth(), bufferedImage.getHeight(), 1));
        }
        else if (args.length == 2) {
            BufferedImage bufferedImage = Images.getImage(args[0]);
            double scale = Double.valueOf(args[1]);
            removePlayerFromMap(commandSource.getPlayer().getUniqueId());
            Images.playerImages.put(commandSource.getPlayer().getUniqueId(), new Image(args[0], (int)(bufferedImage.getWidth() * scale),
                    (int)(bufferedImage.getHeight() * scale), scale));
        }
        else if (args.length >= 3) {
            BufferedImage bufferedImage = Images.getImage(args[0]);
            int width = Integer.valueOf(args[2]);
            int height = Integer.valueOf(args[3]);
            removePlayerFromMap(commandSource.getPlayer().getUniqueId());
            Images.playerImages.put(commandSource.getPlayer().getUniqueId(), new Image(args[0], width, height, -1));

        }
        Bukkit.getLogger().info(Images.playerImages.toString());
    }
}
