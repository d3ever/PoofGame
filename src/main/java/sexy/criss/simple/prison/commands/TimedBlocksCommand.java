package sexy.criss.simple.prison.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.manager.Reference;
import sexy.criss.simple.prison.manager.builders.TimedBlock;
import sexy.criss.simple.prison.utils.ACommand;

public class TimedBlocksCommand extends ACommand {

    public TimedBlocksCommand() {
        super("timedblocks", "tm", "tblocks");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.timedblocks");
    }

    private int interval = 20;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        switch (args.length) {
            case 0:
                help(p);
                return true;
            case 1:
                switch (args[0].toLowerCase()) {
                    case "on":
                        if(TimedBlock.players_activity.containsKey(p)) {
                            p.sendMessage(Reference.TBLOCKS_ALREADY_ACTIVE.get());
                            return true;
                        }
                        TimedBlock.players_activity.put(p, interval);
                        p.sendMessage(Reference.TBLOCKS_MODE_ACTIVATED.get());
                        return true;
                    case "off":
                        if(!TimedBlock.players_activity.containsKey(p)) {
                            p.sendMessage(Reference.TBLOCKS_DONT_ACTIVE.get());
                            return true;
                        }
                        TimedBlock.players_activity.remove(p);
                        p.sendMessage(Reference.TBLOCKS_MODE_DEACTIVATED.get());
                        return true;
                    case "interval":
                        p.sendMessage(Reference.USAGE_SYNTAX.get("timedblocks interval <значение>"));
                        return true;
                    default:
                        help(p);
                        return true;
                }
            default:
                switch (args[0].toLowerCase()) {
                    case "interval":
                        if (StringUtils.isNumeric(args[1])) {
                            this.interval = Integer.valueOf(args[1]);
                            p.sendMessage(Reference.TBLOCKS_INTERVAL_UPDATED.get(this.interval));
                            return true;
                        }
                        p.sendMessage(Reference.NUMBER_FAIL.get());
                        return true;
                    default:
                        help(p);
                        return true;
                }
        }
    }

    private void help(Player p) {
        p.sendMessage(Reference.USAGE_SYNTAX.get("timedblocks on"));
        p.sendMessage(Reference.USAGE_SYNTAX.get("timedblocks off"));
        p.sendMessage(Reference.USAGE_SYNTAX.get("timedblocks interval <значение>"));
    }
}
