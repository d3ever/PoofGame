package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class StartKitCommand extends ACommand {

    public StartKitCommand() {
        super("startkit", "start");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        int interval = (int) (System.currentTimeMillis() / 1000L / 60L - pp.getLastKit());
        if (interval >= 60L) {
            PrisonPlayer.giveStartKit(p);
            p.sendMessage(Utils.f("&7Вы получили начальный набор."));
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Вы можете сделать это не раньше чем через " + ChatColor.RED + (60L - interval) + ChatColor.YELLOW + " минут");
        return false;
    }
}
