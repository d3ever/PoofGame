package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.mobs.Spawner;
import sexy.criss.simple.prison.utils.ACommand;

public class ResetBossesCommand extends ACommand {

    public ResetBossesCommand() {
        super("resetBosses");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.resetbosses");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        Spawner.spawners.values().forEach(Spawner::reset);
        p.sendMessage(ChatColor.GRAY + "Все монстры были сброшены!");
        return false;
    }
}
