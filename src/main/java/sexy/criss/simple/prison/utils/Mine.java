package sexy.criss.simple.prison.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.PrisonPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mine {
    public static Map<String, Mine> mines = new HashMap();
    private String id;
    private Material material;
    private String displayName;
    private List<String> lore;
    private String minGroup;
    private int minLevel;
    private Location location;
    private int amount;
    private short data;
    private int slot;

    public Mine(String id, Material material, String displayName, List<String> lore, String minGroup, int minLevel, int amount, short data, Location location, int slot) {
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.minGroup = minGroup;
        this.minLevel = minLevel;
        this.amount = amount;
        this.data = data;
        this.location = location;
        this.slot = slot;
        mines.put(id, this);
    }

    public Mine getMine(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return null;
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (!meta.hasLore()) {
                return null;
            } else {
                String id = meta.getLore().get(0);
                id = ChatColor.stripColor(id);
                return mines.getOrDefault(id, null);
            }
        }
    }

    public Mine getMine(String id) {
        return mines.getOrDefault(id, null);
    }

    public ItemStack getIcon() {
        ItemStack mine = new ItemStack(this.material, this.amount, this.data);
        ItemMeta meta = mine.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.displayName));
        meta.setLore(this.lore);
        mine.setItemMeta(meta);
        return mine;
    }

    public boolean hasAccess(Player p) {
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        return p.hasPermission(this.minGroup) && pp.getLevel() >= this.minLevel;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getMinGroup() {
        return this.minGroup;
    }

    public void setMinGroup(String permission) {
        this.minGroup = permission;
    }

    public int getMinLevel() {
        return this.minLevel;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public short getData() {
        return this.data;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setData(short data) {
        this.data = data;
    }

    public Location getLocation() {
        return this.location;
    }
}