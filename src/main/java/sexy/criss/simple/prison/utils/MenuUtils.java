package sexy.criss.simple.prison.utils;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.mobs.EntityTypes;
import sexy.criss.simple.prison.Faction;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.PrisonMine;
import sexy.criss.simple.prison.PrisonPlayer;

public class MenuUtils {

   public static void showUpgradeMenu(Player player) {
      PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(player);
      PrisonItem pi = PrisonItem.getPrisonItem(player.getItemInHand());
      ItemStack blank = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)11);
      ItemMeta im = blank.getItemMeta();
      im.setDisplayName(" ");
      blank.setItemMeta(im);
      Inventory menu = Bukkit.createInventory(player, 9, Main.UPGRADE_MENU_NAME);

      for(int i = 0; i < 9; ++i) {
         if(i == 2) {
            menu.setItem(i, pi.getUsableItem());
         } else if(i == 6) {
            if(pi.hasNext()) {
               menu.setItem(i, pi.getUpgradingItem(pp));
            } else {
               player.sendMessage(ChatColor.GOLD + "Этот предмет невозможно улучшить");
            }
         } else {
            menu.setItem(i, blank);
         }
      }

      player.openInventory(menu);
   }

   public static void showGiveMenu(Player player) {
      Inventory menu = Bukkit.createInventory(player, 54, Main.GIVE_MENU_NAME);
      PrisonItem.items.values().forEach(i -> menu.addItem(i.getUsableItem()));

      player.openInventory(menu);
   }

   public static void showFactionMenu(Player player) {
      Inventory menu = Bukkit.createInventory(player, 36, Main.FACTION_MENU_NAME);

      for (Object o : Faction.factions.values()) {
         Faction item = (Faction) o;
         menu.addItem(item.getFactionIcon());
      }
      Faction.factions.values().forEach(f -> menu.addItem(f.getFactionIcon()));

      player.openInventory(menu);
   }

   public static void showStatsMenu(Player player) {
      PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(player);
      Inventory menu = Bukkit.createInventory(player, 9 * 3, Main.STATS_MENU_NAME);

      menu.setItem(10, new Stack(Material.BLAZE_POWDER)
              .displayName("&7Выбрать: &eСилу")
              .lore(  "",
                      "&7В данном разделе вы сможете улучшить",
                      "&7уровень своей силы и повысить",
                      "&7наносимый урон по вашим врагам.",
                      "",
                      "&eНажмите ЛКМ, чтобы выполнить.")
              .amount(pp.getPassives().getOrDefault("Strength", 0)));
      menu.setItem(12, new Stack(Material.GOLD_NUGGET)
              .displayName("&7Выбрать: &eБогатство")
              .lore(  "",
                      "&7В данном разделе вы сможете улучшить",
                      "&7уровень своего богатства.",
                      "",
                      "&7После прокачки данного навыка, вы",
                      "&7сможете получать больше:",
                      "&7 - &cОпыта",
                      "&7 - &cЗолота",
                      "&7 - &cОтмычек",
                      "&7 - &cВещей в сундуке",
                      "",
                      "&eНажмите ЛКМ, чтобы выполнить.")
              .amount(pp.getPassives().getOrDefault("Fortune", 0)));
      menu.setItem(14, new Stack(Material.FERMENTED_SPIDER_EYE)
              .displayName("&7Выбрать: &eБедствие")
              .lore(  "",
                      "&7В данном разделе вы сможете улучшить",
                      "&7уровень своего бедствия и испорченности.",
                      "",
                      "&7Вы были прокляты и носите эту порчу на себе.",
                      "",
                      "&7Ваша проклятая печать будет наносить вред",
                      "&7любому, кого вы косьнётесь.",
                      "",
                      "&7Никто не убежит, никто не скроется.",
                      "",
                      "&eНажмите ЛКМ, чтобы выполнить.")
              .amount(pp.getPassives().getOrDefault("Curse", 0)));
      menu.setItem(16, new Stack(Material.FEATHER)
              .displayName("&7Выбрать: &eПроворотство")
              .lore(  "",
                      "&7В данном разделе вы сможете улучшить",
                      "&7уровень своего проворотства, ловкости и хитрости.",
                      "",
                      "&7Ваша скорость повышена с каждым новым уровнем.",
                      "",
                      "&7Вы способны сбежать с любой передряги,",
                      "&7вас невозможно поймать.",
                      "",
                      "&7Ваша скорость - оружие.",
                      "&7 - &cВозможность уклонения от ударов.",
                      "&7 - &cУрон от боссов уменьшен в два раза.",
                      "&7 - &cМонстры не атакуют вас, пока вы сами",
                      "&7   &cне решите напасть на них.",
                      "",
                      "&eНажмите ЛКМ, чтобы выполнить."
                      )
      .amount(pp.getPassives().getOrDefault("Agility", 0)));

      player.openInventory(menu);
   }

   public static void showStrengthMenu(Player player) {
      PrisonPlayer prisonPlayer = PrisonPlayer.getPrisonPlayer(player);
      Inventory menu = Bukkit.createInventory(player, 9, Main.STRENGHT_MENU_NAME);

      for(int i = 0; i < 9; ++i) {
         if(prisonPlayer.getPassives().containsKey("Strength")) {
            int playerLevel = prisonPlayer.getPassives().get("Strength");
            ItemStack notAvaible;
            ItemMeta notAvaibleMeta;
            if(playerLevel > i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Сила (Ур. " + (i + 1) + ")");
               notAvaible.setDurability((short)5);
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Увеличивает наносимый урон на " + 4 * (i + 1) + "%", ChatColor.YELLOW + "Куплено."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else if(playerLevel == i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaible.setDurability((short)13);
               short price = 0;
               short pkills = 0;
               short ratkills = 0;
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Сила (Ур. " + (i + 1) + ")");
               switch(i + 1) {
                  case 1:
                     price = 400;
                     pkills = 30;
                     ratkills = 50;
                     break;
                  case 2:
                     price = 1000;
                     pkills = 80;
                     ratkills = 125;
                     break;
                  case 3:
                     price = 1600;
                     pkills = 160;
                     ratkills = 250;
                     break;
                  case 4:
                     price = 2500;
                     pkills = 270;
                     ratkills = 400;
                     break;
                  case 5:
                     price = 3750;
                     pkills = 400;
                     ratkills = 600;
                     break;
                  case 6:
                     price = 5200;
                     pkills = 560;
                     ratkills = 900;
                     break;
                  case 7:
                     price = 7000;
                     pkills = 780;
                     ratkills = 1350;
                     break;
                  case 8:
                     price = 9000;
                     pkills = 1000;
                     ratkills = 1900;
                     break;
                  case 9:
                     price = 12000;
                     pkills = 1400;
                     ratkills = 2750;
               }

               ArrayList lore = new ArrayList();
               lore.add(ChatColor.GRAY + "Увеличивает наносимый урон на " + 4 * (i + 1) + "%");
               if(prisonPlayer.hasMoney(price)) {
                  lore.add(ChatColor.GREEN + "Купить за " + ChatColor.YELLOW + price + ChatColor.GREEN + "$");
               } else {
                  lore.add(ChatColor.RED + "Стоимость: " + ChatColor.YELLOW + price + ChatColor.RED + "$");
               }

               if(prisonPlayer.getKills() >= pkills) {
                  lore.add(ChatColor.GREEN + "Необходимо " + pkills + " убийств");
               } else {
                  lore.add(ChatColor.RED + "Необходимо " + pkills + " убийств");
               }

               if(prisonPlayer.getMob_log().containsKey(EntityTypes.RAT) && prisonPlayer.getMob_log().get(EntityTypes.RAT) >= ratkills) {
                  lore.add(ChatColor.GREEN + "Необходимо " + ratkills + " убийств крыс");
               } else {
                  lore.add(ChatColor.RED + "Необходимо " + ratkills + " убийств крыс");
               }

               notAvaibleMeta.setLore(lore);
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaible.setDurability((short)14);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Сила (Ур. " + (i + 1) + ")");
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Увеличивает наносимый урон на " + 4 * (i + 1) + "%", ChatColor.RED + "Недоступно."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            }
         }
      }

      player.openInventory(menu);
   }

   public static void showAgilityMenu(Player player) {
      PrisonPlayer prisonPlayer = PrisonPlayer.getPrisonPlayer(player);
      Inventory menu = Bukkit.createInventory(player, 9, Main.AGILITY_MENU_NAME);

      for(int i = 0; i < 5; ++i) {
         if(prisonPlayer.getPassives().containsKey("Agility")) {
            int playerLevel = prisonPlayer.getPassives().get("Agility");
            ItemStack notAvaible;
            ItemMeta notAvaibleMeta;
            if(playerLevel > i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Ловкость (Ур. " + (i + 1) + ")");
               notAvaible.setDurability((short)5);
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Увеличивает шанс уворота на " + 3 * (i + 1) + "%", ChatColor.YELLOW + "Куплено."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else if(playerLevel == i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaible.setDurability((short)13);
               short price = 0;
               short pkills = 0;
               short sand = 0;
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Ловкость (Ур. " + (i + 1) + ")");
               switch(i + 1) {
                  case 1:
                     price = 500;
                     pkills = 10;
                     sand = 100;
                     break;
                  case 2:
                     price = 1250;
                     pkills = 25;
                     sand = 500;
                     break;
                  case 3:
                     price = 1900;
                     pkills = 70;
                     sand = 2500;
                     break;
                  case 4:
                     price = 2850;
                     pkills = 150;
                     sand = 7500;
                     break;
                  case 5:
                     price = 4000;
                     pkills = 310;
                     sand = 15000;
               }

               ArrayList lore = new ArrayList();
               lore.add(ChatColor.GRAY + "Увеличивает шанс уворота на " + 3 * (i + 1) + "%");
               if(prisonPlayer.hasMoney(price)) {
                  lore.add(ChatColor.GREEN + "Купить за " + ChatColor.YELLOW + price + ChatColor.GREEN + "$");
               } else {
                  lore.add(ChatColor.RED + "Стоимость: " + ChatColor.YELLOW + price + ChatColor.RED + "$");
               }

               if(prisonPlayer.getKills() >= pkills) {
                  lore.add(ChatColor.GREEN + "Необходимо " + pkills + " убийств");
               } else {
                  lore.add(ChatColor.RED + "Необходимо " + pkills + " убийств");
               }

               if(prisonPlayer.getBlock_log().containsKey(Material.SAND) && prisonPlayer.getBlock_log().get(Material.SAND) >= sand) {
                  lore.add(ChatColor.GREEN + "Необходимо сломать " + sand + " блоков песка");
               } else {
                  lore.add(ChatColor.RED + "Необходимо сломать " + sand + " блоков песка");
               }

               notAvaibleMeta.setLore(lore);
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaible.setDurability((short)14);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Ловкость (Ур. " + (i + 1) + ")");
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Увеличивает шанс уворота на " + 3 * (i + 1) + "%", ChatColor.RED + "Недоступно."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            }
         }
      }

      player.openInventory(menu);
   }

   public static void showNeedsMenu(Player player) {
      PrisonPlayer prisonPlayer = PrisonPlayer.getPrisonPlayer(player);
      Inventory menu = Bukkit.createInventory(player, 9, Main.AGILITY_MENU_NAME);

      for(int i = 0; i < 3; ++i) {
         if(prisonPlayer.getPassives().containsKey("Needs")) {
            int playerLevel = prisonPlayer.getPassives().get("Needs");
            ItemStack notAvaible;
            ItemMeta notAvaibleMeta;
            if(playerLevel > i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Потребности (Ур. " + (i + 1) + ")");
               notAvaible.setDurability((short)5);
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Вам нужно ходить в душ и в туалет на  " + 5 * (i + 1) + " минут реже", ChatColor.YELLOW + "Куплено."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else if(playerLevel == i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaible.setDurability((short)13);
               short price = 0;
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Потребности (Ур. " + (i + 1) + ")");
               switch(i + 1) {
                  case 1:
                     price = 5000;
                     break;
                  case 2:
                     price = 15000;
                     break;
                  case 3:
                     price = 25000;
               }

               ArrayList lore = new ArrayList();
               lore.add(ChatColor.GRAY + "Вам нужно ходить в душ и в туалет на  " + 5 * (i + 1) + " минут реже");
               if(prisonPlayer.hasMoney(price)) {
                  lore.add(ChatColor.GREEN + "Купить за " + ChatColor.YELLOW + price + ChatColor.GREEN + "$");
               } else {
                  lore.add(ChatColor.RED + "Стоимость: " + ChatColor.YELLOW + price + ChatColor.RED + "$");
               }

               notAvaibleMeta.setLore(lore);
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaible.setDurability((short)14);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Потребности (Ур. " + (i + 1) + ")");
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Вам нужно ходить в душ и в туалет на  " + 5 * (i + 1) + " минут реже", ChatColor.RED + "Недоступно."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            }
         }
      }

      player.openInventory(menu);
   }

   public static void showFortuneMenu(Player player) {
      PrisonPlayer prisonPlayer = PrisonPlayer.getPrisonPlayer(player);
      Inventory menu = Bukkit.createInventory(player, 9, Main.FORTUNE_MENU_NAME);

      for(int i = 0; i < 3; ++i) {
         if(prisonPlayer.getPassives().containsKey("Fortune")) {
            int playerLevel = prisonPlayer.getPassives().get("Fortune");
            ItemStack notAvaible;
            ItemMeta notAvaibleMeta;
            if(playerLevel > i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Удача (Ур. " + (i + 1) + ")");
               notAvaible.setDurability((short)5);
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Увеличивает шанс найти ключ в " + (i + 2) + " раза", ChatColor.YELLOW + "Куплено."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else if(playerLevel == i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaible.setDurability((short)13);
               short price = 0;
               short dirt = 0;
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Удача (Ур. " + (i + 1) + ")");
               switch(i + 1) {
                  case 1:
                     price = 200;
                     dirt = 2000;
                     break;
                  case 2:
                     price = 450;
                     dirt = 5000;
                     break;
                  case 3:
                     price = 800;
                     dirt = 10000;
               }

               ArrayList lore = new ArrayList();
               lore.add(ChatColor.GRAY + "Увеличивает шанс найти ключ в " + (i + 2) + " раза");
               if(prisonPlayer.hasMoney(price)) {
                  lore.add(ChatColor.GREEN + "Купить за " + ChatColor.YELLOW + price + ChatColor.GREEN + "$");
               } else {
                  lore.add(ChatColor.RED + "Стоимость: " + ChatColor.YELLOW + price + ChatColor.RED + "$");
               }

               if(prisonPlayer.getBlock_log().containsKey(Material.DIRT) && prisonPlayer.getBlock_log().get(Material.DIRT) >= dirt) {
                  lore.add(ChatColor.GREEN + "Необходимо сломать " + dirt + " блоков земли");
               } else {
                  lore.add(ChatColor.RED + "Необходимо сломать " + dirt + " блоков земли");
               }

               notAvaibleMeta.setLore(lore);
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaible.setDurability((short)14);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Удача (Ур. " + (i + 1) + ")");
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "Увеличивает шанс найти ключ в " + (i + 2) + " раза", ChatColor.RED + "Недоступно."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            }
         }
      }

      player.openInventory(menu);
   }

   public static void showCurseMenu(Player player) {
      PrisonPlayer prisonPlayer = PrisonPlayer.getPrisonPlayer(player);
      Inventory menu = Bukkit.createInventory(player, 9, Main.CURSE_MENU_NAME);

      for(int i = 0; i < 5; ++i) {
         if(prisonPlayer.getPassives().containsKey("Curse")) {
            int playerLevel = prisonPlayer.getPassives().get("Curse");
            ItemStack notAvaible;
            ItemMeta notAvaibleMeta;
            if(playerLevel > i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Проклятье (Ур. " + (i + 1) + ")");
               notAvaible.setDurability((short)5);
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "С вероятность " + 2 * (i + 1) + "%" + " вы наложите проклятье при ударе", ChatColor.YELLOW + "Куплено."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else if(playerLevel == i) {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Проклятье (Ур. " + (i + 1) + ")");
               notAvaible.setDurability((short)13);
               short price = 0;
               short zombiekills = 0;
               short goldblock = 0;
               switch(i + 1) {
                  case 1:
                     price = 250;
                     zombiekills = 30;
                     goldblock = 50;
                     break;
                  case 2:
                     price = 550;
                     zombiekills = 80;
                     goldblock = 125;
                     break;
                  case 3:
                     price = 1000;
                     zombiekills = 160;
                     goldblock = 250;
                     break;
                  case 4:
                     price = 1700;
                     zombiekills = 270;
                     goldblock = 400;
                     break;
                  case 5:
                     price = 2500;
                     zombiekills = 400;
                     goldblock = 600;
               }

               ArrayList lore = new ArrayList();
               lore.add(ChatColor.GRAY + "С вероятность " + 2 * (i + 1) + "%" + " вы наложите проклятье при ударе");
               if(prisonPlayer.hasMoney(price)) {
                  lore.add(ChatColor.GREEN + "Купить за " + ChatColor.YELLOW + price + ChatColor.GREEN + "$");
               } else {
                  lore.add(ChatColor.RED + "Стоимость: " + ChatColor.YELLOW + price + ChatColor.RED + "$");
               }

               if(prisonPlayer.getMobKillCount(EntityTypes.ZOMBIE) >= zombiekills) {
                  lore.add(ChatColor.GREEN + "Необходимо " + zombiekills + " убийств зомби");
               } else {
                  lore.add(ChatColor.RED + "Необходимо " + zombiekills + " убийств зомби");
               }

               if(prisonPlayer.getBlockDigsCount(Material.GOLD_BLOCK) >= goldblock) {
                  lore.add(ChatColor.GREEN + "Необходимо сломать " + goldblock + " золотых блоков");
               } else {
                  lore.add(ChatColor.RED + "Необходимо сломать " + goldblock + " золотых блоков");
               }

               notAvaibleMeta.setLore(lore);
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            } else {
               notAvaible = new ItemStack(Material.WOOL);
               notAvaible.setDurability((short)14);
               notAvaibleMeta = notAvaible.getItemMeta();
               notAvaibleMeta.setDisplayName(ChatColor.GOLD + "Проклятье (Ур. " + (i + 1) + ")");
               notAvaibleMeta.setLore(Arrays.asList(ChatColor.GRAY + "С вероятность " + 2 * (i + 1) + "%" + " вы наложите проклятье при ударе", ChatColor.RED + "Недоступно."));
               notAvaible.setItemMeta(notAvaibleMeta);
               menu.setItem(i, notAvaible);
            }
         }
      }

      player.openInventory(menu);
   }

   public static void showShopMenu(Player p) {
      PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
      FileConfiguration conf = Main.shop_storage;
      ConfigurationSection sec = conf.getConfigurationSection("shop");
      String title = Utils.f(sec.getString("title", "&a#shop"));
      int inv_size = sec.getInt("rows", 1);
      Inventory inv = Bukkit.createInventory(null, 9 * inv_size, title);
      // # slot, prisonItemID, amount, price
      for(String s : sec.getStringList("items")) {
         String[] ar = s.split(", ");
         int slot = 0;
         int size = 1;
         int price = 0;
         PrisonItem item = null;

         try {
            slot = Integer.parseInt(ar[0]) - 1;
            item = PrisonItem.getPrisonItem(ar[1]);
            size = Integer.parseInt(ar[2]);
            price = Integer.parseInt(ar[3]);
         } catch (Exception ex) {
            throw new IllegalArgumentException("Failure create shop item.");
         }

         ItemStack stack = item.getUsableItem();
         ItemMeta meta = stack.getItemMeta();
         ChatColor c = pp.hasMoney(price) ? ChatColor.GREEN : ChatColor.RED;
         meta.setDisplayName(c + Utils.strip(item.getName()));
         meta.setLore(Utils.f(Lists.newArrayList(
                 ChatColor.BLACK + item.getId(),
                 c + "● &7Стоимость: &e" + price + "$",
                 "&a● &7Количество: &e" + size,
                 "",
                 "&eНажмите ЛКМ, чтобы выполнить.")));
         meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
         stack.setAmount(size);
         stack.setItemMeta(meta);

         inv.setItem(slot, stack);
      }

      p.openInventory(inv);
   }

}
