package sexy.criss.simple.prison.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.manager.DonateMines;
import sexy.criss.simple.prison.utils.Mine;
import sexy.criss.simple.prison.utils.SexyEvent;

import java.util.Iterator;

public class DonateMinesHandler extends SexyEvent {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals(Main.donate_mines.getMinesMenu().getName())) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            if (i != null && i.getType() != null) {
                Player p = (Player)e.getWhoClicked();
                PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
                Iterator var6 = DonateMines.mineSet.iterator();

                while(var6.hasNext()) {
                    Mine mine = (Mine)var6.next();
                    ItemStack cur = e.getCurrentItem();
                    ItemStack mm = mine.getIcon();
                    if (cur.equals(mm)) {
                        if (!mine.hasAccess(p)) {
                            p.sendMessage("§bВаша привилегия слишком низкая!");
                            return;
                        }

                        try {
                            p.teleport(mine.getLocation());
                        } catch (NullPointerException ex) {
                            p.sendMessage("§bПроизошла ошибка при телепортации! Пожалуйста, обратитесь к администрации!");
                        }

                        p.sendMessage("§bВы были телепортированы на шахту §a\"" + cur.getItemMeta().getDisplayName() + "§a\"");
                    }
                }

            }
        }
    }

}
