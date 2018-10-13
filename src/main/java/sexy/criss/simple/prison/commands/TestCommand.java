package sexy.criss.simple.prison.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.manager.ActionManager;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class TestCommand extends ACommand {

    public TestCommand() {
        super("test");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.admin");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        switch (args.length) {
            case 2:
                switch (args[0].toLowerCase()) {
                    case "border":
                        try {
                            int val = Integer.parseInt(args[1]);

                            Utils.sendRedScreen(p, val);
                            p.sendMessage(Utils.f("&7Значение: %d.", val));
                        } catch (Exception ex) { p.sendMessage(Utils.f("&4" + ex.getMessage())); }
                        break;
                }
                break;
            default:
                p.sendMessage(Utils.f("&cUnknown arguments."));
                break;
        }
        return false;
    }
}
