package sexy.criss.simple.prison.manager.builders;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Stack;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Set;

public class Confirm extends SexyEvent {
    private String name = Utils.f("&aПодтвердите действие.");
    private ItemStack accept = new Stack(Material.STAINED_CLAY).durability((short) 5);
    private ItemStack denny = new Stack(Material.STAINED_CLAY).durability((short) 14);
    private boolean value = false;
    private boolean complete;
    private Set<Player> set = Sets.newHashSet();

    public Confirm(Player p) {
        set.add(p);
        execInventory(p);
    }

    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(!(set.contains(p))) return;
        execHandler(e);
    }

    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(!(set.contains(p))) return;
        execHandler(e);
    }

    private void execHandler(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory i = e.getClickedInventory();
        ItemStack cur = e.getCurrentItem();
        if(i == null) return;
        if(!(i.getName().equals(name))) return;

        this.value = (cur != null) && (cur.equals(this.accept));
        this.complete = true;
        p.closeInventory();
        Utils.play(p, Sound.CAT_MEOW);
    }

    private void execHandler(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if(inv == null) return;
        if(!(inv.getName().equals(name))) return;
        if(this.value) return;

        this.complete = true;
        this.value = false;
    }

    private void execInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, name);
        ItemStack background = new Stack(Material.STAINED_CLAY).durability((short) 15);

        inv.setItem(1, this.accept);
        inv.setItem(3, this.denny);
        for(int i = 0; i < inv.getSize(); i++) if(inv.getItem(i) == null) inv.setItem(i, background);

        p.openInventory(inv);
    }

}
