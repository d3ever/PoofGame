package sexy.criss.simple.prison;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Faction {

   public static Map<String, Faction> factions = new HashMap();
   String id;
   String name;
   String prefix;
   String color;
   Material icon;
   Location home;


   public Faction(String id, String name, String prefix, Material icon, Location home) {
      this.id = id;
      this.name = name;
      this.prefix = prefix;
      this.icon = icon;
      this.home = home;
      factions.put(id, this);
   }

   public Faction(String id, ConfigurationSection cFaction) {
      this.id = id;
      this.name = Utils.f(cFaction.getString("name"));
      this.prefix = Utils.f(cFaction.getString("prefix"));
      this.color = Utils.f(cFaction.getString("color"));
      this.icon = Material.getMaterial(cFaction.getString("icon"));
      if(cFaction.contains("location")) {
         ConfigurationSection l = cFaction.getConfigurationSection("location");
         this.home = new Location(Bukkit.getWorld(l.getString("world")), (double)l.getInt("x"), (double)l.getInt("y"), (double)l.getInt("z"));
      }

      factions.put(id, this);
   }

   public ItemStack getFactionIcon() {
      ItemStack icon = new ItemStack(this.icon);
      ItemMeta iconMeta = icon.getItemMeta();
      iconMeta.setDisplayName(ChatColor.GOLD + "Выбрать фракцию «" + this.name + ChatColor.GOLD + "»");
      iconMeta.setLore(Collections.singletonList(ChatColor.BLACK + this.id));
      icon.setItemMeta(iconMeta);
      return icon;
   }

   public static Faction getFaction(String id) {
      return id != null && factions.containsKey(id) ? factions.get(id) : null;
   }

   public static boolean isFaction(ItemStack icon) {
      try {
         return factions.containsKey(ChatColor.stripColor(icon.getItemMeta().getLore().get(0)));
      } catch (Exception var2) {
         System.out.println("Something wrong. Not a faction check!");
         return false;
      }
   }

   public static boolean isFaction(String id) {
      return factions.containsKey(id);
   }

   public static Faction getFaction(ItemStack is) {
      try {
         return factions.get(ChatColor.stripColor(is.getItemMeta().getLore().get(0)));
      } catch (Exception var2) {
         System.out.println("Something wrong. Not a faction get!");
         return null;
      }
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public Material getIcon() {
      return this.icon;
   }

   public String toString() {
      return "Faction [id=" + this.id + ", name=" + this.name + ", prefix=" + this.prefix + ", icon=" + this.icon + "]";
   }

   public Location getHome() {
      return this.home;
   }

   public void setHome(Location home) {
      ConfigurationSection faction = Main.factions_storage.getConfigurationSection(this.id);
      ConfigurationSection loc;
      if(faction.contains("location")) {
         loc = faction.getConfigurationSection("location");
      } else {
         loc = faction.createSection("location");
      }

      loc.set("x", (int) home.getX());
      loc.set("y", (int) home.getY());
      loc.set("z", (int) home.getZ());
      loc.set("world", home.getWorld().getName());
      Main.factions_storage.getConfigurationSection(this.id).set("location", loc);
      this.home = home;
      Main.saveConfig(Main.factions_storage, "factions.yml");
   }

   public String getColor() {
      return this.color;
   }
}
