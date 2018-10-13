package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import sexy.criss.simple.prison.utils.ACommand;

import java.util.Arrays;

public class DebugCommand extends ACommand {

    public DebugCommand() {
        super("debug");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.debug");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        p.sendMessage(ChatColor.DARK_PURPLE + "Searching...");

        p.sendMessage(ChatColor.RED + " - Player Death Event:");
        Arrays.asList(PlayerDeathEvent.getHandlerList().getRegisteredListeners()).forEach(l -> p.sendMessage(ChatColor.DARK_AQUA + "  -" + l.getPlugin().getName()));

        p.sendMessage(ChatColor.RED + " - Entity Damage Event:");
        Arrays.asList(EntityDamageEvent.getHandlerList().getRegisteredListeners()).forEach(l -> p.sendMessage(ChatColor.DARK_AQUA + "  -" + l.getPlugin().getName()));

        p.sendMessage(ChatColor.RED + " - Entity Damage By Entity Event:");
        Arrays.asList(EntityDamageByEntityEvent.getHandlerList().getRegisteredListeners()).forEach(l -> p.sendMessage(ChatColor.DARK_AQUA + "  -" + l.getPlugin().getName()));
        p.sendMessage(ChatColor.DARK_PURPLE + "end");
        return false;
    }
}
