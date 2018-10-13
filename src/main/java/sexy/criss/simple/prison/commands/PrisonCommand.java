package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class PrisonCommand extends ACommand {

    public PrisonCommand() {
        super("prison");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        String prefix = Utils.f("&7[&cRF&7] ");
        p.sendMessage(prefix + ChatColor.GRAY + "Доступные команды:");
        p.sendMessage(prefix + ChatColor.BLUE + "/mine " + ChatColor.GRAY + "- открыть меню шахт");
        p.sendMessage(prefix + ChatColor.BLUE + "/stats " + ChatColor.GRAY + "- открыть меню прокачки характеристик");
        p.sendMessage(prefix + ChatColor.BLUE + "/level " + ChatColor.GRAY + "- открыть меню прокачки уровня");
        p.sendMessage(prefix + ChatColor.BLUE + "/upgrade " + ChatColor.GRAY + "- открыть меню улучшения предмета");
        p.sendMessage(prefix + ChatColor.BLUE + "/faction " + ChatColor.GRAY + "- открыть меню выбора фракции");
        p.sendMessage(prefix + ChatColor.BLUE + "/base " + ChatColor.GRAY + "- телепортация на базу фракции");
        p.sendMessage(prefix + ChatColor.BLUE + "/fleave " + ChatColor.GRAY + "- покинуть фракцию за 1000$");
        p.sendMessage(prefix + ChatColor.BLUE + "/start " + ChatColor.GRAY + "- получить набор новичка");
        if (p.hasPermission("prison.admin")) {
            p.sendMessage(prefix + ChatColor.DARK_RED + "/addspawner " + ChatColor.GRAY + "- добавить точку спавна моба");
            p.sendMessage(prefix + ChatColor.DARK_RED + "/resetbosses " + ChatColor.GRAY + "- воскресить всех боссов");
            p.sendMessage(prefix + ChatColor.DARK_RED + "/base <faction> " + ChatColor.GRAY + "- телепортироваться на базу фракции <faction>");
            p.sendMessage(prefix + ChatColor.DARK_RED + "/setlockchest " + ChatColor.GRAY + "- установить запертый сундук");
            p.sendMessage(prefix + ChatColor.DARK_RED + "/addspawner " + ChatColor.GRAY + "- добавить точку спавна моба");
            p.sendMessage(prefix + ChatColor.DARK_RED + "/setbase <faction> " + ChatColor.GRAY + "- установить базу для фракции <faction>");
            p.sendMessage(prefix + ChatColor.DARK_RED + "/base <faction> " + ChatColor.GRAY + "- телепортироваться на базу фракции <faction>");
            p.sendMessage(prefix + ChatColor.DARK_RED + ChatColor.BOLD + "/debug " + ChatColor.GRAY + "- показать дебаговую информацию");
        }
        return false;
    }
}
