//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import sexy.criss.simple.prison.utils.Utils;
import sexy.criss.simple.prison.mobs.EntityTypes;

public class PrisonPlayer {
    private static Map<String, PrisonPlayer> players = new HashMap();
    String name;
    private int money;
    int level;
    Map<Material, Integer> block_log;
    Map<EntityTypes, Integer> mob_log;
    Map<String, Integer> passives;
    Objective obj;
    int kills;
    int deaths;
    boolean free;
    int exp;
    double keysMultiplier;
    double moneyMultiplier;
    int blocksMultiplier;
    boolean cellarAccess;
    boolean mushroomAccess;
    boolean sectorAccess;
    boolean magicAccess;
    long lastkit;
    long lastReward;
    Faction faction;
    Scoreboard board;
    int totalblocks;
    private String lastDamager;
    private long lastDamagerHitTime = -1L;
    private boolean autoSell = false;

    public void setLastDamager(String lastDamager) {
        this.lastDamager = lastDamager;
        this.lastDamagerHitTime = System.currentTimeMillis() / 1000L;
    }

    public String getLastDamager() {
        return System.currentTimeMillis() / 1000L - this.lastDamagerHitTime <= 15L ? this.lastDamager : null;
    }

    public int getNextLevelBlockPrice() {
        int price = Main.LEVEL_BLOCK_PRICE;

        for(int i = 1; i < this.level; ++i) price *= 2;

        return price;
    }

    public int getNextLevelMoneyPrice() {
        int price = Main.LEVEL_PRICE;

        for(int i = 1; i < this.level; i++) price *= 2;

        return price;
    }

    public PrisonPlayer(String name) {
        this.name = name;
        this.money = 0;
        this.level = 1;
        this.kills = 0;
        this.deaths = 0;
        this.exp = 0;
        this.keysMultiplier = 0.0D;
        this.blocksMultiplier = 1;
        this.moneyMultiplier = 1.0;
        this.cellarAccess = false;
        this.mushroomAccess = false;
        this.sectorAccess = false;
        this.magicAccess = false;
        this.block_log = new HashMap();
        this.mob_log = new HashMap();
        this.faction = null;
        players.put(name, this);
    }

    public PrisonPlayer(String name, ConfigurationSection info) {
        this.name = name;
        this.money = info.getInt("money");
        this.level = info.getInt("level");
        this.cellarAccess = info.getBoolean("CellarAccess");
        this.magicAccess = info.getBoolean("MagicAccess");
        this.mushroomAccess = info.getBoolean("MushroomAccess");
        this.sectorAccess = info.getBoolean("SectorAccess");
        this.exp = info.getInt("exp");
        this.moneyMultiplier = info.getDouble("boosters.money");
        this.blocksMultiplier = info.getInt("boosters.blocks");
        this.keysMultiplier = info.getDouble("boosters.keys");
        this.kills = info.getInt("kills");
        this.deaths = info.getInt("deaths");
        this.totalblocks = info.getInt("total-blocks");
        this.faction = Faction.getFaction(info.getString("faction"));
        if (info.contains("free")) {
            this.free = info.getBoolean("free");
        }

        this.block_log = new HashMap();
        ConfigurationSection bl;
        String type;
        Iterator var5;
        if (info.contains("block-log")) {
            bl = info.getConfigurationSection("block-log");
            if (bl.getKeys(false).size() > 0) {
                var5 = bl.getKeys(false).iterator();

                while(var5.hasNext()) {
                    type = (String)var5.next();
                    this.block_log.put(Material.getMaterial(type), bl.getInt(type));
                }
            }
        }

        this.mob_log = new HashMap();
        if (info.contains("mob-log")) {
            bl = info.getConfigurationSection("mob-log");
            if (bl.getKeys(false).size() > 0) {
                var5 = bl.getKeys(false).iterator();

                while(var5.hasNext()) {
                    type = (String)var5.next();
                    this.mob_log.put(EntityTypes.valueOf(type), bl.getInt(type));
                }
            }
        }

        this.passives = new HashMap();
        if (info.contains("passives")) {
            bl = info.getConfigurationSection("passives");

            if (!bl.contains("Fortune")) bl.set("Fortune", 0);
            if (!bl.contains("Curse")) bl.set("Curse", 0);
            if (!bl.contains("Lucky")) bl.set("Lucky", 0);
            if (!bl.contains("Strength")) bl.set("Strength", 0);
            if (!bl.contains("Agility")) bl.set("Agility", 0);


            if (bl.getKeys(false).size() > 0) {
                ConfigurationSection finalBl = bl;
                bl.getKeys(false).forEach(s -> this.passives.put(s, finalBl.getInt(s)));
            }
        } else {
            this.passives.put("Strength", 0);
            this.passives.put("Agility", 0);
            this.passives.put("Lucky", 0);
            this.passives.put("Fortune", 0);
            this.passives.put("Curse", 0);
        }

        players.put(name, this);
    }

