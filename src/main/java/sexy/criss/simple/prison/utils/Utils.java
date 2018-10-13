package sexy.criss.simple.prison.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import packetwrapper.WrapperPlayServerPlayerInfo;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.handlers.ShopHandler;
import sexy.criss.simple.prison.wrapper.particle.ParticleEffect;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class Utils {

   private static final char[] SUFFIXES = {'K', 'M', 'G', 'T', 'P', 'E' };

   @NotNull
   private static String f(String s) {
      return s == null ? "" : ChatColor.translateAlternateColorCodes('&', s);
   }


   @NotNull
   public static List<String> f(@NotNull List<String> s) {
      List<String> temp = Lists.newArrayList();
      s.forEach(c -> temp.add(f(c)));
      return temp;
   }

   @NotNull
   public static long getDate() {
      return 0;
   }

   @NotNull
   public static String strip(@NotNull String s) {
      return ChatColor.stripColor(s);
   }

   @NotNull
   public static String f(@NotNull String s, @NotNull Object... args) {
      try {
         return f(String.format(s, args));
      } catch (Exception ex) {
         return "";
      }
   }

   @NotNull
   public static String format(@NotNull long number) {
      if(number < 1000) return String.valueOf(number);

      final String string = String.valueOf(number);
      final int magnitude = (string.length() - 1) / 3;
      final int digits = (string.length() - 1) % 3 + 1;

      char[] value = new char[4];
      for(int i = 0; i < digits; i++) value[i] = string.charAt(i);
      int valueLength = digits;

      if(digits == 1 && string.charAt(1) != '0') {
         value[valueLength++] = '.';
         value[valueLength++] = string.charAt(1);
      }

      value[valueLength++] = SUFFIXES[magnitude - 1];
      return new String(value, 0, valueLength);
   }

   @NotNull
   public static String getProgressBar(@NotNull int current, @NotNull int max, @NotNull int totalBars, @NotNull String symbol, @NotNull String completedColor, @NotNull String notCompletedColor) {
      float percent = (float) current / max;
      int progressBars = (int) (totalBars * percent);
      int leftOver = (totalBars - progressBars);

      StringBuilder sb = new StringBuilder();

      sb.append(f(completedColor));
      for (int i = 0; i < progressBars; i++) sb.append(symbol);

      sb.append(f(notCompletedColor));
      for (int i = 0; i < leftOver; i++) sb.append(symbol);

      return sb.toString();
   }

   @NotNull
   public static String getProgressBar(@NotNull int current, @NotNull int max, @NotNull int totalBars, @NotNull String completeSymbol, @NotNull String notCompleteSymbol, @NotNull String completedColor, @NotNull String notCompletedColor) {
      float percent = (float) current / max;
      int progressBars = (int) (totalBars * percent);
      int leftOver = (totalBars - progressBars);

      StringBuilder sb = new StringBuilder();

      sb.append(f(completedColor));
      for (int i = 0; i < progressBars; i++) sb.append(completeSymbol);

      sb.append(f(notCompletedColor));
      for (int i = 0; i < leftOver; i++) sb.append(notCompleteSymbol);

      String out = sb.toString();
      int a = completedColor.length() + notCompletedColor.length();
      if(out.length() + a > totalBars) out = out.substring(0, totalBars);

      return out;
   }

   @NotNull
   public static Map<String, Integer> calculatePercents(@NotNull Map<String, Integer> attackers, @NotNull int totalDamage) {
      Map<String, Integer> damagePercents = Maps.newHashMap();
      int totalDamagePercents =  totalDamage / 100;

      attackers.forEach((s, integer) -> damagePercents.put(s,  (integer / totalDamagePercents)));
      return damagePercents;
   }

   @NotNull
   public static Location getLocation(@NotNull ConfigurationSection sec) {
      String world = sec.getString("world", "World");
      double x = sec.getDouble("x", 0);
      double y = sec.getDouble("y", 0);
      double z = sec.getDouble("z", 0);
      float yaw = Float.parseFloat(String.valueOf(sec.getDouble("yaw", 0.0f)));
      float pitch = Float.parseFloat(String.valueOf(sec.getDouble("pitch", 0.0f)));

      return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
   }

   @NotNull
   public static boolean contains(@NotNull String s, @NotNull Set<String> set) {
      AtomicBoolean v = new AtomicBoolean(false);
      set.forEach(c -> {
         if(!v.get()) return;
         if((!v.get()) && (Utils.strip(s).contains(c))) v.set(true);
      });
      return v.get();
   }

   public static void bossInfo(@NotNull String bossName, @NotNull Map<String, Integer> map) {
      List<String> d = Lists.newArrayList("", Utils.f("&7Босс &r%s&7 был повержен. Нападавшие получили его ценные сокровища!", bossName), "&7В битве участвовали:");
      map.forEach((s, v) -> d.add("  &9" + s + " = " + v + "%"));
      d.add("");
      Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(Utils.f(d).toArray(new String[0])));
   }

   public static void sendActionBarMessage(@NotNull Player p, @NotNull String message) {
      PacketPlayOutChat bar = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + f(message) + "\"}"), (byte)2);
      ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
   }

   @NotNull
   public static Set<Player> getAllPlayers() {
      return Sets.newHashSet(Bukkit.getOnlinePlayers());
   }

   public static void setPrefix(@NotNull Player player, @NotNull String name) {
      PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromPlayer(player), 1,
              EnumWrappers.NativeGameMode.SURVIVAL,
              WrappedChatComponent.fromText(name));
      WrapperPlayServerPlayerInfo wpspi = new WrapperPlayServerPlayerInfo();
      wpspi.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
      wpspi.setData(Collections.singletonList(pid));

      getAllPlayers().stream().filter(p -> !p.equals(player)).forEach(p -> {
         p.hidePlayer(player);
         wpspi.sendPacket(player);
      });

      ProtocolLibrary.getProtocolManager().addPacketListener(
              new PacketAdapter(Main.getInstance(), PacketType.Play.Server.PLAYER_INFO) {
                 @Override
                 public void onPacketSending(PacketEvent event) {
                    if(event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER) return;

                    PlayerInfoData pid = event.getPacket().getPlayerInfoDataLists().read(0).get(0);
                    if(!pid.getProfile().getName().toLowerCase().equals(player.getName())) return;

                    PlayerInfoData newPid = new PlayerInfoData(pid.getProfile().withName("HEAD_NAME"), pid.getPing(), pid.getGameMode(), WrappedChatComponent.fromText(name));
                    event.getPacket().getPlayerInfoDataLists().write(0, Collections.singletonList(newPid));
                 }
              }
      );

      getAllPlayers().stream().filter(p -> !p.equals(player)).forEach(p -> p.showPlayer(player));
   }

   public static void play(@NotNull Player p, @NotNull Sound sound) {
      p.playSound(p.getLocation(), sound, 1,1);
   }

   public static void sellInventory(@NotNull Player p) {
      PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
      AtomicInteger total = new AtomicInteger();

      int a = 0;

      for(Map.Entry<ItemStack, Integer> entry : ShopHandler.price_map.entrySet()) {
         for(int i = 0; i < p.getInventory().getSize(); i++) {
            if(p.getInventory().getItem(i) != null) {
               ItemStack s = p.getInventory().getItem(i);
               if (!(s == null) && s.getType().equals(entry.getKey().getType())) {
                  total.set(total.get() + (entry.getValue() * s.getAmount()));
                  a += s.getAmount();
                  p.getInventory().remove(s);
               }
            }
         }
      }

      if(total.get() == 0) {
         p.sendMessage(Utils.f("&7Извините, у вас нет предметов для продажи."));
         return;
      }

      pp.giveMoney(total.get());
      pp.giveExp(total.get() / 3);
      play(p, Sound.PORTAL_TRAVEL);
      dropItem("&aSOLD", new Stack(Material.EMERALD).displayName("&aSOLD").amount(a > 64 ? 64 : a), 5, p.getLocation(), false);
   }

   public static void dropItem(@NotNull String name, @NotNull ItemStack stack, @NotNull int amount, @NotNull Location loc, @NotNull boolean pickup) {

      Vector pre = loc.toVector().subtract(loc.toVector()).multiply(.2);
      for(int i = 0; i < amount; i++) {
         pre = pre.subtract(loc.toVector()).multiply(.3);
         Item item = loc.getWorld().dropItem(loc, stack);
         if(!(name.equals(""))) {
            item.setCustomName(Utils.f(name));
            item.setCustomNameVisible(true);
         }
         if(!pickup) {
            item.setPickupDelay(Integer.MAX_VALUE);
            Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), item::remove, new Random().nextInt(400), 0);
         }
      }
   }

   @NotNull
   public static ParticleEffect sendParticle(@NotNull ParticleEffect particleEffect) {
      return particleEffect;
   }

   public static void sendRedScreen(@NotNull Player p, @NotNull int time) {
      EntityPlayer ePlayer = ((CraftPlayer) p).getHandle();

      WorldBorder w = new WorldBorder();
      w.setSize(1);
      w.setCenter(p.getLocation().getX() + 10_000, p.getLocation().getZ() + 10_000);

      ePlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(w, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));

      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         WorldBorder ww = new WorldBorder();
         ww.setSize(30_000_000);
         ww.setCenter(p.getLocation().getX(), p.getLocation().getZ());
         ePlayer.playerConnection.sendPacket(new PacketPlayOutWorldBorder(ww, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
      }, 20 * time);
   }


   public static void teleport(@NotNull Player p, @NotNull Location location) {
      try {
         p.teleport(location);
         p.sendMessage(Utils.f("&cВжух**"));
         p.playSound(p.getLocation(), Sound.CAT_PURREOW, 1, 1);
      }catch (Exception ex) {
         p.sendMessage(Utils.f("&7Произошла ошибка во время телепортации, сообщите администратору."));
      }
   }

   public static void teleport(@NotNull Player p, @NotNull Location loc, @NotNull int seconds) {
      p.sendTitle(Utils.f("&eСтой на месте"), Utils.f("&7Начинаем телепортацию"));
      new BukkitRunnable() {
         final Location def = p.getLocation();
         int sec = 0;
         int z = 3;

         private double sum(Location loc) {
            return loc.getX() + loc.getY() + loc.getZ();
         }

         @Override
         public void run() {
            if(p == null) this.cancel();

            if(!((sum(p.getLocation()) == sum(def)))) {
               p.sendTitle(Utils.f("&cПровал"), Utils.f("&7Стой на месте!"));
               p.sendMessage(Utils.f("&7Вам нужно стоять на месте неподвижно, чтобы выполнить телепортацию."));
               this.cancel();
            }

            if(sec < 80) {
               sec++;
               if(sec % 20 == 0) {
                  p.sendTitle(Utils.f("&c" + z), Utils.f("&7До телепортации"));
                  z--;
               }
            } else {
               this.cancel();
               p.sendTitle(Utils.f("&eDone"), Utils.f("&7Телепортируем (:"));
               teleport(p, loc);
            }
         }
      }.runTaskTimer(Main.getInstance(), 0, 1);
   }

   public static void breakNearbyBlocks(@NotNull Location start, @NotNull Material material, @NotNull int radius) {
      radius /= 2;
      for(int x = -radius; x <= radius; x++) {
         for(int y = -radius; y <= radius; y++) {
            for(int z = -radius; z <= radius; z++) {
               Block block = start.clone().add(x, y, z).getBlock();
               if(!block.getType().equals(material)) return;
               block.setType(Material.AIR);
            }
         }
      }
   }

   @NotNull
   public static Location getLocation(@NotNull String location) {
      World world = null;
      double x = 0.0D;
      double y = 0.0D;
      double z = 0.0D;
      double yaw = 0.0D;
      double pitch = 0.0D;
      String[] _loc = location.split(" ");

      try {
         world = Bukkit.getWorld(_loc[0]);
         x = Double.valueOf(_loc[1]);
         y = Double.valueOf(_loc[2]);
         z = Double.valueOf(_loc[3]);

         try {
            yaw = Double.valueOf(_loc[4]);
            pitch = Double.valueOf(_loc[5]);
         } catch (IndexOutOfBoundsException var14) {
            yaw = 0.0D;
            pitch = 0.0D;
         }

      } catch (NullPointerException ex) {
         Main.getInstance().getLogger().log(Level.WARNING, "Не удалось получить локацию!", ex);
      }

      Location loc = new Location(world, x, y, z, (float) yaw, (float) pitch);
      return loc.getWorld() != null ? loc : null;
   }

   public static Map<Enchantment, Integer> getEnchantments(String[] enchantments) {
      Map<Enchantment, Integer> requirements = new HashMap();
      String[] var5 = enchantments;
      int var4 = enchantments.length;

      for (int var3 = 0; var3 < var4; ++var3) {
         String s = var5[var3];
         String[] localEnch = s.split(":");
         String sEnch = localEnch[0];
         String sEnchLvl = localEnch[1];
         Enchantment aEnch = Enchantment.DURABILITY;
         Integer aEnchLvl = 1;

         try {
            aEnch = Enchantment.getByName(sEnch);
         } catch (NullPointerException var13) {
            Main.getInstance().getLogger().log(Level.WARNING, "Не удалось получить зачарование \"" + sEnch + "\". Устанавливаем стандартные значения.", var13);
         }

         try {
            aEnchLvl = Integer.valueOf(sEnchLvl);
         } catch (NumberFormatException var12) {
            Main.getInstance().getLogger().log(Level.WARNING, "Не удалось получить уровень зачарования. Устанавливаем 1 уровень!", var12);
         }

         requirements.put(aEnch, aEnchLvl);
      }
      return requirements;
   }

   @NotNull
   public static Integer slotCounter(@NotNull Integer itemsCount) {
      while(itemsCount % 9 != 0) itemsCount = itemsCount + 1;

      return itemsCount;
   }

   @NotNull
   public static boolean inRange(@NotNull Location base, @NotNull Location to, @NotNull int distance) {
      return base.distance(to) <= distance;
   }

}
