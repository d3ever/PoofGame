package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.Faction;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;

public class BaseCommand extends ACommand {

    public BaseCommand() {
        super("base");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        if (args.length > 0 && sender.hasPermission("prison.admin")) {
            if (Faction.isFaction(args[0])) {
                if (Faction.getFaction(args[0]).getHome() != null) {
                    p.teleport(Faction.getFaction(args[0]).getHome());
                    sender.sendMessage(ChatColor.GRAY + "Вы были телепортированы в точку фракции '" + Faction.getFaction(args[0]).getName() + ChatColor.GREEN + "'");
                    return true;
                }

                p.sendMessage(ChatColor.RED + "База этой фракции еще не установлена! Обратитесь к администратору.");
                return true;
            }

            sender.sendMessage(ChatColor.RED + "Фракция с id '" + args[0] + "' не найдена!");
            return true;
        } else if (pp.hasFaction()) {
            if (pp.getFaction().getHome() != null) {
                p.teleport(pp.getFaction().getHome());
                return true;
            }

            p.sendMessage(ChatColor.GRAY + "База вашей фракции еще не установлена! Обратитесь к администратору.");
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "Вы не принадлежите ни к одной фракции.");
        return false;
    }
}
