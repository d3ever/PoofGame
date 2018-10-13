package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class SetCityCommand extends ACommand {

    public SetCityCommand() {
        super("setcity");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.setcity");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);

        Main.CITY_LOCATION = p.getLocation();
        sender.sendMessage(ChatColor.AQUA + "Город установлен на вашей текущей позиции");
        Main.config.set("city.world", p.getLocation().getWorld().getName());
        Main.config.set("city.x", p.getLocation().getX());
        Main.config.set("city.y", p.getLocation().getY());
        Main.config.set("city.z", p.getLocation().getZ());
        Main.getInstance().saveConfig();
        return false;
    }
}
