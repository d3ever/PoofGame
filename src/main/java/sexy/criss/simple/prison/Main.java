//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import sexy.criss.simple.prison.commands.*;
import sexy.criss.simple.prison.handlers.*;
import sexy.criss.simple.prison.manager.DonateMines;
import sexy.criss.simple.prison.manager.ShopManager;
import sexy.criss.simple.prison.manager.SpawnManager;
import sexy.criss.simple.prison.manager.builders.TimedBlock;
import sexy.criss.simple.prison.mobs.EntityTypes;
import sexy.criss.simple.prison.mobs.Spawner;
import sexy.criss.simple.prison.mobs.SpawnerUpdater;
import sexy.criss.simple.prison.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

public class Main extends JavaPlugin implements CommandExecutor {
    private SpawnManager spawnManager;
    private static WorldGuardPlugin worldguard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
    static Random r;
    public static int LEVEL_PRICE;
    public static int LEVEL_BLOCK_PRICE;
    public static Location LOCK_CHEST_LOCATION;
    public static Main plugin;
    public static String LVLUP_MENU_NAME;
    public static String UPGRADE_MENU_NAME;
    public static String GIVE_MENU_NAME;
    public static String FACTION_MENU_NAME;
    public static String MINES_MENU_NAME;
    public static String STATS_MENU_NAME;
    public static String STRENGHT_MENU_NAME;
    public static String AGILITY_MENU_NAME;
    public static String FORTUNE_MENU_NAME;
    public static String CURSE_MENU_NAME;
    public static ItemStack KEY;
    public static String SPIDER_NAME;
    public static int SPIDER_DAMAGE;
    public static double SPIDER_MONEY;
    public static double SPIDER_SPEED;
    public static int SPIDER_JUMPDELAY;
    public static int SPIDER_HEALTH;
    public static String SKELETON_NAME;
    public static int SKELETON_DAMAGE;
    public static int SKELETON_KNOCKBACK;
    public static int SKELETON_HEALTH;
    public static int SKELETON_SHOOTDELAY;
    public static String RAT_NAME;
    public static int RAT_HEALTH;
    public static int RAT_DAMAGE;
    public static double RAT_SPEED;
    public static String ZOMBIE_NAME;
    public static int ZOMBIE_HEALTH;
    public static int ZOMBIE_DAMAGE;
    public static double ZOMBIE_SPEED;
    public static String ZOMBIEGIANT_NAME;
    public static int ZOMBIEGIANT_HEALTH;
    public static int ZOMBIEGIANT_DAMAGE;
    public static double ZOMBIEGIANT_SPEED;
    public static int ZOMBIEGIANT_MONEY;
    public static String BIGSLIME_NAME;
    public static int BIGSLIME_HEALTH;
    public static int BIGSLIME_DAMAGE;
    public static double BIGSLIME_SPEED;
    public static int BIGSLIME_MONEY;
    public static int TINYSLIME_HEALTH;
    public static int TINYSLIME_DAMAGE;
    public static String TINYSLIME_NAME;
    public static Location CITY_LOCATION;

    public static FileConfiguration config;
    public static FileConfiguration items_storage;
    public static FileConfiguration players_storage;
    public static FileConfiguration mines_storage;
    public static FileConfiguration factions_storage;
    public static FileConfiguration spawners_storage;
    public static FileConfiguration blocks_storage;
    public static FileConfiguration shop_storage;
    public static FileConfiguration price_storage;

    public static boolean saveEnabled;
    public static Set<Player> chest_setters;
    public static Map<ItemStack, Integer> giantItems;
    public static Map<ItemStack, Integer> slimeItems;
    public static List<String> startItems;
    public static Map<ItemStack, Integer> chestItems;
    public static Map<ItemStack, Integer> spiderItems;

