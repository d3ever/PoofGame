package sexy.criss.simple.prison.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sexy.criss.simple.prison.utils.Mine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DonateMines {
    private JavaPlugin plugin;
    private File file;
    private Inventory menu;
    public static Set<Mine> mineSet = new HashSet();

    public DonateMines(JavaPlugin plugin) throws Exception {
        this.plugin = plugin;
        this.reloadConfiguration0();
    }

    private void reloadConfiguration0() throws Exception {
        this.shaftFileNotNull();
        this.initMines();
        this.initMenu();
    }

    public Inventory getMinesMenu() {
        return this.menu;
    }

    private boolean shaftFileNotNull() {
        this.file = new File(this.plugin.getDataFolder(), "donate_shaft.json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                return false;
            } catch (IOException var2) {
                return false;
            }
        } else {
            return true;
        }
    }

    private void initMenu() throws Exception {
        JSONParser parser = new JSONParser();
        Object parsed = parser.parse(new FileReader(this.file));
        JSONObject sec = (JSONObject)parsed;
        JSONObject menu = (JSONObject)sec.get("menu");
        int size = 9 * Integer.parseInt(String.valueOf(menu.get("size")));
        String title = (String)menu.get("title");
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));
        Iterator var9 = mineSet.iterator();

        while(var9.hasNext()) {
            Mine m = (Mine)var9.next();
            inv.setItem(m.getSlot(), m.getIcon());
        }

        this.menu = inv;
    }

    private void initMines() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object parsed = parser.parse(new FileReader(this.file));
        JSONObject sec = (JSONObject)parsed;
        JSONObject mines = (JSONObject)sec.get("mines");
        Iterator var6 = mines.keySet().iterator();

        while(var6.hasNext()) {
            Object key = var6.next();
            System.out.println(key.toString());
            JSONObject mine = (JSONObject)mines.get(key);
            JSONObject loc = (JSONObject)mine.get("location");
            String id = key.toString();
            Material material = Material.getMaterial(String.valueOf(mine.get("material")));
            String displayName = ChatColor.translateAlternateColorCodes('&', String.valueOf(mine.get("displayName")));
            List<String> lore = new ArrayList();
            String minGroup = String.valueOf(mine.get("minGroup"));
            int minLevel = Integer.parseInt(String.valueOf(mine.get("minLevel")));
            int amount = Integer.parseInt(String.valueOf(mine.get("amount")));
            short data = Short.parseShort(String.valueOf(mine.get("data")));
            int slot = Integer.parseInt(String.valueOf(mine.get("slot"))) - 1;
            String world = String.valueOf(loc.get("world"));
            double x = Double.parseDouble(String.valueOf(loc.get("x")));
            double y = Double.parseDouble(String.valueOf(loc.get("y")));
            double z = Double.parseDouble(String.valueOf(loc.get("z")));
            Location location = new Location(Bukkit.getWorld(world), x, y, z);
            lore.add(ChatColor.BLACK + id);
            JSONArray list = (JSONArray)mine.get("lore");

            for(int i = 0; i < list.size(); ++i) {
                JSONObject obj = (JSONObject)list.get(i);
                String s = ChatColor.translateAlternateColorCodes('&', (String)obj.get("line"));
                s = s.replace("$group", minGroup).replace("$level", String.valueOf(minLevel));
                lore.add(s);
            }

            mineSet.add(new Mine(id, material, displayName, lore, minGroup, minLevel, amount, data, location, slot));
        }

    }
}