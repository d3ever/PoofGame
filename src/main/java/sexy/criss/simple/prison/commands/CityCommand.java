package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;

import static sexy.criss.simple.prison.Main.CITY_LOCATION;

public class CityCommand extends ACommand {

    public CityCommand() {
        super("city");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (CITY_LOCATION != null)
            if (PrisonPlayer.getPrisonPlayer(p).isFree()) p.teleport(CITY_LOCATION);
            else sender.sendMessage(ChatColor.RED + "Вы не свободны! Чтобы выкупиться из тюрьмы - используйте команду " + ChatColor.DARK_RED + "/prisonbb");
        else sender.sendMessage(ChatColor.RED + "Город не задан! Обратитесь к администратору.");

        return false;
    }
}
