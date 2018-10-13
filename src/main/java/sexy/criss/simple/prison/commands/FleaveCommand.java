package sexy.criss.simple.prison.commands;

import com.sun.istack.internal.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Utils;

public class FleaveCommand extends ACommand {

    public FleaveCommand() {
        super("fleave", "factionleave");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        removeFaction(p);
        return false;
    }

    public static void removeFaction(@NotNull Player p) {
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        if(!(pp.hasFaction())) {
            p.sendMessage(Utils.f("&7Вы не состоите во фракции. &9/faction&7 - для выбора."));
            return;
        }

        if(!(pp.hasMoney(1000))) {
            p.sendMessage(Utils.f("&7У вас недостаточно средств. Стоит накопить ещё " + (1000 - pp.getBalance())));
            return;
        }
        p.sendMessage(Utils.f("&7Фракция была убрана. С вашего счёта была взята плата в размере &91000$&7."));
        pp.setMoney(pp.getBalance() - 1000);
        pp.setFaction(null);
    }
}
