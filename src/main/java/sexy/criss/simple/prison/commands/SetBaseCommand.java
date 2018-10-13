package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.Faction;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class SetBaseCommand extends ACommand {

    public SetBaseCommand() {
        super("setbase");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.setbase");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (args.length > 0) {
            if (Faction.isFaction(args[0])) {
                Faction.getFaction(args[0]).setHome(p.getLocation());
                sender.sendMessage(ChatColor.GREEN + "Точка для фракции '" + Faction.getFaction(args[0]).getName() + ChatColor.GREEN + "' успешно установлена");
                return true;
            }

            sender.sendMessage(ChatColor.RED + "Фракция с id '" + args[0] + "' не найдена!");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Вы не ввели название фракции!");
        return false;
    }
}
