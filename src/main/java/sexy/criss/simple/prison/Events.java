//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison;

import com.google.common.collect.Maps;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simple.prison.manager.DonateMines;
import sexy.criss.simple.prison.mobs.EntityTypes;
import sexy.criss.simple.prison.utils.MenuUtils;
import sexy.criss.simple.prison.utils.Mine;
import sexy.criss.simple.prison.utils.Utils;
import sexy.criss.simple.prison.utils.scoreboard.Board;
import sexy.criss.simple.prison.utils.scoreboard.common.EntryBuilder;
import sexy.criss.simple.prison.utils.scoreboard.type.Entry;
import sexy.criss.simple.prison.utils.scoreboard.type.Scoreboard;
import sexy.criss.simple.prison.utils.scoreboard.type.ScoreboardHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Events implements Listener {
    public static String LEVELUP;
    public static String UPGRADE;
    public static String GIVE;
    public static String FACTIONS;
    public static String MINES;
    public static String STATS;
    public static Random rand;
    public static List<String> invs;
    private Map<Player, Scoreboard> scoreboards = Maps.newHashMap();

    static {
        LEVELUP = Main.LVLUP_MENU_NAME;
        UPGRADE = Main.UPGRADE_MENU_NAME;
        GIVE = Main.GIVE_MENU_NAME;
        FACTIONS = Main.FACTION_MENU_NAME;
        MINES = Main.MINES_MENU_NAME;
        STATS = Main.STATS_MENU_NAME;
        rand = new Random();
        invs = Arrays.asList(LEVELUP, UPGRADE, GIVE, FACTIONS, MINES, STATS, Main.CURSE_MENU_NAME, Main.FORTUNE_MENU_NAME, Main.AGILITY_MENU_NAME, Main.STRENGHT_MENU_NAME);
    }

    public static Map<Player, String> colorCodes = Maps.newHashMap();

    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        event.setJoinMessage(null);
        PrisonPlayer.loadPrisonPlayer(p);
        Scoreboard board = Board.createBoard(p).setHandler(new ScoreboardHandler() {

            @Override
            public String getTitle(Player player) {
                PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(player);
                if(pp.hasFaction()) return Utils.f(pp.getFaction().getPrefix().length() > 16 ? pp.getFaction().getPrefix().substring(0, 16) : pp.getFaction().getPrefix());

                return Utils.f("&c&lREVOLUTION");
            }

            @Override
            public List<Entry> getEntries(Player player) {
                PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(player);
                String f = pp.hasFaction() ? pp.getFaction().getName() : "&cнет";
                String color = colorCodes.getOrDefault(player, "&9");
                return new EntryBuilder()
                        .next("&7Золото: " + color + Utils.format(pp.getBalance()) + " ⛃")
                        .blank()
                        .next("&7Фракция:")
                        .next(f)
                        .blank()
                        .next("&7Уровень: " + color + pp.getLevel())
                        .blank()
                        .next("&7Персональный опыт:")
                        .next(color + pp.getExp() + "&r")
                        .blank()
                        .next("&7Множитель блоков " + color + "x" + pp.getBlocksMultiplier())
                        .next("&7Множитель ключей " + color + "x" + pp.getKeysMultiplier())
                        .blank()
                        .next(color + "vk.com/revolutionprison")
                        .build();
            }
        }).setUpdateInterval(200L);
        board.activate();
        scoreboards.put(p, board);
        startUpdater(p, PrisonPlayer.getPrisonPlayer(p));
    }

    private void startUpdater(Player p, PrisonPlayer pp) {
        new BukkitRunnable() {

            private int parse(int a, int b) {
                return a > b ? b : a;
            }

            @Override
            public void run() {

                try {
                    int val = (pp.getExp() * 100) / pp.getNextLevelBlockPrice();
                    int cur = parse(pp.getExp(), pp.getNextLevelBlockPrice()) + parse(pp.getBalance(), pp.getNextLevelMoneyPrice());
                    int max = pp.getNextLevelMoneyPrice() + pp.getNextLevelBlockPrice();

                    String msg = Utils.f(Utils.getProgressBar(cur, max, 10, "▒", "&c", "&7") + "&a " + val) + "%";
                    msg = cur == max ? Utils.f(Utils.getProgressBar(cur, max, 10, "▒", "&c", "&7") + "&a&l /LEVEL") : msg;
                    Utils.sendActionBarMessage(p, msg);
                } catch (Exception ex) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Entity[] arrayOfEntity;
        int j = (arrayOfEntity = event.getChunk().getEntities()).length;

        for(int i = 0; i < j; ++i) {
            Entity cEntity = arrayOfEntity[i];
            if (cEntity.getType() != EntityType.PLAYER) {
                event.setCancelled(true);
                break;
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        event.setQuitMessage(null);
        scoreboards.get(p).deactivate();
        scoreboards.remove(p);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws IllegalArgumentException {
        if(event.getClickedInventory() == null || event.getInventory() == null) return;
        if (event.getClick() == ClickType.NUMBER_KEY) {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
        } else {
            Inventory inv = event.getClickedInventory();
            Player player = (Player)event.getWhoClicked();
            PrisonPlayer prisonPlayer = PrisonPlayer.getPrisonPlayer(player);
            if (invs.contains(inv.getName())) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    Material material = event.getCurrentItem().getType();
                    String name = inv.getName();
                    if (UPGRADE.equals(name)) {
                        if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() && event.getCurrentItem().getItemMeta().getDisplayName().contains("Улучшение")) {
                            event.setCancelled(true);
                            if (PrisonItem.isCustomItem(event.getCurrentItem())) {
                                PrisonItem pi = PrisonItem.getPrisonItem(player.getItemInHand());
                                if (pi.equals(PrisonItem.getPrisonItem(event.getInventory().getItem(2)))) {
                                    if (prisonPlayer.hasMoney(pi.getNext().getPrice()) && prisonPlayer.takeMoney(pi.getNext().getPrice())) {
                                        player.sendMessage(ChatColor.GREEN + "Ваше оружие было улучшено!");
                                        player.setItemInHand(pi.getNext().getUsableItem());
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Недостаточно средств!");
                                    }
                                } else {
                                    event.setCancelled(true);
                                    player.sendMessage(ChatColor.RED + "Замечена попытка подмены предмета! Ай-ай-ай!");
                                    player.closeInventory();
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Требования не выполнены!");
                            }

                            player.closeInventory();
                        }
                    } else if (GIVE.equals(name)) {
                        player.getInventory().addItem(event.getCurrentItem());
                    } else if (FACTIONS.equals(name) && Faction.isFaction(event.getCurrentItem())) {
                        Faction faction = Faction.getFaction(event.getCurrentItem());
                        prisonPlayer.setFaction(faction);
                        player.sendMessage(ChatColor.YELLOW + "Теперь вы принадлежите к фракции " + faction.getName());
                        player.closeInventory();
                    } else if (name.toLowerCase().contains("skills")) {
                        event.setCancelled(true);
                        if (event.getCurrentItem().getType().equals(Material.BLAZE_POWDER)) {
                            MenuUtils.showStrengthMenu(player);
                        } else if (event.getCurrentItem().getType() == Material.FEATHER) {
                            MenuUtils.showAgilityMenu(player);
                        } else if (event.getCurrentItem().getType() == Material.INK_SACK) {
                            MenuUtils.showNeedsMenu(player);
                        } else if (event.getCurrentItem().getType() == Material.GOLD_NUGGET) {
                            MenuUtils.showFortuneMenu(player);
                        } else if (event.getCurrentItem().getType() == Material.FERMENTED_SPIDER_EYE) {
                            MenuUtils.showCurseMenu(player);
                        } else {
                            int level;
                            short price;
                            short zombiekills;
                            short goldblock;
                            int playerLevel;
                            if (event.getCurrentItem().getType().equals(Material.WOOL) && event.getCurrentItem().getDurability() == 13 && event.getCurrentItem().getItemMeta().getDisplayName().contains("Сила")) {
                                level = event.getSlot() + 1;
                                price = 0;
                                zombiekills = 0;
                                goldblock = 0;
                                switch(level) {
                                    case 1:
                                        price = 400;
                                        zombiekills = 30;
                                        goldblock = 50;
                                        break;
                                    case 2:
                                        price = 1000;
                                        zombiekills = 80;
                                        goldblock = 125;
                                        break;
                                    case 3:
                                        price = 1600;
                                        zombiekills = 160;
                                        goldblock = 250;
                                        break;
                                    case 4:
                                        price = 2500;
                                        zombiekills = 270;
                                        goldblock = 400;
                                        break;
                                    case 5:
                                        price = 3750;
                                        zombiekills = 400;
                                        goldblock = 600;
                                        break;
                                    case 6:
                                        price = 5200;
                                        zombiekills = 560;
                                        goldblock = 900;
                                        break;
                                    case 7:
                                        price = 7000;
                                        zombiekills = 780;
                                        goldblock = 1350;
                                        break;
                                    case 8:
                                        price = 9000;
                                        zombiekills = 1000;
                                        goldblock = 1900;
                                        break;
                                    case 9:
                                        price = 12000;
                                        zombiekills = 1400;
                                        goldblock = 2750;
                                }

                                if (prisonPlayer.hasMoney(price)) {
                                    if (prisonPlayer.kills >= zombiekills) {
                                        if (prisonPlayer.mob_log.get(EntityTypes.RAT) >= goldblock) {
                                            playerLevel = prisonPlayer.passives.get("Strength");
                                            prisonPlayer.passives.remove("Strength");
                                            prisonPlayer.passives.put("Strength", playerLevel + 1);
                                            prisonPlayer.takeMoney(price);
                                            MenuUtils.showStrengthMenu(player);
                                            player.sendMessage(ChatColor.GREEN + "Пассив успешно куплен!");
                                        } else player.sendMessage(ChatColor.RED + "У Вас недостаточно убийств крыс!");
                                    } else player.sendMessage(ChatColor.RED + "У Вас недостаточно убийств!");
                                } else player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                            } else if (event.getCurrentItem().getType().equals(Material.WOOL) && event.getCurrentItem().getDurability() == 13 && event.getCurrentItem().getItemMeta().getDisplayName().contains("Ловкость")) {
                                level = event.getSlot() + 1;
                                price = 0;
                                zombiekills = 0;
                                goldblock = 0;
                                switch(level) {
                                    case 1:
                                        price = 500;
                                        zombiekills = 10;
                                        goldblock = 100;
                                        break;
                                    case 2:
                                        price = 1250;
                                        zombiekills = 25;
                                        goldblock = 500;
                                        break;
                                    case 3:
                                        price = 1900;
                                        zombiekills = 70;
                                        goldblock = 2500;
                                        break;
                                    case 4:
                                        price = 2850;
                                        zombiekills = 150;
                                        goldblock = 7500;
                                        break;
                                    case 5:
                                        price = 4000;
                                        zombiekills = 310;
                                        goldblock = 15000;
                                }

                                if (prisonPlayer.hasMoney(price)) {
                                    playerLevel = prisonPlayer.passives.get("Agility");
                                    if (prisonPlayer.kills >= zombiekills) {
                                        if (prisonPlayer.block_log.get(Material.SAND) >= goldblock) {
                                            prisonPlayer.passives.remove("Agility");
                                            prisonPlayer.passives.put("Agility", playerLevel + 1);
                                            prisonPlayer.takeMoney(price);
                                            player.sendMessage(ChatColor.GREEN + "Пассив успешно куплен!");
                                            MenuUtils.showAgilityMenu(player);
                                        } else player.sendMessage(ChatColor.RED + "Вы сломали недостаточное количество блоков песка!");
                                    } else player.sendMessage(ChatColor.RED + "У Вас недостаточно убийств!");
                                } else player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                            } else if (event.getCurrentItem().getType().equals(Material.WOOL) && event.getCurrentItem().getDurability() == 13 && event.getCurrentItem().getItemMeta().getDisplayName().contains("Удача")) {
                                level = event.getSlot() + 1;
                                price = 0;
                                zombiekills = 0;
                                switch(level) {
                                    case 1:
                                        price = 200;
                                        zombiekills = 2000;
                                        break;
                                    case 2:
                                        price = 450;
                                        zombiekills = 5000;
                                        break;
                                    case 3:
                                        price = 800;
                                        zombiekills = 10000;
                                }

                                if (prisonPlayer.hasMoney(price)) {
                                    playerLevel = prisonPlayer.passives.get("Fortune");
                                    if (prisonPlayer.block_log.get(Material.DIRT) >= zombiekills) {
                                        prisonPlayer.passives.remove("Fortune");
                                        prisonPlayer.passives.put("Fortune", playerLevel + 1);
                                        prisonPlayer.takeMoney(price);
                                        player.sendMessage(ChatColor.GREEN + "Пассив успешно куплен!");
                                        MenuUtils.showFortuneMenu(player);
                                    } else player.sendMessage(ChatColor.RED + "Вы сломали недостаточное количество блоков земли!");
                                } else player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                            } else if (event.getCurrentItem().getType().equals(Material.WOOL) && event.getCurrentItem().getDurability() == 13 && event.getCurrentItem().getItemMeta().getDisplayName().contains("Проклятье")) {
                                level = event.getSlot() + 1;
                                price = 0;
                                zombiekills = 0;
                                goldblock = 0;
                                switch(level) {
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

                                if (prisonPlayer.hasMoney(price)) {
                                    if (prisonPlayer.getBlockDigsCount(Material.GOLD_BLOCK) >= goldblock) {
                                        if (prisonPlayer.mob_log.get(EntityTypes.ZOMBIE) >= zombiekills) {
                                            playerLevel = prisonPlayer.passives.get("Curse");
                                            prisonPlayer.passives.remove("Curse");
                                            prisonPlayer.passives.put("Curse", playerLevel + 1);
                                            prisonPlayer.takeMoney(price);
                                            player.sendMessage(ChatColor.GREEN + "Пассив успешно куплен!");
                                            MenuUtils.showCurseMenu(player);
                                        } else player.sendMessage(ChatColor.RED + "У Вас недостаточно убийств зомби!");
                                    } else player.sendMessage(ChatColor.RED + "Вы сломали недостаточно блоков золота!");
                                } else player.sendMessage(ChatColor.RED + "У Вас недостаточно денег!");
                            } else {
                                switch (event.getCurrentItem().getDurability()) {
                                    case 5:
                                        player.sendMessage(Utils.f("&7Вы уже купили данный уровень."));
                                        break;
                                    case 14:
                                        player.sendMessage(Utils.f("&cВам недоступен данный уровень."));
                                        break;
                                }
                            }
                        }
                    }

                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getCustomName() != null) {
            e.setDroppedExp(0);
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onBlockBreak(BlockBreakEvent event) {
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(event.getPlayer());
        if (!event.isCancelled() && event.getBlock().getType() != Material.TRIPWIRE && event.getBlock().getType() != Material.SAPLING && event.getBlock().getType() != Material.TRIPWIRE_HOOK && event.getBlock().getType() != Material.WATER_LILY && event.getBlock().getType() != Material.YELLOW_FLOWER && event.getBlock().getType() != Material.RED_ROSE) {
            Material material = event.getBlock().getType();
            Player player = event.getPlayer();
            pp.addBlockDig(material);


            pp.totalblocks += pp.getBlocksMultiplier();
            int mod;
            double xKeys = pp.getKeysMultiplier();
            if (pp.getPassives() != null && pp.getPassives().get("Fortune") != null && pp.getPassives().containsKey("Fortune"))
                mod = pp.getPassives().get("Fortune") + 1; else mod = 1;


            if (event.getBlock().getType() != Material.LEAVES && event.getBlock().getType() != Material.TRIPWIRE && event.getBlock().getType() != Material.SAPLING && event.getBlock().getType() != Material.LEAVES_2 && !event.getBlock().getType().isTransparent() && (double) rand.nextFloat() <= 0.005 * (xKeys > 1 ? mod + xKeys : mod )) {
                player.getInventory().addItem(PrisonItem.getPrisonItem("key").getUsableItem());
                player.sendMessage(ChatColor.GRAY + "Вы нашли отмычку.");
                player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
            }

        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        player.closeInventory();
        e.setKeepInventory(true);

        for(int i = 0; i < 36; ++i) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                if (this.willDrop(player, item)) {
                    player.getWorld().dropItem(player.getLocation(), item);
                    player.getInventory().remove(item);
                }

                if (player.getInventory().getHelmet() != null && this.willDrop(player, player.getInventory().getHelmet())) {
                    player.getWorld().dropItem(player.getLocation(), player.getInventory().getHelmet());
                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                }

                if (player.getInventory().getChestplate() != null && this.willDrop(player, player.getInventory().getChestplate())) {
                    player.getWorld().dropItem(player.getLocation(), player.getInventory().getChestplate());
                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                }

                if (player.getInventory().getLeggings() != null && this.willDrop(player, player.getInventory().getLeggings())) {
                    player.getWorld().dropItem(player.getLocation(), player.getInventory().getLeggings());
                    player.getInventory().setLeggings(new ItemStack(Material.AIR));
                }

                if (player.getInventory().getBoots() != null && this.willDrop(player, player.getInventory().getBoots())) {
                    player.getWorld().dropItem(player.getLocation(), player.getInventory().getBoots());
                    player.getInventory().setBoots(new ItemStack(Material.AIR));
                }
            }
        }

        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(player);
        ++pp.deaths;
        if(pp.getLastDamager() != null) {
            Player killer = Bukkit.getPlayer(pp.getLastDamager());
            if (pp.getLastDamager() != null && pp.getLastDamager().length() > 0 && killer != null) {
                ++PrisonPlayer.getPrisonPlayer(killer).kills;
                if (pp.takeMoney(pp.getLevel())) PrisonPlayer.getPrisonPlayer(killer).giveMoney(pp.getLevel());
            } else pp.takeMoney(pp.getLevel());
        }



        player.setRemainingAir(player.getMaximumAir());
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.setRemainingAir(player.getMaximumAir());
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1));
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
        player.sendMessage(ChatColor.DARK_RED + "Вы погибли.");
        player.spigot().respawn();
        Main.getInstance().getSpawnManager().teleport(player, false);
    }

    public boolean willDrop(Player p, ItemStack item) {
        return !PrisonItem.isCustomItem(item) || PrisonItem.getPrisonItem(item).willDrop();
    }

    @EventHandler
            (priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity d = event.getDamager();
        if (d instanceof Player || d instanceof Projectile) {
            if (d instanceof Projectile) {
                if (!(((Projectile)d).getShooter() instanceof Player)) {
                    return;
                }

                d = (Entity)((Projectile)d).getShooter();
            }

            PrisonPlayer damager = PrisonPlayer.getPrisonPlayer((Player) d);
            if (event.getEntity().getType() == EntityType.PLAYER) {
                PrisonPlayer entity = PrisonPlayer.getPrisonPlayer((Player) event.getEntity());
                entity.setLastDamager(d.getName());
                if (damager.hasFaction() && entity.hasFaction() && damager.getFaction() == entity.getFaction()) {
                    event.setCancelled(true);
                }

                if (rand.nextInt(100) + 1 <= entity.passives.getOrDefault("Agility", 0) * 3 && !event.isCancelled()) {
                    event.setCancelled(true);
                    event.getEntity().sendMessage(ChatColor.GREEN + "Вы уклонились от удара!");
                    event.getDamager().sendMessage(ChatColor.RED + "Вы промахнулись!");
                }

                if (rand.nextInt(100) + 1 <= damager.getPassives().getOrDefault("Curse",0) * 2 && !event.isCancelled()) {
                    PotionEffectType[] types = new PotionEffectType[]{PotionEffectType.BLINDNESS, PotionEffectType.SLOW, PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.CONFUSION, PotionEffectType.WITHER};
                    PotionEffectType selected = types[rand.nextInt(types.length)];
                    ((Player)event.getEntity()).addPotionEffect(new PotionEffect(selected, 200, 0));
                }
            }

            event.setDamage(event.getDamage() + (double) damager.getAdditionalDamage());
            event.setDamage(event.getDamage() + (double) (damager.passives.getOrDefault("Strength", 0) * 4 / 100));
        }

    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == State.CAUGHT_FISH && event.getCaught().getType() == EntityType.DROPPED_ITEM) {
            Item item = (Item) event.getCaught();
            ItemStack is = item.getItemStack();
            if (is.getType() == Material.ENCHANTED_BOOK || is.getType() == Material.LEATHER_BOOTS || is.getType() == Material.FISHING_ROD || is.getType() == Material.BOW) {
                item.remove();
                event.getPlayer().sendMessage(ChatColor.GREEN + "Вы поймали деньги!");
                PrisonPlayer.getPrisonPlayer(event.getPlayer()).giveMoney((1 + rand.nextInt(9)) + 10);
            }
        }

    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        ItemStack is = event.getItemDrop().getItemStack();
        if (PrisonItem.isCustomItem(is) && !PrisonItem.getPrisonItem(is).willDrop()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        if (event.getItem().getType().getMaxDurability() > 16 && !event.getItem().getItemMeta().spigot().isUnbreakable()) {
            ItemStack item = event.getItem();
            ItemMeta im = item.getItemMeta();
            item.setDurability((short)0);
            im.spigot().setUnbreakable(true);
            item.setItemMeta(im);
        }

        if (event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().contains("Улучшение")) {
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST && event.getClickedBlock().getLocation().equals(Main.LOCK_CHEST_LOCATION)) {
            event.setCancelled(true);
            if (event.getItem() != null && PrisonItem.isCustomItem(event.getItem()) && PrisonItem.getPrisonItem(event.getItem()) == PrisonItem.getPrisonItem("key")) {
                if (event.getItem().getAmount() > 1) event.getItem().setAmount(event.getItem().getAmount() - 1);
                else event.getPlayer().setItemInHand(new ItemStack(Material.AIR));

                Main.openKeyChestFor(event.getPlayer());
                return;
            }

            event.getPlayer().sendMessage(ChatColor.RED + "Вам нужен ключ, чтобы открыть этот сундук.");
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST && Main.chest_setters.contains(event.getPlayer())) {
            ConfigurationSection lock_chest;
            if (Main.config.contains("locked-chest.location")) {
                lock_chest = Main.config.getConfigurationSection("locked-chest.location");
            } else {
                lock_chest = Main.config.createSection("location");
            }

            Location loc = event.getClickedBlock().getLocation();
            Main.LOCK_CHEST_LOCATION = loc;
            lock_chest.set("x", loc.getBlockX());
            lock_chest.set("y", loc.getBlockY());
            lock_chest.set("z", loc.getBlockZ());
            lock_chest.set("world", loc.getWorld().getName());
            Main.config.set("locked-chest.location", lock_chest);
            Main.chest_setters.remove(event.getPlayer());
            Main.getInstance().saveConfig();
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.GRAY + "Сундук задан.");
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if ((e.getClickedBlock().getType() == Material.WORKBENCH || e.getClickedBlock().getType() == Material.ANVIL || e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) && !e.getPlayer().isOp()) {
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        if (!e.getWhoClicked().isOp()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onDeathNearSlime(EntityDeathEvent e) {
        for (Entity entity : e.getEntity().getNearbyEntities(10.0D, 5.0D, 10.0D)) {
            if (entity instanceof LivingEntity && entity.getCustomName() != null && entity.getCustomName().contains(Main.BIGSLIME_NAME.replace("#<3", ""))) {
                Slime slime = (Slime) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.SLIME);
                slime.setSize(3);
                slime.setMaxHealth((double) Main.TINYSLIME_HEALTH);
                slime.setHealth((double) Main.TINYSLIME_HEALTH);
                slime.setCustomName(Main.TINYSLIME_NAME);
            }
        }
    }

    @EventHandler
            (priority = EventPriority.LOWEST)
    public void onSpawn(CreatureSpawnEvent e) {
        if(!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Main.getInstance().getSpawnManager().getSpawnLocation());
    }

    @EventHandler
    public void onSlimeDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.SLIME && e.getDamager().getCustomName().equals(Main.TINYSLIME_NAME)) {
            e.setDamage((double)Main.TINYSLIME_DAMAGE);
        }

    }
}
