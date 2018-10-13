package sexy.criss.simple.prison;

import java.util.*;
import java.util.logging.Level;

import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.utils.Utils;

public class PrisonMine {

   private static FileConfiguration config;
   private String boss;
   private String description;
   private String ingameName;
   private String permission;
   private String permMessage;
   private String technical;
   private Integer level;
   private Location location;
   private Material material;
   private PrisonMine.MineType type;
   public static List<PrisonMine> mines;

   static {
      config = Main.mines_storage;
      mines = new ArrayList();
   }

   public PrisonMine(String boss, String description, String ingameName, String name, Integer level, Location location, Material material, PrisonMine.MineType type, String permission, String permMessage) {
      this.boss = boss;
      this.description = description;
      this.ingameName = ingameName;
      this.technical = name;
      this.level = level;
      this.location = location;
      this.material = material;
      this.type = type;
      this.permission = permission;
      this.permMessage = permMessage;
   }

   public boolean hasBoss() {
      return this.boss != null;
   }

   public String getBoss() {
      return this.boss;
   }

   public String getDescription() {
      return this.description;
   }

   public String getIngame() {
      return this.ingameName;
   }

   public String getTechnical() {
      return this.technical;
   }

   public int getLevel() {
      return this.level;
   }

   public Location getLocation() {
      return this.location;
   }

   public Material getMaterial() {
      return this.material;
   }

   public PrisonMine.MineType getType() {
      return this.type;
   }

   public String getPermMessage() {
      return this.permMessage;
   }

   public String getPermission() {
      return this.permission;
   }

   public enum MineType {
      MINE("MINE", 0, "MINE", 0, "MINE", 0),
      LOCATION("LOCATION", 1, "LOCATION", 1, "LOCATION", 1);

      MineType(String a, int b, String c, int d, String e, int f) {
      }
   }

   public static class MineUtils {
      public MineUtils() {
      }

      public static boolean isRegistered(PrisonMine mine) {
         return PrisonMine.mines.contains(mine);
      }

      public static boolean isRegistered(String mine) {
         return PrisonMine.mines.contains(getMine(mine));
      }

      public static Map<PrisonMine, Boolean> availableMines(Player player) {
         Map<PrisonMine, Boolean> available = new HashMap();
         Iterator var3 = PrisonMine.mines.iterator();

         while(var3.hasNext()) {
            PrisonMine mine = (PrisonMine)var3.next();
            if (isAvailable(player, mine)) {
               available.put(mine, true);
            }
         }

         return available;
      }

      public static boolean isAvailable(Player player, String mine) {
         Iterator var3 = PrisonMine.mines.iterator();

         while(var3.hasNext()) {
            PrisonMine current = (PrisonMine)var3.next();
            if (current.getTechnical().equalsIgnoreCase(mine)) {
               PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(player);
               if (current.getType() == PrisonMine.MineType.MINE && current.getLevel() <= pp.getLevel()) {
                  return true;
               }

               if (current.getType() == PrisonMine.MineType.LOCATION && current.getTechnical().equals("vault")) {
                  return pp.hasCellarAccess();
               }
            }
         }

         return false;
      }

      public static boolean isAvailable(Player player, PrisonMine mine) {
         return isAvailable(player, mine.getTechnical());
      }

      public static PrisonMine getMine(String mine) {
         Iterator var2 = PrisonMine.mines.iterator();

         while(var2.hasNext()) {
            PrisonMine totalMines = (PrisonMine)var2.next();
            if (totalMines.getTechnical().equalsIgnoreCase(mine)) {
               return totalMines;
            }
         }

         return null;
      }

      public static void init() {
         Iterator var1 = PrisonMine.config.getConfigurationSection("mines").getKeys(false).iterator();

         while(var1.hasNext()) {
            String name = (String)var1.next();
            String boss = null;
            String description = null;
            String ingameName = null;
            Integer level = 0;
            Location location = null;
            Material material = null;
            PrisonMine.MineType type = PrisonMine.MineType.MINE;
            String permission = null;
            String permMessage = null;

            try {
               if (PrisonMine.config.contains("mines." + name + ".boss")) {
                  boss = PrisonMine.config.getString("mines." + name + ".boss");
               } else {
                  boss = "нет";
               }

               description = PrisonMine.config.getString("mines." + name + ".description").replace('&', '§');
               ingameName = PrisonMine.config.getString("mines." + name + ".ingameName").replace('&', '§');
               location = Utils.getLocation(PrisonMine.config.getString("mines." + name + ".location"));
               if (PrisonMine.config.contains("mines." + name + ".permission")) {
                  permission = PrisonMine.config.getString("mines." + name + ".permission");
                  permMessage = PrisonMine.config.getString("mines." + name + ".permMessage");
               }
            } catch (NullPointerException var17) {
               Main.getInstance().getLogger().log(Level.WARNING, "Не удалось найти описание или внутриигровое название шахты!", var17);
               continue;
            }

            try {
               if (PrisonMine.config.contains("mines." + name + ".level")) {
                  level = PrisonMine.config.getInt("mines." + name + ".level");
               } else {
                  level = 1;
                  type = PrisonMine.MineType.LOCATION;
               }

               material = Material.getMaterial(PrisonMine.config.getString("mines." + name + ".material"));
            } catch (NumberFormatException | NullPointerException ex) {
               Main.getInstance().getLogger().log(Level.WARNING, "Не удалось найти материал шахты \"" + ingameName + "\"!", ex);
               continue;
            }

            PrisonMine.mines.add(new PrisonMine(boss, description, ingameName, name, level, location, material, type, permission, permMessage));
         }

         Main.getInstance().getLogger().log(Level.INFO, "Было загружено " + PrisonMine.mines.size() + " шахт, наборов.");
      }
   }

}