    static {
        LVLUP_MENU_NAME = ChatColor.GREEN + "&c&lLEVEL &8│ Главная";
        UPGRADE_MENU_NAME = ChatColor.GREEN + "&c&lUPGRADE &8│ Главная";
        GIVE_MENU_NAME = ChatColor.GREEN + "&c&lPRISONITEMS &8│ Главная";
        FACTION_MENU_NAME = Utils.f("&c&lFACTION &8│ Главная");
        MINES_MENU_NAME = Utils.f("&c&lMINES &8│ Главная");
        STATS_MENU_NAME = Utils.f("&c&lSKILLS &8│ Главная");
        STRENGHT_MENU_NAME = Utils.f("&c&lSKILLS &8│ Сила");
        AGILITY_MENU_NAME = Utils.f("&c&lSKILLS &8│ Проворотство");
        FORTUNE_MENU_NAME = Utils.f("&c&lSKILLS &8│ Богатство");
        CURSE_MENU_NAME = Utils.f("&c&lSKILLS &8│ Бедствие");
        r = new Random();
        saveEnabled = true;
        chest_setters = new HashSet();
        giantItems = new HashMap();
        slimeItems = new HashMap();
        startItems = new ArrayList();
        chestItems = new HashMap();
        spiderItems = new HashMap();
    }

    public static DonateMines donate_mines;

    Thread saver = new SavePlayersThread();

    public static Main getInstance() {
        return plugin;
    }

    private static void loadItems() {
        if (items_storage.getKeys(false).size() > 0)
            items_storage.getKeys(false).forEach(s -> new PrisonItem(s, items_storage.getConfigurationSection(s)));
    }

    private static void loadMines() {
        PrisonMine.MineUtils.init();
    }

    private static void loadSpawners() {

        for (String cSpawn : spawners_storage.getKeys(false)) {
            ConfigurationSection path = spawners_storage.getConfigurationSection(cSpawn);
            (new Spawner(Utils.getLocation(path), EntityTypes.valueOf(path.getString("type")), path.getInt("interval"))).update();
        }

        getInstance().getLogger().log(Level.INFO, "Loaded " + Spawner.spawners.size() + " mob spawners");
    }

    private static void loadFactions() {
        if (factions_storage.getKeys(false).size() > 0) {

            for (String faction_id : factions_storage.getKeys(false)) {
                new Faction(faction_id, factions_storage.getConfigurationSection(faction_id));
                System.out.println("Loading faction " + faction_id);
            }
        }
    }

    public static WorldGuardPlugin getWorldGuard() {
        return worldguard;
    }

    private static void loadConfig() {
        LEVEL_PRICE = config.getInt("level-price");
        LEVEL_BLOCK_PRICE = config.getInt("blocks-for-level");

        if (config.contains("start-items")) startItems = config.getStringList("start-items");

        ConfigurationSection skeleton = config.getConfigurationSection("mobs.skeleton");
        SKELETON_NAME = Utils.f(skeleton.getString("name"));
        SKELETON_DAMAGE = skeleton.getInt("damage");
        SKELETON_HEALTH = skeleton.getInt("health");
        SKELETON_KNOCKBACK = skeleton.getInt("knockback");
        SKELETON_SHOOTDELAY = skeleton.getInt("shoot-delay");
        ConfigurationSection spider = config.getConfigurationSection("mobs.spider");
        SPIDER_NAME = Utils.f(spider.getString("name"));
        SPIDER_DAMAGE = spider.getInt("damage");
        SPIDER_MONEY = spider.getDouble("money");
        SPIDER_SPEED = spider.getDouble("speed");
        SPIDER_JUMPDELAY = spider.getInt("jumpdelay");
        SPIDER_HEALTH = spider.getInt("health");
        ConfigurationSection rat = config.getConfigurationSection("mobs.rat");
        RAT_NAME = Utils.f(rat.getString("name"));
        RAT_DAMAGE = rat.getInt("damage");
        RAT_HEALTH = rat.getInt("health");
        RAT_SPEED = rat.getDouble("speed");
        ConfigurationSection zombie = config.getConfigurationSection("mobs.zombie");
        ZOMBIE_NAME = Utils.f(zombie.getString("name"));
        ZOMBIE_DAMAGE = zombie.getInt("damage");
        ZOMBIE_HEALTH = zombie.getInt("health");
        ZOMBIE_SPEED = zombie.getDouble("speed");
        ConfigurationSection zombiegiant = config.getConfigurationSection("mobs.zombiegiant");
        ZOMBIEGIANT_NAME = Utils.f(zombiegiant.getString("name"));
        ZOMBIEGIANT_DAMAGE = zombiegiant.getInt("damage");
        ZOMBIEGIANT_HEALTH = zombiegiant.getInt("health");
        ZOMBIEGIANT_SPEED = zombiegiant.getDouble("speed");
        ZOMBIEGIANT_MONEY = zombiegiant.getInt("money");
        ConfigurationSection bigslime = config.getConfigurationSection("mobs.bigslime");
        BIGSLIME_NAME = Utils.f(bigslime.getString("name"));
        BIGSLIME_DAMAGE = bigslime.getInt("damage");
        BIGSLIME_HEALTH = bigslime.getInt("health");
        BIGSLIME_SPEED = bigslime.getDouble("speed");
        BIGSLIME_MONEY = bigslime.getInt("money");
        ConfigurationSection tinyslime = config.getConfigurationSection("mobs.tinyslime");
        TINYSLIME_NAME = Utils.f(tinyslime.getString("name"));
        TINYSLIME_DAMAGE = tinyslime.getInt("damage");
        TINYSLIME_HEALTH = tinyslime.getInt("health");
        if (config.contains("city")) {
            ConfigurationSection city = config.getConfigurationSection("city");
            CITY_LOCATION = new Location(Bukkit.getWorld(city.getString("world")), (double) city.getInt("x"), (double) city.getInt("y"), (double) city.getInt("z"));
        }

    }

