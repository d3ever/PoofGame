package sexy.criss.simple.prison.handlers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonMine;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Iterator;

public class MinesHandler extends SexyEvent {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getCurrentItem() == null) return;
        if (Utils.strip(e.getClickedInventory().getName()).equals(Utils.strip(Main.MINES_MENU_NAME))) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            Player p = (Player)e.getWhoClicked();
            PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
            if (i.getType() == Material.EMERALD) {
                p.openInventory(Main.donate_mines.getMinesMenu());
            } else {
                Iterator var6 = PrisonMine.mines.iterator();

                while(var6.hasNext()) {
                    PrisonMine mine = (PrisonMine)var6.next();
                    String clickedMineIngame = ChatColor.stripColor(i.getItemMeta().getDisplayName());
                    if (clickedMineIngame.equalsIgnoreCase(ChatColor.stripColor(mine.getIngame()))) {
                        if (mine.getPermission() != null && !p.hasPermission(mine.getPermission())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', mine.getPermMessage()));
                            return;
                        }

                        if (mine.getType() == PrisonMine.MineType.LOCATION && mine.getTechnical().equalsIgnoreCase("vault") && !pp.hasCellarAccess()) {
                            p.sendMessage("§bУ вас нет доступа к локации §a\"" + mine.getIngame() + "\"");
                            return;
                        }

                        if (mine.getType() == PrisonMine.MineType.LOCATION && mine.getTechnical().equalsIgnoreCase("mushroom") && !pp.hasMushroomAccess()) {
                            p.sendMessage("§bУ вас нет доступа к локации §a\"" + mine.getIngame() + "\"");
                            return;
                        }

                        if (mine.getType() == PrisonMine.MineType.LOCATION && mine.getTechnical().equalsIgnoreCase("Sektor") && !pp.hasSectorAccess()) {
                            p.sendMessage("§bУ вас нет доступа к локации §a\"" + mine.getIngame() + "\"");
                            return;
                        }

                        if (mine.getType() == PrisonMine.MineType.LOCATION && mine.getTechnical().equalsIgnoreCase("magic") && !pp.hasMagicAccess()) {
                            p.sendMessage("§bУ вас нет доступа к локации §a\"" + mine.getIngame() + "\"");
                            return;
                        }

                        if (pp.getLevel() < mine.getLevel()) {
                            p.sendMessage("§bУ вас слишком маленький уровень!");
                            return;
                        }

                        try {
                            p.teleport(mine.getLocation());
                        } catch (NullPointerException var9) {
                            p.sendMessage("§bПроизошла ошибка при телепортации! Пожалуйста, обратитесь к администрации!");
                        }

                        p.sendMessage("§bВы были телепортированы на шахту §a\"" + clickedMineIngame + "\"");
                    }
                }
            }
        }
    }

}
