package sexy.criss.simple.prison.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.MenuUtils;

public class StatsCommand extends ACommand {

    public StatsCommand() {
        super("stats", "trainer");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        MenuUtils.showStatsMenu(p);
        return false;
    }
}
