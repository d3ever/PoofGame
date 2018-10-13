package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.MenuUtils;

public class UpgradeCommand extends ACommand {

    public UpgradeCommand() {
        super("upgrade");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        int v;
        v = p.getInventory().getHeldItemSlot();
        if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
            if (p.getItemInHand().hasItemMeta() && p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().contains("Улучшение")) {
                p.sendMessage(ChatColor.RED + "Этот предмет не подлежит улучшению");
                return true;
            }

            if (PrisonItem.isCustomItem(p.getItemInHand())) {
                MenuUtils.showUpgradeMenu(p);
                p.getInventory().setHeldItemSlot(v);
                return true;
            }

            sender.sendMessage(ChatColor.YELLOW + "Этот предмет не подлежит улучшению");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Вы должны держать предмет в руках");
        return false;
    }
}
