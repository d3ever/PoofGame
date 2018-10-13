package sexy.criss.simple.prison;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class Tempfix extends BukkitRunnable {

   public void run() {
      Iterator var2 = Bukkit.getServer().getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         PlayerInventory inv = player.getInventory();
         ItemStack[] arrayOfItemStack;
         int m = (arrayOfItemStack = inv.getContents()).length;

         ItemStack chest;
         for(int helmet = 0; helmet < m; ++helmet) {
            chest = arrayOfItemStack[helmet];
            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Улучшение")) {
               inv.remove(chest);
            }

            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Урон")) {
               inv.remove(chest);
            }

            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Ловкость")) {
               inv.remove(chest);
            }

            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Потребности")) {
               inv.remove(chest);
            }

            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Удача")) {
               inv.remove(chest);
            }

            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Информация")) {
               inv.remove(chest);
            }

            if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Поднять уровень")) {
               inv.remove(chest);
            }
         }

         ItemStack var10 = player.getInventory().getHelmet();
         if(var10 != null && var10.hasItemMeta() && var10.getItemMeta().hasDisplayName() && var10.getItemMeta().getDisplayName().contains("Улучшение")) {
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
         }

         chest = player.getInventory().getChestplate();
         if(chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName() && chest.getItemMeta().getDisplayName().contains("Улучшение")) {
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
         }

         ItemStack legg = player.getInventory().getLeggings();
         if(legg != null && legg.hasItemMeta() && legg.getItemMeta().hasDisplayName() && legg.getItemMeta().getDisplayName().contains("Улучшение")) {
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
         }

         ItemStack boots = player.getInventory().getBoots();
         if(boots != null && boots.hasItemMeta() && boots.getItemMeta().hasDisplayName() && boots.getItemMeta().getDisplayName().contains("Улучшение")) {
            player.getInventory().setBoots(new ItemStack(Material.AIR));
         }

         if(boots != null && boots.hasItemMeta() && boots.getItemMeta().hasDisplayName() && boots.getItemMeta().getDisplayName().contains("Ловкость")) {
            player.getInventory().setBoots(new ItemStack(Material.AIR));
         }
      }

   }
}
