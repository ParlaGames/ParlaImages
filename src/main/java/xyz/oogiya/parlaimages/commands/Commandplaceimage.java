package xyz.oogiya.parlaimages.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.util.StringUtil;
import xyz.oogiya.parlaimages.images.Image;
import xyz.oogiya.parlaimages.images.Images;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class Commandplaceimage extends ParlaCommand {
    public Commandplaceimage() {
        super("PlaceImage");
    }


    @Override
    public void run(Server server, CommandSource commandSource, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            if (Images.isImageExists(args[0])) {
                Image image = new Image(args[0], Images.getImage(args[0]));
                commandSource.getPlayer().getInventory().addItem(Images.getImageStick(image));
            }

        }
        else if (args.length == 2) {
            if (Images.isImageExists(args[0])) {
                double scale;
                try {
                    scale = Double.valueOf(args[1]);
                } catch (NumberFormatException ex) {
                    scale = 1;
                }
                if (scale <= 1) {
                    Image image = new Image(args[0], scale, Images.getImage(args[0]));
                    commandSource.getPlayer().getInventory().addItem(Images.getImageStick(image));
                }
            }

        }
        else if (args.length >= 3) {
            if (Images.isImageExists(args[0])) {
                if (StringUtils.isNumeric(args[1]) && StringUtils.isNumeric(args[2])) {
                    int width = Integer.valueOf(args[1]);
                    int height = Integer.valueOf(args[2]);
                    if (width > 0 && height > 0) {
                        Image image = new Image(args[0], width, height, Images.getImage(args[0]));
                        commandSource.getPlayer().getInventory().addItem(Images.getImageStick(image));
                    }
                }
            }
        }
    }
}
