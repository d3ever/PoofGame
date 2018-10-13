package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class PrisonbbCommand extends ACommand {

    public PrisonbbCommand() {
        super("prisonbb");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        if(pp.getLevel() < 15) {
            p.sendMessage(Utils.f("&7Чтобы заплатить выкуп вы должны достигнуть &915-ого &7уровня."));
            return false;
        }

        if (pp.takeMoney(10000)) {
            p.sendMessage(ChatColor.GREEN + "Теперь вы свободны! Используйте команду " + ChatColor.DARK_GREEN + "/city " + ChatColor.GREEN + " для того чтобы отправиться в город.");
            pp.setFree(true);
            return true;
        }

        p.sendMessage(ChatColor.GOLD + "Недостаточно денег для выкупа!");
        p.sendMessage(ChatColor.RED + "Вам необходимо еще " + ChatColor.DARK_RED + (10000 - pp.getBalance()) + "$");
        return false;
    }
}