    public void save() {
        ConfigurationSection cPlayer;
        if (Main.players_storage.contains(this.name)) {
            cPlayer = Main.players_storage.getConfigurationSection(this.name);
        } else {
            cPlayer = Main.players_storage.createSection(this.name);
        }

        cPlayer.set("money", this.money);
        cPlayer.set("level", this.level);
        cPlayer.set("exp", this.exp);

        ConfigurationSection b;
        if(cPlayer.contains("boosters")) b = cPlayer.getConfigurationSection("boosters");
        else b = cPlayer.createSection("boosters");

        b.set("money", this.moneyMultiplier);
        b.set("blocks", this.blocksMultiplier);
        b.set("keys", this.keysMultiplier);

        cPlayer.set("kills", this.kills);
        cPlayer.set("deaths", this.deaths);
        cPlayer.set("CellarAccess", this.cellarAccess);
        cPlayer.set("MushroomAccess", this.mushroomAccess);
        cPlayer.set("SectorAccess", this.sectorAccess);
        cPlayer.set("MagicAccess", this.magicAccess);
        cPlayer.set("total-blocks", this.totalblocks);
        cPlayer.set("free", this.free);
        ConfigurationSection bLog;
        Entry entry;
        Iterator var4;
        if (this.block_log != null && this.block_log.size() > 0) {
            if (cPlayer.contains("block-log")) {
                bLog = cPlayer.getConfigurationSection("block-log");
            } else {
                bLog = cPlayer.createSection("block-log");
            }

            if (this.block_log.size() > 0) {
                var4 = this.block_log.entrySet().iterator();

                while(var4.hasNext()) {
                    entry = (Entry)var4.next();
                    bLog.set(entry.getKey().toString(), entry.getValue());
                }
            }
        }

        if (this.mob_log != null && this.mob_log.size() > 0) {
            if (cPlayer.contains("mob-log")) {
                bLog = cPlayer.getConfigurationSection("mob-log");
            } else {
                bLog = cPlayer.createSection("mob-log");
            }

            if (this.mob_log.size() > 0) {
                var4 = this.mob_log.entrySet().iterator();

                while(var4.hasNext()) {
                    entry = (Entry)var4.next();
                    bLog.set(entry.getKey().toString(), entry.getValue());
                }
            }
        }

        if (this.passives != null && this.passives.size() > 0) {
            if (cPlayer.contains("passives")) {
                bLog = cPlayer.getConfigurationSection("passives");
            } else {
                bLog = cPlayer.createSection("passives");
            }

            if (this.passives.size() > 0) {
                var4 = this.passives.entrySet().iterator();

                while(var4.hasNext()) {
                    entry = (Entry)var4.next();
                    bLog.set(((String)entry.getKey()), entry.getValue());
                }
            }
        } else {
            if (cPlayer.contains("passives")) {
                bLog = cPlayer.getConfigurationSection("passives");
            } else {
                bLog = cPlayer.createSection("passives");
            }

            bLog.set("Strength", 0);
            bLog.set("Agility", 0);
            bLog.set("Needs", 0);
            bLog.set("Fortune", 0);
            bLog.set("Curse", 0);
        }

    }