    private static void loadLockedChest() {
        ConfigurationSection locked_chest = config.getConfigurationSection("locked-chest");
        if (locked_chest.contains("location")) {
            ConfigurationSection lcl = locked_chest.getConfigurationSection("location");
            LOCK_CHEST_LOCATION = new Location(Bukkit.getWorld(lcl.getString("world")), (double) lcl.getInt("x"), (double) lcl.getInt("y"), (double) lcl.getInt("z"));
            getInstance().getLogger().log(Level.INFO, "Locket chest location found and loaded.");
        } else {
            getInstance().getLogger().log(Level.INFO, "Locket chest location not found. You need to set it with /setlockchest");
        }

        if (!locked_chest.contains("items") && !locked_chest.contains("custom_items")) {
            getInstance().getLogger().log(Level.INFO, "No items for locked chest. Add some in config.");
        } else {
            getInstance().getLogger().log(Level.INFO, "Locket chest items founded.");
            ConfigurationSection custom_items;
            String strItem;
            Iterator var4;
            if (locked_chest.contains("items")) {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded(Common Items).");
                custom_items = locked_chest.getConfigurationSection("items");
                var4 = custom_items.getKeys(false).iterator();

                while (var4.hasNext()) {
                    strItem = (String) var4.next();
                    getInstance().getLogger().log(Level.INFO, "Locket chest item found: " + strItem);
                    ConfigurationSection item = custom_items.getConfigurationSection(strItem);
                    ItemStack is = new ItemStack(Material.getMaterial(strItem));
                    if (item.contains("name")) {
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(Utils.f(item.getString("name")));
                        is.setItemMeta(im);
                    }

                    if (item.contains("data")) {
                        is.setDurability((short) item.getInt("data"));
                    }

                    if (item.contains("amount")) {
                        is.setAmount(item.getInt("amount"));
                    }

                    if (item.contains("chance")) {
                        chestItems.put(is, item.getInt("chance"));
                    } else {
                        getInstance().getLogger().log(Level.INFO, "NO CHANCE FOR ITEM " + strItem);
                    }
                }
            }

            if (locked_chest.contains("custom_items")) {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded(CUSTOM Items).");
                custom_items = locked_chest.getConfigurationSection("custom_items");
                var4 = custom_items.getKeys(false).iterator();

                while (var4.hasNext()) {
                    strItem = (String) var4.next();
                    if (PrisonItem.isAvailable(strItem)) {
                        chestItems.put(PrisonItem.getPrisonItem(strItem).getUsableItem(), custom_items.getInt(strItem));
                    } else {
                        getInstance().getLogger().log(Level.INFO, "Custom item " + strItem + " not found.");
                    }
                }
            }
        }

    }

