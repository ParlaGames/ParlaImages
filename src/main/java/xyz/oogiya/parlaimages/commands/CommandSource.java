package xyz.oogiya.parlaimages.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSource {

    protected CommandSender sender;

    public CommandSource(CommandSender sender) {
        this.sender = sender;
    }

    public final CommandSender getSender() {
        return this.sender;
    }

    public final Player getPlayer() {
        if (this.sender instanceof Player) {
            return (Player)this.sender;
        } return null;
    }

    public final boolean isPlayer() {
        return this.sender instanceof Player;
    }

    public final CommandSender setSender(CommandSender sender) {
        return this.sender = sender;
    }

    public void sendMessage(String msg) {
        if (!msg.isEmpty()) {
            this.sender.sendMessage(msg);
        }
    }

}