    public static void loadPrisonPlayer(Player player) {
        String name = player.getName();
        if (Main.players_storage.contains(player.getName())) {
            new PrisonPlayer(name, Main.players_storage.getConfigurationSection(name));
        } else {
            PrisonPlayer pp = new PrisonPlayer(name);
            (new BukkitRunnable() {
                public void run() {
                    PrisonPlayer.giveStartKit(player);
                }
            }).runTaskLater(Main.getInstance(), 20L);
            pp.save();
        }

    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public static void giveStartKit(Player player) {
        Inventory inv = player.getInventory();
        if (Main.startItems.size() > 0 && PrisonItem.items.size() > 0)
            Main.startItems.forEach(s -> inv.addItem(PrisonItem.getPrisonItem(s).getUsableItem()));

        getPrisonPlayer(player).lastkit = System.currentTimeMillis() / 1000L / 60L;
    }

    public static PrisonPlayer getPrisonPlayer(Player player) {
        return players.get(player.getName());
    }

    public int getLevel() {
        return this.level;
    }

    public void levelUp() {
        if(getExp() >= getNextLevelBlockPrice()) {
            int taked = 0;
            final int take = getNextLevelBlockPrice();
            this.level++;
            while(taked < take) {
                taked++;
                this.exp--;
            }
            this.save();
        }
    }

    public void setLevel(int level) {
        this.level = level;
        this.save();
    }

    public int getTotalBlocks() {
        return totalblocks;
    }

    public double getMoneyMultiplier() {
        return this.moneyMultiplier;
    }

    public int getBlocksMultiplier() {
        return this.blocksMultiplier;
    }

    public double getKeysMultiplier() {
        return this.keysMultiplier;
    }

    public void setMoneyMultiplier(double x) {
        this.moneyMultiplier = x;
    }

    public void setBlocksMultiplier(int x) {
        this.blocksMultiplier = x;
    }

    public void setKeysMultiplier(double x) {
        this.keysMultiplier = x;
    }

    public void grantCellarAccess() {
        this.cellarAccess = true;
    }
    public void grantMushroomAccess() { this.mushroomAccess = true; }
    public void grantSectorAccess() { this.sectorAccess = true; }
    public void grantMagicAccess() { this.magicAccess = true; }

    public boolean hasCellarAccess() {
        return this.cellarAccess;
    }

    public boolean hasMushroomAccess() {
        return mushroomAccess;
    }

    public boolean hasSectorAccess() {
        return sectorAccess;
    }

    public boolean hasMagicAccess() {
        return magicAccess;
    }

    public int getBalance() {
        return this.money;
    }

    public boolean takeMoney(int money) {
        if (this.hasMoney(money)) {
            this.money -= money;
            Bukkit.getPlayer(this.name).sendMessage(ChatColor.RED + "С вашего счета было списано " + ChatColor.DARK_RED + money + "$");
            return true;
        }
        return false;
    }

    public void giveMoney(int money) {
        if (Bukkit.getPlayer(this.name) != null) {
            try {
                money = ((int) (money * moneyMultiplier));
                Bukkit.getPlayer(this.name).sendMessage(ChatColor.GREEN + "На ваш счет было зачисленно " + ChatColor.GREEN + money + "$");
                this.money += money;
            } catch (NullPointerException ignored) {
            }
        }
    }

    public void giveExp(int exp, int min) {
        int r = (new Random().nextInt(exp) + min);
        giveExp(r);
    }

    public void giveExp(int exp) {
        this.exp += exp;
        Player p = Bukkit.getPlayer(name);
        try {
            String msg = "&6+%d EXP";
            p.sendTitle("", Utils.f(msg, exp));
        }catch (Exception ignored) {
        }
    }

    public boolean hasMoney(int money) {
        return this.money >= money;
    }

    public static Collection<PrisonPlayer> getAll() {
        return players.values();
    }

    public void addBlockDig(Material material) {
        if (this.block_log.containsKey(material)) {
            this.block_log.put(material, this.block_log.get(material) + blocksMultiplier);
        } else {
            this.block_log.put(material, blocksMultiplier);
        }

    }

    public int getBlockDigsCount(Material material) {
        return this.block_log.getOrDefault(material, 0);
    }

    public void addMobKill(EntityTypes type) {
        if (this.mob_log.containsKey(type)) {
            this.mob_log.put(type, this.mob_log.get(type) + 1);
        } else {
            this.mob_log.put(type, 1);
        }
    }

    public int getExp() {
        return this.exp;
    }

    public int getMobKillCount(EntityTypes type) {
        return this.mob_log.getOrDefault(type, 0);
    }

    public int getKills() {
        return this.kills;
    }

    public Map<String, Integer> getPassives() {
        return this.passives;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getAdditionalDamage() {
        return this.level / 5;
    }

    public Faction getFaction() {
        return this.faction;
    }

    public String getName() {
        return this.name;
    }

    public void setTotalBlocks(int totalBlocks) {
        this.totalblocks = totalBlocks;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        Main.players_storage.getConfigurationSection(this.name).set("faction", faction != null ? faction.getId() : null);
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean hasFaction() {
        return this.faction != null;
    }

    public boolean isFree() {
        return this.free;
    }

    public long getLastKit() {
        return lastkit;
    }

    public Map<Material, Integer> getBlock_log() {
        return block_log;
    }

    public Map<EntityTypes, Integer> getMob_log() {
        return mob_log;
    }

    public void toggleAutoSell() {
        this.autoSell = !autoSell;
    }

    public boolean isAutoSell() {
        return this.autoSell;
    }

    public void toggleAutoSell(@NotNull boolean v) {
        this.autoSell = v;
    }

}