    private static void loadSpiderDrop() {
        getInstance().getLogger().log(Level.INFO, "Loading spider drop");
        if (config.contains("mobs.spider.drop")) {
            ConfigurationSection spider_drop = config.getConfigurationSection("mobs.spider.drop");
            if (!spider_drop.contains("items") && !spider_drop.contains("custom_items")) {
                getInstance().getLogger().log(Level.INFO, "No items for spider. Add some in config.");
            } else {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded.");
                ConfigurationSection custom_items;
                String strItem;
                Iterator var4;
                if (spider_drop.contains("items")) {
                    getInstance().getLogger().log(Level.INFO, "Spider drop items founded(Common Items).");
                    custom_items = spider_drop.getConfigurationSection("items");
                    var4 = custom_items.getKeys(false).iterator();

                    while (var4.hasNext()) {
                        strItem = (String) var4.next();
                        getInstance().getLogger().log(Level.INFO, "Locket chest item found: " + strItem);
                        ConfigurationSection item = custom_items.getConfigurationSection(strItem);
                        ItemStack is = new ItemStack(Material.getMaterial(strItem));
                        if (item.contains("name")) {
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(Utils.f(item.getString("name")));
                            is.setItemMeta(im);
                        }

                        if (item.contains("data")) {
                            is.setDurability((short) item.getInt("data"));
                        }

                        if (item.contains("amount")) {
                            is.setAmount(item.getInt("amount"));
                        }

                        if (item.contains("chance")) {
                            spiderItems.put(is, item.getInt("chance"));
                        } else {
                            getInstance().getLogger().log(Level.INFO, "NO CHANCE FOR ITEM " + strItem);
                        }
                    }
                }

                if (spider_drop.contains("custom_items")) {
                    getInstance().getLogger().log(Level.INFO, "Spider drop items founded(CUSTOM Items).");
                    custom_items = spider_drop.getConfigurationSection("custom_items");
                    var4 = custom_items.getKeys(false).iterator();

                    while (var4.hasNext()) {
                        strItem = (String) var4.next();
                        if (PrisonItem.isAvailable(strItem)) {
                            spiderItems.put(PrisonItem.getPrisonItem(strItem).getUsableItem(), custom_items.getInt(strItem));
                        } else {
                            getInstance().getLogger().log(Level.INFO, "Custom item " + strItem + " not found.");
                        }
                    }
                }
            }
        }

    }

    private static void loadGiantDrop() {
        getInstance().getLogger().log(Level.INFO, "Loading giant drop");
        if (config.contains("mobs.zombiegiant.drop")) {
            ConfigurationSection giant_drop = config.getConfigurationSection("mobs.zombiegiant.drop");
            if (!giant_drop.contains("items") && !giant_drop.contains("custom_items")) {
                getInstance().getLogger().log(Level.INFO, "No items for giant. Add some in config.");
            } else {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded.");
                ConfigurationSection custom_items;
                String strItem;
                Iterator var4;
                if (giant_drop.contains("items")) {
                    getInstance().getLogger().log(Level.INFO, "Giant drop items founded(Common Items).");
                    custom_items = giant_drop.getConfigurationSection("items");
                    var4 = custom_items.getKeys(false).iterator();

                    while (var4.hasNext()) {
                        strItem = (String) var4.next();
                        getInstance().getLogger().log(Level.INFO, "Locket chest item found: " + strItem);
                        ConfigurationSection item = custom_items.getConfigurationSection(strItem);
                        ItemStack is = new ItemStack(Material.getMaterial(strItem));
                        if (item.contains("name")) {
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(Utils.f(item.getString("name")));
                            is.setItemMeta(im);
                        }

                        if (item.contains("data")) {
                            is.setDurability((short) item.getInt("data"));
                        }

                        if (item.contains("amount")) {
                            is.setAmount(item.getInt("amount"));
                        }

                        if (item.contains("chance")) {
                            giantItems.put(is, item.getInt("chance"));
                        } else {
                            getInstance().getLogger().log(Level.INFO, "NO CHANCE FOR ITEM " + strItem);
                        }
                    }
                }

                if (giant_drop.contains("custom_items")) {
                    getInstance().getLogger().log(Level.INFO, "Giant drop items founded(CUSTOM Items).");
                    custom_items = giant_drop.getConfigurationSection("custom_items");
                    var4 = custom_items.getKeys(false).iterator();

                    while (var4.hasNext()) {
                        strItem = (String) var4.next();
                        if (PrisonItem.isAvailable(strItem)) {
                            giantItems.put(PrisonItem.getPrisonItem(strItem).getUsableItem(), custom_items.getInt(strItem));
                        } else {
                            getInstance().getLogger().log(Level.INFO, "Custom item " + strItem + " not found.");
                        }
                    }
                }
            }
        }

    }

