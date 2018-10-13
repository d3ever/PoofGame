package sexy.criss.simple.prison;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.istack.internal.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.mobs.EntityTypes;
import sexy.criss.simple.prison.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PrisonItem {

   public static Map<String, PrisonItem> items = new HashMap<>();
   private String id;
   private String next;
   private String name;
   private Material material;
   private List<String> lore;
   private Map enchantments;
   private boolean drop = false;
   private short data = 0;
   private int price;
   private HashMap requirements;
   private HashMap mob_requirements;


   PrisonItem(String id, ConfigurationSection cItem) {
      this.id = id;
      this.material = Material.valueOf(cItem.getString("material"));
      this.name = Utils.f(cItem.getString("name"));
      this.enchantments = new HashMap();
      this.requirements = new HashMap();
      this.mob_requirements = Maps.newHashMap();

      if(cItem.contains("lore")) this.lore = Utils.f(cItem.getStringList("lore"));
      if(this.lore == null) this.lore = Lists.newArrayList();


      this.lore.add(0, ChatColor.BLACK + id);
      Entry cReq;
      Iterator var4;
      if(cItem.contains("enchantments")) {
         var4 = cItem.getConfigurationSection("enchantments").getValues(false).entrySet().iterator();

         while(var4.hasNext()) {
            cReq = (Entry)var4.next();
            this.enchantments.put(Enchantment.getByName((String)cReq.getKey()), cReq.getValue());
         }
      }

      if(cItem.contains("requirements")) {
         var4 = cItem.getConfigurationSection("requirements").getValues(false).entrySet().iterator();

         while(var4.hasNext()) {
            cReq = (Entry)var4.next();
            this.requirements.put(Material.getMaterial((String)cReq.getKey()), cReq.getValue());
         }
      }

      if(cItem.contains("mob-requirements")) {
         var4 = cItem.getConfigurationSection("mob-requirements").getValues(false).entrySet().iterator();

         while(var4.hasNext()) {
            cReq = (Entry)var4.next();
            this.mob_requirements.put(EntityTypes.valueOf((String)cReq.getKey()), cReq.getValue());
         }
      }

      if(cItem.contains("price")) this.price = cItem.getInt("price");
      if(cItem.contains("next")) this.next = cItem.getString("next");
      if(cItem.contains("drop")) this.drop = cItem.getBoolean("drop");
      if(cItem.contains("data")) this.data = (short)cItem.getInt("data");

      items.put(id, this);
   }

   @NotNull
   public ItemStack getUsableItem() {
      ItemStack is = new ItemStack(this.material);
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(ChatColor.RESET + this.name);
      im.setLore(this.lore);
      if(this.enchantments.size() > 0) {
         for (Object o : this.enchantments.entrySet()) {
            Entry entry = (Entry) o;
            im.addEnchant((Enchantment) entry.getKey(), (Integer) entry.getValue(), false);
         }
      }

      if(is.getType().getMaxDurability() > 16) im.spigot().setUnbreakable(true);

      is.setItemMeta(im);
      is.setDurability(this.data);
      return is;
   }

   public ItemStack getUpgradingItem(PrisonPlayer prisonPlayer) {
      PrisonItem nextItem = getPrisonItem(this.next);
      ItemStack is = new ItemStack(nextItem.material);
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(ChatColor.GOLD + "Улучшение: " + nextItem.name);
      List<String> lore = Lists.newArrayList(nextItem.lore);
      lore.add(ChatColor.GREEN + "Цена: " + ChatColor.DARK_GREEN + nextItem.price + "$");
      if(nextItem.requirements.size() > 0 || nextItem.mob_requirements.size() > 0) {
         lore.add(ChatColor.DARK_GREEN + "Необходимо: ");

         if(nextItem.requirements.size() > 0) lore.add(ChatColor.GOLD + "Блоки: ");

         boolean allowed = true;
         boolean good;
         Iterator var9 = nextItem.requirements.entrySet().iterator();

         Entry entry;
         ChatColor color;
         while(var9.hasNext()) {
            entry = (Entry)var9.next();
            good = prisonPlayer.getBlockDigsCount((Material)entry.getKey()) >= (Integer) entry.getValue();
            color = good?ChatColor.DARK_GREEN:ChatColor.DARK_RED;
            lore.add(ChatColor.GOLD + entry.getKey().toString() + ": " + color + prisonPlayer.getBlockDigsCount((Material)entry.getKey()) + " / " + entry.getValue());
            if(!good) allowed = false;

         }

         if(nextItem.mob_requirements.size() > 0) lore.add(ChatColor.GOLD + "Мобы: ");

         var9 = nextItem.mob_requirements.entrySet().iterator();

         while(var9.hasNext()) {
            entry = (Entry)var9.next();
            good = prisonPlayer.getMobKillCount((EntityTypes)entry.getKey()) >= ((Integer)entry.getValue()).intValue();
            color = good?ChatColor.DARK_GREEN:ChatColor.DARK_RED;
            lore.add(ChatColor.GOLD + ((EntityTypes)entry.getKey()).getName() + ": " + color + prisonPlayer.getMobKillCount((EntityTypes)entry.getKey()) + " / " + entry.getValue());
            if(!good) allowed = false;
         }

         if(nextItem.enchantments.size() > 0) {
            var9 = nextItem.enchantments.entrySet().iterator();

            while(var9.hasNext()) {
               entry = (Entry)var9.next();
               im.addEnchant((Enchantment)entry.getKey(), (Integer) entry.getValue(), false);
            }
         }

         if(!allowed) lore.set(0, ChatColor.BLACK + "not_allowed");
      }

      im.setLore(lore);
      is.setDurability(this.data);

      if(is.getType().getMaxDurability() > 16) im.spigot().setUnbreakable(true);

      is.setItemMeta(im);
      return is;
   }

   public ItemStack getMaxLevelItem() {
      ItemStack is = this.getUsableItem();
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(ChatColor.RESET + this.name);
      this.lore.set(0, ChatColor.BLACK + "level_max");
      this.lore.add("");
      this.lore.add(ChatColor.YELLOW + "Ваш предмет максимального уровня\t");
      im.setLore(this.lore);

      if(is.getType().getMaxDurability() > 16) im.spigot().setUnbreakable(true);

      is.setItemMeta(im);
      return is;
   }

   public PrisonItem getNext() {
      return items.getOrDefault(this.next, null);
   }

   public boolean hasNext() {
      return this.getNext() != null;
   }

   @NotNull
   public static PrisonItem getPrisonItem(@NotNull ItemStack is) {
      return items.get(ChatColor.stripColor(is.getItemMeta().getLore().get(0)));
   }

   @NotNull
   public static PrisonItem getPrisonItem(@NotNull String id) {
      return items.get(id);
   }

   public static boolean isCustomItem(ItemStack is) {
      if(is == null) return false;
      if(!(is.hasItemMeta()) || !(is.getItemMeta().hasLore())) return false;

      try {
         return items.containsKey(ChatColor.stripColor(is.getItemMeta().getLore().get(0)));
      } catch (Exception ex) {
         return false;
      }
   }

   public static boolean isAvailable(String id) {
      return items.containsKey(id);
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public Material getMaterial() {
      return this.material;
   }

   public List<String> getLore() {
      return this.lore;
   }

   public Map getEnchantments() {
      return this.enchantments;
   }

   public boolean willDrop() {
      return this.drop;
   }

   public short getData() {
      return this.data;
   }

   public int getPrice() {
      return this.price;
   }

   public HashMap getRequirements() {
      return this.requirements;
   }

   public HashMap getMob_requirements() {
      return this.mob_requirements;
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(!(o instanceof PrisonItem)) {
         return false;
      } else {
         PrisonItem that = (PrisonItem)o;
         return this.id.equals(that.id);
      }
   }

   public int hashCode() {
      return this.id.hashCode();
   }
}
