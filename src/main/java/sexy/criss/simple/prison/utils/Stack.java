/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/29/2018 - 01:59 Wednesday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils;

import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Stack extends ItemStack {

    public Stack(@NotNull Material material) {
        super.setType(material);
    }

    public Stack amount(@NotNull int amount) {
        super.setAmount(amount);
        return this;
    }

    public Stack lore(@NotNull String... lines) {
        ItemMeta meta = super.getItemMeta();
        List<String> c = Lists.newArrayList();
        Arrays.asList(lines).forEach(s -> c.add(Utils.f(s)));
        meta.setLore(c);
        super.setItemMeta(meta);
        return this;
    }

    public Stack displayName(@NotNull String name) {
        ItemMeta meta = super.getItemMeta();
        meta.setDisplayName(Utils.f(name));
        super.setItemMeta(meta);
        return this;
    }

    public Stack durability(@NotNull short durability) {
        super.setDurability(durability);
        return this;
    }

    public Stack addEnchant(@NotNull Enchantment enchantment, @NotNull int level) {
        super.addEnchantment(enchantment, level);
        return this;
    }

    public Stack flags(@NotNull ItemFlag... flags) {
        ItemMeta meta = super.getItemMeta();
        meta.addItemFlags(flags);
        super.setItemMeta(meta);
        return this;
    }

}
