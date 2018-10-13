package sexy.criss.simple.prison.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class SetCommand extends ACommand {

    public SetCommand() {
        super("set");
        this.unavailableFromConsole();
        this.unavailableWithoutPermission("prison.commands.set");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        switch (args.length) {
            case 0:
                help(p);
                break;
            case 3:
                Player target = Bukkit.getPlayerExact(args[1]);
                if(target == null) {
                    p.sendMessage(Utils.f("&cЦель отсутствует на сервере."));
                    return true;
                }
                switch (args[0].toLowerCase()) {
                    case "level":
                        try {
                            int level = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setLevel(level);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "blocks":
                        try {
                            int blocks = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setTotalBlocks(blocks);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "exp":
                        try {
                            int exp = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setExp(exp);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "kills":
                        try {
                            int k = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setKills(k);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "deaths":
                        try {
                            int d = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setDeaths(d);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "money":
                        try {
                            int m = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setMoney(m);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "bm":
                        try {
                            int x = Integer.parseInt(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setBlocksMultiplier(x);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "mm":
                        try {
                            double x = Double.parseDouble(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setMoneyMultiplier(x);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                    case "km":
                        try {
                            double x = Double.parseDouble(args[2]);
                            PrisonPlayer t = PrisonPlayer.getPrisonPlayer(target);
                            t.setKeysMultiplier(x);
                            p.sendMessage(Utils.f("&7Успешно."));
                        } catch (Exception ex) {
                            p.sendMessage(Utils.f("&7Произошла ошибка при выполнении запроса."));
                            p.sendMessage(Utils.f("&4" + ex.getMessage()));
                        }
                        break;
                }
            default:
                help(p);
                break;
        }

        return false;
    }

    private void help(Player p) {
        p.sendMessage(Utils.f("&7Возможные вариации команды:"));
        p.sendMessage(Utils.f("&9/set level <player> <value> &7- Изменить уровень."));
        p.sendMessage(Utils.f("&9/set blocks <player> <value> &7- Изменить блоки."));
        p.sendMessage(Utils.f("&9/set exp <player> <value> &7- Изменить опыт."));
        p.sendMessage(Utils.f("&9/set kills <player> <value> &7- Изменить убийства."));
        p.sendMessage(Utils.f("&9/set deaths <player> <value> &7- Изменить смерти."));
        p.sendMessage(Utils.f("&9/set money <player> <value> &7- Изменить деньги."));
        p.sendMessage(Utils.f("&9/set bm <player> <value> &7- Изменить множитель блоков."));
        p.sendMessage(Utils.f("&9/set mm <player> <value> &7- Изменить множитель денег."));
        p.sendMessage(Utils.f("&9/set km <player> <value> &7- Изменить множитель ключей."));
    }

}
