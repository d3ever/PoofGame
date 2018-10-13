package sexy.criss.simple.prison.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.mobs.EntityTypes;
import sexy.criss.simple.prison.mobs.Spawner;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

import static sexy.criss.simple.prison.Main.saveConfig;
import static sexy.criss.simple.prison.Main.spawners_storage;

public class AddSpawnerCommand extends ACommand {

    public AddSpawnerCommand() {
        super("addSpawner");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.spawner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if(!(p.hasPermission("prison.admin")) || !(p.isOp())) {
            p.sendMessage(Utils.f("&7Извините, у вас недостаточно прав, чтобы использовать это."));
            return true;
        }

        switch (args.length) {
            case 0:
                sender.sendMessage(ChatColor.GRAY + "Вы не ввели имя точки спавна.");
                break;
            case 1:
                sender.sendMessage(ChatColor.GRAY + "Вы не ввели тип точки спавна.");
                Main.getInstance().sendAllTypes(sender);
                break;
            case 2:
                sender.sendMessage(ChatColor.GRAY + "Вы не ввели интервал.");
                break;
            default:
                EntityTypes type;
                try {
                    type = EntityTypes.valueOf(args[1]);
                } catch (Exception ex) {
                    p.sendMessage(ChatColor.GRAY + "Неизвестный тип!");
                    Main.getInstance().sendAllTypes(p);
                    return true;
                }

                if (Main.getInstance().tryParseInt(args[2])) {
                    int interval = Integer.parseInt(args[2]);
                    String path = args[0].toLowerCase();
                    Location loc = p.getLocation();
                    spawners_storage.set(path + ".type", String.valueOf(type));
                    spawners_storage.set(path + ".interval", interval);
                    spawners_storage.set(path + ".x", loc.getX());
                    spawners_storage.set(path + ".y", loc.getY());
                    spawners_storage.set(path + ".z", loc.getZ());
                    spawners_storage.set(path + ".world", loc.getWorld().getName());
                    saveConfig(spawners_storage, "spawners.yml");
                    Spawner spawner = new Spawner(loc, type, interval);
                    spawner.update();
                    p.sendMessage(ChatColor.GRAY + "Точка спавна успешно добавлена.");
                    return true;
                }

                p.sendMessage(ChatColor.GRAY + "Интервал введен неверно!");
                break;
        }
        return false;
    }
}
