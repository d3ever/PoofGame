package sexy.criss.simple.prison.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.utils.scoreboard.type.Entry;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class Item {

    public static ItemStack getItem(Material material, Integer amount, Short data, String name, List<String> lore) {
        ItemStack it = new ItemStack(material, amount, data);
        ItemMeta m = it.getItemMeta();
        m.setDisplayName(name);
        if (lore != null) {
            m.setLore(lore);
        }

        it.setItemMeta(m);
        return it;
    }

    public static ItemStack getItem(Material material, Integer amount, Short data, String name, String... lore) {
        return getItem(material, amount, data, name, Arrays.asList(lore));
    }

    public static ItemStack getItem(ItemStack itemstack, String enchantment) {
        String[] totalEnch = enchantment.split(":");
        Enchantment aEnch = Enchantment.getByName(totalEnch[0]);
        Integer aEnchLvl = 1;

        try {
            aEnchLvl = Integer.valueOf(totalEnch[1]);
        } catch (NumberFormatException ex) {
            Main.getInstance().getLogger().log(Level.WARNING, "Не удалось получить уровень зачарования \"" + totalEnch[0] + "\". Устанавливаем 1!", ex);
        }

        itemstack.addEnchantment(aEnch, aEnchLvl);
        return itemstack;
    }

    public static ItemStack getItem(String item) {
        String[] totalItem = item.split(" ");
        String[] totalMaterial = totalItem[0].split(":");
        String sMaterial = totalMaterial[0];
        String sData = totalMaterial[1];
        String sAmount = totalItem[1];
        String[] totalEnch = totalItem[2].replace("enchantment:", "").split("-");
        String totalName = totalItem[3].replace("name:", "").replace("_", " ").replace('&', '§');
        List<String> totalLore = new ArrayList();
        String allLore = totalItem[4].replace("lore:", "").replace("_", " ");
        String[] var13;
        int var12 = (var13 = allLore.split("\n")).length;

        for(int var11 = 0; var11 < var12; ++var11) {
            String s = var13[var11];
            totalLore.add(s.replace("&", "§"));
        }

        Material aMaterial = null;
        String aName = totalName != null ? totalName : "";
        List<String> aLore = !totalLore.isEmpty() && totalLore != null ? totalLore : null;
        int aAmount = 0;
        int aData = 0;

        try {
            aMaterial = Material.getMaterial(sMaterial);
        } catch (NullPointerException ex) {
            Main.getInstance().getLogger().log(Level.WARNING, "Не удалось получить тип предмета. Устанавливаем стандартные значения.", ex);
        }

        try {
            aAmount = Integer.valueOf(sAmount);
            aData = Integer.valueOf(sData);
        } catch (NumberFormatException var18) {
            Main.getInstance().getLogger().log(Level.WARNING, "Не удалось получить количество или дату предмета. Устанавливаем стандартные значения.", var18);
        }

        return getItem(aMaterial, aAmount, (short)aData, aName, aLore);
    }

    public static ItemStack getSkull(OfflinePlayer owner, Integer amount, String name, String... lore) {
        ItemStack it = new ItemStack(Material.SKULL_ITEM, amount, (short)3);
        SkullMeta m = (SkullMeta)it.getItemMeta();
        if (name != null) m.setDisplayName(name);
        if (lore != null) m.setLore(new ArrayList(Arrays.asList(lore)));

        m.setOwner(owner.getName());
        it.setItemMeta(m);
        return it;
    }

    public static ItemStack getSkull(String url, String name) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url.isEmpty()) {
            return head;
        } else {
            ItemMeta headMeta = head.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = headMeta.getClass().getDeclaredField("profile");
            } catch (SecurityException | NoSuchFieldException var9) {
                var9.printStackTrace();
            }

            profileField.setAccessible(true);

            try {
                profileField.set(headMeta, profile);
            } catch (IllegalAccessException | IllegalArgumentException var8) {
                var8.printStackTrace();
            }

            headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            head.setItemMeta(headMeta);
            return head;
        }
    }

    public static ItemStack getSkull(String url, String name, String lore) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url.isEmpty()) {
            return head;
        } else {
            ItemMeta headMeta = head.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = headMeta.getClass().getDeclaredField("profile");
            } catch (SecurityException | NoSuchFieldException var10) {
                var10.printStackTrace();
            }

            profileField.setAccessible(true);

            try {
                profileField.set(headMeta, profile);
            } catch (IllegalAccessException | IllegalArgumentException var9) {
                var9.printStackTrace();
            }

            headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if (lore != null) {
                headMeta.setLore(new ArrayList(Arrays.asList(lore)));
            }

            head.setItemMeta(headMeta);
            return head;
        }
    }

    public boolean isValid() {
        return false;
    }
}