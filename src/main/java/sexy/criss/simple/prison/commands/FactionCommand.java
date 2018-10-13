package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.MenuUtils;

public class FactionCommand extends ACommand {

    public FactionCommand() {
        super("faction");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);

        if (pp.getLevel() >= 5) {
            if (!pp.hasFaction()) {
                MenuUtils.showFactionMenu(p);
                return true;
            }

            sender.sendMessage(ChatColor.GOLD + "Вы уже сделали свой выбор");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Выбор фракции доступен после пятого уровня.");
        return false;
    }
}
