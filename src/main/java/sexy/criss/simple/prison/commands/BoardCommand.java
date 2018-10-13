/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/28/2018 - 23:40 Tuesday
 *
 *******************************************************/
package sexy.criss.simple.prison.commands;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.Events;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Stack;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Map;

public class BoardCommand extends ACommand {
    private static final String name_color = Utils.f("&cBOARD &8│ Цветокоррекция");
    private static final String name_main = Utils.f("&cBOARD &8│ Главная");

    public BoardCommand() {
        super("board", "scoreboard");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        toPlayer1(p);
        return false;
    }

    public static void handle(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getClickedInventory() == null) return;
        Inventory v = e.getClickedInventory();
        String t = v.getName();
        if(t.equals(name_color)) {
            e.setCancelled(true);
            String color;
            String name;

            if(e.getCurrentItem().getType().equals(Material.BEDROCK)) {
                p.sendMessage(Utils.f("&cИзвините, у вас уже активирована данная функция."));
                p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1, 1);
                return;
            }

            switch (e.getCurrentItem().getDurability()) {
                case 14:
                    name = "&cКрасный";
                    color = "&c";
                    break;
                case 5:
                    name = "&aЗелёный";
                    color = "&a";
                    break;
                case 4:
                    name = "&eЖёлтый";
                    color = "&e";
                    break;
                default:
                    color = "&9";
                    name = "&9Синий";
                    break;
            }

            p.sendMessage(Utils.f("&7Таблица справа изменила свой цвет на %s&7.", name));
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1,1);
            Events.colorCodes.put(p, color);
            toPlayer2(p);
            return;
        }
        if(t.equals(name_main)) {
            e.setCancelled(true);
            switch (e.getCurrentItem().getType()) {
                case INK_SACK:
                    toPlayer2(p);
                    break;

            }
        }
    }

    private static void toPlayer2(Player player) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, Utils.f(name_color));
        Map<Integer, ItemStack> items = Maps.newHashMap();

        items.put(0, new Stack(Material.BARRIER).amount(0).displayName("&7RESET").lore(
                "",
                "&7Восстанавливает ваш цвет боковой таблицы.",
                "&7После активации стандартный цвет будет",
                "&7автоматически установлен на &9Синий&7.",
                "",
                "&eНажмите ЛКМ для выбора."
        ));
        items.put(1, new Stack(Material.STAINED_GLASS).durability((short) 5).displayName("&7Активировать &aЗелёный&7 цвет.").lore(
                "",
                "&7Устанавливает ваш цвет боковой таблицы ",
                "&7на &aЗелёный&7.",
                "",
                "&eНажмите ЛКМ для выбора."
        ));
        items.put(2, new Stack(Material.STAINED_GLASS).durability((short) 4).displayName("&7Активировать &eЖёлтый&7 цвет.").lore(
                "",
                "&7Устанавливает ваш цвет боковой таблицы ",
                "&7на &eЖёлтый&7.",
                "",
                "&eНажмите ЛКМ для выбора."
        ));
        items.put(3, new Stack(Material.STAINED_GLASS).durability((short) 14).displayName("&7Активировать &cКрасный&7 цвет.").lore(
                "",
                "&7Устанавливает ваш цвет боковой таблицы ",
                "&7на &cКрасный&7.",
                "",
                "&eНажмите ЛКМ для выбора."
        ));
        items.put(4, new Stack(Material.BARRIER).amount(0).displayName("&7RESET").lore(
                "",
                "&7Восстанавливает ваш цвет боковой таблицы.",
                "&7После активации стандартный цвет будет",
                "&7автоматически установлен на &9Синий&7.",
                "",
                "&eНажмите ЛКМ для выбора."
        ));

        items.forEach(inv::setItem);
        for(int i = 0; i < inv.getSize(); i++) {
            short dura = inv.getItem(i).getDurability();
            String color = dura == 14 ? "&c" : dura == 5 ? "&a" : dura == 4 ? "&e" : "&9";
            if(Events.colorCodes.containsKey(player)) {
                if(Events.colorCodes.get(player).equals(color)) {
                    inv.setItem(i, new Stack(Material.BEDROCK).amount(0).displayName("&cЗАБЛОКИРОВАН").lore(
                            "",
                            "&cДанный цветовой код уже активирован.",
                            "",
                            "&7Для того, чтобы разблокировать этот",
                            "&7цветовой код, вам нужно выбрать любой другой."
                    ));
                } else {
                    inv.getItem(i).setAmount(1);
                }
            } else {
                if(color.equals("&9")) {
                    inv.setItem(i, new Stack(Material.BEDROCK).amount(0).displayName("&cЗАБЛОКИРОВАН").lore(
                            "",
                            "&cДанный цветовой код уже активирован.",
                            "",
                            "&7Для того, чтобы разблокировать этот",
                            "&7цветовой код, вам нужно выбрать любой другой."
                    ));
                } else {
                    inv.getItem(i).setAmount(1);
                }
            }
        }

        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1,1);
    }

    private static void toPlayer1(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, name_main);
        inv.setItem(4, new Stack(Material.INK_SACK).durability((short) 14).displayName("&7Цветокоррекция").lore(
                "",
                "&7В этом разделе доступна цветовая коррекция",
                "&7стандартной таблицы без каких-либо изменений.",
                "",
                "&eНажмите ЛКМ, чтобы выполнить."
        ).amount(1));

        inv.setItem(0, new Stack(Material.SIGN).displayName("&aСоздать свою таблицу &7│ &cСКОРО").lore(
                "",
                "&7В этом разделе вы сможете создать",
                "&7свою таблицу и использовать другие",
                "&7ключи с датой, чтобы получить",
                "&7более полезную статистику вашего персонажа.",
                "",
                "&cНевозможно использование."
        ));

        p.openInventory(inv);
        p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1,1);
    }

}