    private static void loadBigSlimeDrop() {
        getInstance().getLogger().log(Level.INFO, "Loading slime drop");
        if (config.contains("mobs.bigslime.drop")) {
            ConfigurationSection slime_drop = config.getConfigurationSection("mobs.bigslime.drop");
            if (!slime_drop.contains("items") && !slime_drop.contains("custom_items")) {
                getInstance().getLogger().log(Level.INFO, "No items for slime. Add some in config.");
            } else {
                getInstance().getLogger().log(Level.INFO, "Locket chest items founded.");
                ConfigurationSection custom_items;
                String strItem;
                Iterator var4;
                if (slime_drop.contains("items")) {
                    getInstance().getLogger().log(Level.INFO, "Slime drop items founded(Common Items).");
                    custom_items = slime_drop.getConfigurationSection("items");
                    var4 = custom_items.getKeys(false).iterator();

                    while (var4.hasNext()) {
                        strItem = (String) var4.next();
                        getInstance().getLogger().log(Level.INFO, "Locket chest item found: " + strItem);
                        ConfigurationSection item = custom_items.getConfigurationSection(strItem);
                        ItemStack is = new ItemStack(Material.getMaterial(strItem));
                        if (item.contains("name")) {
                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(Utils.f(item.getString("name")));
                            is.setItemMeta(im);
                        }

                        if (item.contains("data")) {
                            is.setDurability((short) item.getInt("data"));
                        }

                        if (item.contains("amount")) {
                            is.setAmount(item.getInt("amount"));
                        }

                        if (item.contains("chance")) {
                            slimeItems.put(is, item.getInt("chance"));
                        } else {
                            getInstance().getLogger().log(Level.INFO, "NO CHANCE FOR ITEM " + strItem);
                        }
                    }
                }

                if (slime_drop.contains("custom_items")) {
                    getInstance().getLogger().log(Level.INFO, "Slime drop items founded(CUSTOM Items).");
                    custom_items = slime_drop.getConfigurationSection("custom_items");
                    var4 = custom_items.getKeys(false).iterator();

                    while (var4.hasNext()) {
                        strItem = (String) var4.next();
                        if (PrisonItem.isAvailable(strItem)) {
                            slimeItems.put(PrisonItem.getPrisonItem(strItem).getUsableItem(), custom_items.getInt(strItem));
                        } else {
                            getInstance().getLogger().log(Level.INFO, "Custom item " + strItem + " not found.");
                        }
                    }
                }
            }
        }

    }

