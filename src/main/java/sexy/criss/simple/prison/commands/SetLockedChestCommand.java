package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

import static sexy.criss.simple.prison.Main.chest_setters;

public class SetLockedChestCommand extends ACommand {

    public SetLockedChestCommand() {
        super("setLockedChest", "setLockChest", "setChest");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.setlockedchest");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        chest_setters.add(p);
        sender.sendMessage(ChatColor.GRAY + "Нажмите ЛКМ по сундуку, чтобы выставить локацию.");
        return false;
    }
}
