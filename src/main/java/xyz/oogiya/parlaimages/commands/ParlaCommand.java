package xyz.oogiya.parlaimages.commands;

import com.sun.scenario.Settings;
import org.bukkit.Server;
import org.bukkit.command.Command;

import java.util.logging.Logger;

public abstract class ParlaCommand {
    private final transient String name;

    protected static final Logger logger = Logger.getLogger("Parlamentum");

    protected transient Settings settings;

    public ParlaCommand(String name) {
        this.name = name;
    }

    public abstract void run(Server server, CommandSource commandSource, Command cmd, String label, String args[]);

    private final String getName() {
        return this.name;
    }

}
