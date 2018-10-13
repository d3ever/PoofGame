package sexy.criss.simple.prison.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.manager.ShopManager;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.MenuUtils;
import sexy.criss.simple.prison.utils.Utils;

public class ShopCommand extends ACommand {

    public ShopCommand() {
        super("shop");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        toPlayer(p);
        return false;
    }

    public void toPlayer(Player p) {
        MenuUtils.showShopMenu(p);
    }

    public static void handle(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        if(e.getClickedInventory() == null) return;
        if(e.getCurrentItem() == null) return;
        if(!e.getClickedInventory().getName().equals(ShopManager.display)) return;
        ItemStack cur = e.getCurrentItem();
        String id = Utils.strip(cur.getItemMeta().getLore().get(0));
        for(String s : Main.shop_storage.getStringList("shop.items")) {
            if(s.contains(id)) {
                String[] ar = s.split(", ");
                if(pp.hasMoney(Integer.parseInt(ar[3]))) {
                    pp.takeMoney(Integer.parseInt(ar[3]));
                    ItemStack stack = PrisonItem.getPrisonItem(id).getUsableItem();
                    stack.setAmount(Integer.parseInt(ar[2]));

                    p.getInventory().addItem(stack);
                    MenuUtils.showShopMenu(p);
                    return;
                }
                p.sendMessage(Utils.f("§bНа вашем счету недостаточно денег! §a(" + (Integer.parseInt(ar[3]) - pp.getBalance()) + "$)"));
                e.setCancelled(true);
                Utils.play(p, Sound.ANVIL_BREAK);
                return;
            }
        }
    }

}
