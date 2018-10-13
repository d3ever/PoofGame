package sexy.criss.simple.prison.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.manager.Reference;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class GamemodeCommand extends ACommand {

    public GamemodeCommand() {
        super("gamemode", "gm");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.gamemode");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        switch (args.length) {
            case 0:
                help(p, label);
                break;
            case 1:
                String type = args[0];
                GameMode mode;
                if(StringUtils.isNumeric(type)) {
                    if(GameMode.getByValue(Integer.parseInt(type)) == null) {
                        p.sendMessage(Utils.f("&7Извините, режим не был найден."));
                        AtomicReference<String> str = new AtomicReference<>("&9Возможные типы: ");
                        Stream.of(GameMode.values()).forEach(m -> str.set(str.get() + "&c" + m.toString() + "&7, "));
                        p.sendMessage(Utils.f(str.get().substring(0, str.get().length() - 2).concat(".")));
                        return true;
                    }

                    mode = GameMode.getByValue(Integer.parseInt(type));
                } else {
                    try {
                        mode = GameMode.valueOf(type);
                    }catch (EnumConstantNotPresentException ex) {
                        p.sendMessage(Utils.f("&7Извините, режим не был найден."));
                        AtomicReference<String> str = new AtomicReference<>("&9Возможные типы: ");
                        Stream.of(GameMode.values()).forEach(m -> str.set(str.get() + Utils.f("&c" + m.toString() + "&7, ")));
                        p.sendMessage(Utils.f(str.get().substring(0, str.get().length() - 2).concat(".")));
                        return true;
                    }
                }

                if(mode != null) {
                    p.setGameMode(mode);
                    p.sendMessage(Utils.f("&7Игровой режим был изменён на &c" + mode.name() + "&7."));
                }
                break;
        }
        return false;
    }

    private void help(Player p, String label) {
        p.sendMessage(Reference.USAGE_SYNTAX.get(label + " <mode> &7- Изменить игровой режим"));
    }

}
