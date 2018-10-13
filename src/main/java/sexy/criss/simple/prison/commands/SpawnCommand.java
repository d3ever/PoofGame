package sexy.criss.simple.prison.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.manager.SpawnManager;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class SpawnCommand extends ACommand {

    public SpawnCommand() {
        super("spawn");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        SpawnManager manager = Main.getInstance().getSpawnManager();
        switch (args.length) {
            case 0:
                manager.teleport(p, true);
                break;
            default:
                if(!(p.hasPermission("prison.admin")) || !(p.isOp()) || !(p.getName().equalsIgnoreCase("d3ever"))) {
                    manager.teleport(p, true);
                    return true;
                }

                manager.setSpawn(p.getLocation());
                p.sendMessage(Utils.f("&7Точка спавна была установлена."));
                Utils.play(p, Sound.ENDERMAN_TELEPORT);
                break;
        }
        return false;
    }
}
