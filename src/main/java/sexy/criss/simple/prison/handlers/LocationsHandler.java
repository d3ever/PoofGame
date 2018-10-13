package sexy.criss.simple.prison.handlers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.SexyEvent;

import java.lang.reflect.InvocationTargetException;

public class LocationsHandler extends SexyEvent {

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = false
    )
    public void onBlockBreak(BlockBreakEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Player p = e.getPlayer();
        Material m = e.getBlock().getType();
        if (!e.isCancelled() && m == Material.MYCEL && p.getInventory().getItemInHand().getType().equals(Material.DIAMOND_SPADE) && p.getInventory().getItemInHand().getEnchantments().containsKey(Enchantment.DIG_SPEED) && (Integer)p.getInventory().getItemInHand().getEnchantments().get(Enchantment.DIG_SPEED) == 4) {
            p.sendMessage("§bВы не можете копать этот блок данной лопатой!");
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onLocationAccess(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory().getItemInHand() == null) return;
        if(PrisonItem.getPrisonItem(p.getItemInHand()) == null) return;
        ItemStack it = p.getInventory().getItemInHand();
        PrisonPlayer pp;
        if (it.equals(PrisonItem.getPrisonItem("vault").getUsableItem())) {
            e.setCancelled(true);
            pp = PrisonPlayer.getPrisonPlayer(p);
            if (pp.hasCellarAccess()) {
                p.sendMessage("§bУ вас уже есть доступ к локации §a\"Таинственный подвал\"§b.");
                return;
            }

            if (it.getAmount() > 1) {
                it.setAmount(p.getInventory().getItemInHand().getAmount() - 1);
            } else {
                p.getInventory().setItemInHand(new ItemStack(Material.AIR));
            }

            pp.grantCellarAccess();
            p.sendMessage("§bВы получили доступ к локации §a\"Таинственный подвал\"§b.");
        } else if (it.equals(PrisonItem.getPrisonItem("mushroom").getUsableItem())) {
            e.setCancelled(true);
            pp = PrisonPlayer.getPrisonPlayer(p);
            if (pp.hasMushroomAccess()) {
                p.sendMessage("§bУ вас уже есть доступ к локации §a\"Грибной мир\"§b.");
                return;
            }

            if (it.getAmount() > 1) {
                it.setAmount(p.getInventory().getItemInHand().getAmount() - 1);
            } else {
                p.getInventory().setItemInHand(new ItemStack(Material.AIR));
            }

            pp.grantMushroomAccess();
            p.sendMessage("§bВы получили доступ к локации §a\"Грибной мир\"§b.");
        } else if (it.equals(PrisonItem.getPrisonItem("sektor").getUsableItem())) {
            e.setCancelled(true);
            pp = PrisonPlayer.getPrisonPlayer(p);
            if (pp.hasSectorAccess()) {
                p.sendMessage("§bУ вас уже есть доступ к локации §a\"Засекреченный отсек\"§b.");
                return;
            }

            if (it.getAmount() > 1) {
                it.setAmount(p.getInventory().getItemInHand().getAmount() - 1);
            } else {
                p.getInventory().setItemInHand(new ItemStack(Material.AIR));
            }

            pp.grantSectorAccess();
            p.sendMessage("§bВы получили доступ к локации §a\"Засекреченный отсек\"§b.");
        } else if (it.equals(PrisonItem.getPrisonItem("magic").getUsableItem())) {
            e.setCancelled(true);
            pp = PrisonPlayer.getPrisonPlayer(p);
            if (pp.hasMagicAccess()) {
                p.sendMessage("§bУ вас уже есть доступ к локации §a\"Магический остров\"§b.");
                return;
            }

            if (it.getAmount() > 1) {
                it.setAmount(p.getInventory().getItemInHand().getAmount() - 1);
            } else {
                p.getInventory().setItemInHand(new ItemStack(Material.AIR));
            }

            pp.grantMagicAccess();
            p.sendMessage("§bВы получили доступ к локации §a\"Магический остров\"§b.");
        }

    }
}
