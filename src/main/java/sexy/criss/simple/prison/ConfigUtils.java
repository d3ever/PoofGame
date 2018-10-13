package sexy.criss.simple.prison;

import java.io.File;
import java.io.InputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtils {

   public static FileConfiguration loadConfig(String fileName) {
      File file = new File(Main.getInstance().getDataFolder(), fileName);
      YamlConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
      InputStream defConfigStream = Main.getInstance().getResource(fileName);
      if(defConfigStream != null) {
         YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
         fileConfiguration.setDefaults(defConfig);
      }

      return fileConfiguration;
   }

   public static FileConfiguration loadConfig(File file) {
      return YamlConfiguration.loadConfiguration(file);
   }

   public static void saveDefaultConfig(String fileName) {
      File file = new File(Main.getInstance().getDataFolder(), fileName);
      if(!file.exists()) {
         Main.getInstance().saveResource(fileName, false);
      }

   }
}