    public static void saveConfig(FileConfiguration config, String fileName) {
        try {
            config.save(new File(getInstance().getDataFolder(), fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void openKeyChestFor(Player player) {
        Inventory inv = Bukkit.createInventory(player, InventoryType.CHEST);
        Random rand = new Random();
        Set<ItemStack> drop = Sets.newHashSet();
        if(chestItems.size() > 0) {
            chestItems.forEach((a, c) -> {
                if(drop.size() >= 3) return;
                if(rand.nextInt(100) < c) drop.add(a);
            });
        }

        drop.forEach(itemStack -> inv.setItem(rand.nextInt(inv.getSize() - 1), itemStack));

        PrisonPlayer.getPrisonPlayer(player).giveMoney((rand.nextInt(19) + 1));
        PrisonPlayer.getPrisonPlayer(player).giveExp(10, 1);
        player.playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 1,1);
        player.openInventory(inv);
    }

    public static void dropSpiderDropOn(Location loc) {
        Random rand = new Random();
        Set<ItemStack> drop = new HashSet();
        Iterator var4;
        if (spiderItems.size() > 0) {
            var4 = spiderItems.entrySet().iterator();

            while (var4.hasNext()) {
                Entry<ItemStack, Integer> cEntry = (Entry) var4.next();
                if (drop.size() >= 2) {
                    break;
                }

                if (rand.nextInt(100) < cEntry.getValue()) {
                    drop.add(new ItemStack(cEntry.getKey()));
                }
            }
        }

        var4 = drop.iterator();

        while (var4.hasNext()) {
            ItemStack is = (ItemStack) var4.next();
            loc.getWorld().dropItem(loc, is);
        }

    }

    public static void dropGiantDropOn(Location loc) {
        Random rand = new Random();
        Set<ItemStack> drop = new HashSet();
        Iterator var4;
        if (giantItems.size() > 0) {
            var4 = giantItems.entrySet().iterator();

            while (var4.hasNext()) {
                Entry<ItemStack, Integer> cEntry = (Entry) var4.next();
                if (drop.size() >= 2) {
                    break;
                }

                if (rand.nextInt(100) < cEntry.getValue()) {
                    drop.add(new ItemStack(cEntry.getKey()));
                }
            }
        }

        var4 = drop.iterator();

        while (var4.hasNext()) {
            ItemStack is = (ItemStack) var4.next();
            loc.getWorld().dropItem(loc, is);
        }

    }

    public static void dropBigSlimeDropOn(Location loc) {
        Random rand = new Random();
        Set<ItemStack> drop = new HashSet();
        Iterator var4;
        if (slimeItems.size() > 0) {
            var4 = slimeItems.entrySet().iterator();

            while (var4.hasNext()) {
                Entry<ItemStack, Integer> cEntry = (Entry) var4.next();
                if (drop.size() >= 2) {
                    break;
                }

                if (rand.nextInt(100) < cEntry.getValue()) {
                    drop.add(new ItemStack(cEntry.getKey()));
                }
            }
        }

        var4 = drop.iterator();

        while (var4.hasNext()) {
            ItemStack is = (ItemStack) var4.next();
            loc.getWorld().dropItem(loc, is);
        }

    }

    public void onLoad() {
        plugin = this;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public void onEnable() {
        try {
            this.saveDefaults();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        spawnManager = new SpawnManager();
        spawnManager.setSpawn(Utils.getLocation(getConfig().getConfigurationSection("spawn")));

        this.hookConfigs();
        this.getServer().getPluginManager().registerEvents(new Events(), this);
        EntityTypes.registerAll();
        loadConfig();
        loadItems();
        loadLockedChest();
        loadMines();
        loadFactions();
        loadSpawners();
        loadSpiderDrop();
        loadGiantDrop();
        loadBigSlimeDrop();

        blocks_storage.getStringList("blocks").forEach(s -> {
            String[] ar = s.split("/");
            String[] l = ar[0].split(":");
            Location loc = new Location(Bukkit.getWorld(l[0]), Double.parseDouble(l[1]), Double.parseDouble(l[2]), Double.parseDouble(l[3]));

            new TimedBlock(loc, Material.getMaterial(ar[1]), Byte.parseByte(ar[2]), Integer.parseInt(ar[3]));
        });

        ConfigurationSection sec = shop_storage.getConfigurationSection("shop");
        ShopManager.display = Utils.f(sec.getString("title", "&a#shop"));


        price_storage.getConfigurationSection("price").getKeys(false).forEach(s -> ShopHandler.price_map.put(new ItemStack(Material.getMaterial(s.contains("-") ? s.split("-")[0] : s), 1, Short.parseShort(s.contains("-") ? s.split("-")[1] : "0")), price_storage.getConfigurationSection("price").getInt(s)));



        // commands
        {
            new AddSpawnerCommand().register();
            new BaseCommand().register();
            new BoardCommand().register();
            new CityCommand().register();
            new DebugCommand().register();
            new FactionCommand().register();
            new FleaveCommand().register();
            new GamemodeCommand().register();
            new MineCommand().register();
            new PrisonbbCommand().register();
            new PrisonCommand().register();
            new PrisonItemsCommand().register();
            new ResetBossesCommand().register();
            new SetBaseCommand().register();
            new SetCityCommand().register();
            new SetCommand().register();
            new SetLockedChestCommand().register();
            new SpawnCommand().register();
            new StartKitCommand().register();
            new StatsCommand().register();
            new TestCommand().register();
            new TimedBlocksCommand().register();
            new UpgradeCommand().register();
            new ShopCommand().register();
        }

        new JoinHandler();
        new InventoryHandler();
        new TagsHandler();
        new TimedBlockHandler();
        new GravityHandler();
        new ShopHandler();
        new GetDamageHandler();
        new WorldChangeHandler();
        new GrassHandler();
        new DonateMinesHandler();
        new MinesHandler();
        new LocationsHandler();
        new VisualChatHandler();

        (new Tempfix()).runTaskTimer(this, 0L, 400L);
        (new SavePlayersTask()).runTaskTimer(this, 0L, 200L);
        (new SpawnerUpdater()).runTaskTimer(this, 20L, 600L);

        try {
            donate_mines = new DonateMines(this);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        this.saver.start();
    }

    public void onDisable() {
        Spawner.spawners.values().stream().filter(s -> s.getCurrent() != null).forEach(s -> s.getCurrent().getBukkitEntity().remove());
        spawnManager.save();
        saveDefaultConfig();
        saveConfig();
        List<String> arr = Lists.newArrayList();
        TimedBlock.timedBlocks_map.forEach((a, b) -> {
            String out = "";
            String l = a.getWorld().getName() + ":" + a.getX() + ":" + a.getY() + ":" + a.getZ();
            out = out.concat(l).concat("/");
            out = out.concat(b.getRegenType().toString().toUpperCase()).concat("/");
            out = out.concat(String.valueOf(b.getData())).concat("/");
            out = out.concat(String.valueOf(b.getInterval()));
            arr.add(out);
        });

        blocks_storage.set("blocks", arr);

        saveConfig(blocks_storage, "blocks.yml");
        saveConfig(shop_storage, "shop.yml");
        saveConfig(players_storage, "players.yml");
        saveConfig(price_storage, "price_storage.yml");
        Bukkit.getScheduler().cancelTasks(this);
        saveEnabled = false;
        System.out.println("Waiting for save-thread stop.");
        System.out.println("This may take ~10 sec. Please wait.");

        try {
            this.saver.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public void hookConfigs() {
        config = this.getConfig();
        players_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "players.yml"));
        items_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "items.yml"));
        mines_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "mines.yml"));
        factions_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "factions.yml"));
        spawners_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "spawners.yml"));
        blocks_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "blocks.yml"));
        shop_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "shop.yml"));
        price_storage = ConfigUtils.loadConfig(new File(getInstance().getDataFolder(), "price_storage.yml"));
    }

    private void saveDefaults() throws IOException {
        this.saveDefaultConfig();
        File players = new File(this.getDataFolder(), "players.yml");
        File items = new File(this.getDataFolder(), "items.yml");
        File mines = new File(this.getDataFolder(), "mines.yml");
        File factions = new File(this.getDataFolder(), "factions.yml");

        if (!players.exists()) players.createNewFile();
        if (!items.exists()) items.createNewFile();
        if (!mines.exists()) mines.createNewFile();
        if (!factions.exists()) factions.createNewFile();
    }

    public void sendAllTypes(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "Доступные типы:");
        Arrays.asList(EntityTypes.values()).forEach(e -> sender.sendMessage(Utils.f("&c" + e.name() + " &7- " + e.getName())));
    }

    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
