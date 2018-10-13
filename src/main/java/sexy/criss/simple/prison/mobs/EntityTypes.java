//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison.mobs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public enum EntityTypes {
    SPIDER("Паук", 52, EntitySpider.class),
    JOCKEY("Наездник", 51, EntityJockey.class),
    RAT("Тюремная крыса", 60, EntityRat.class),
    ZOMBIE("Зомби", 54, EntityZombie.class),
    GIANTZOMBIE("Зомби-гигант", 53, EntityZombieGiant.class),
    COW("Корова", 92, PooCow.class),
    BIGSLIME("Большой слизень", 55, EntityBigSlime.class),
    PIG("Свинья", 90, EntityPig.class),
    WITCH("Ведьма", 66, EntityWitchBoss.class);

    private static Map<Integer, UUID> assoc = new HashMap<>();
    String name;
    int id;
    Class<? extends Entity> custom;

    EntityTypes(String name, int id, Class<? extends Entity> custom) {
       this.name = name;
       this.id = id;
       this.custom = custom;
   }

    public static Entity spawnEntity(Entity entity, Location loc) {
       entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
       ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity);
       return entity;
   }

    public static void spawnEntity(EntityTypes entityType, Location loc, Spawner spawner) {
       try {
          Entity entity = entityType.getEntityClass().getConstructor(Spawner.class).newInstance(spawner);
          entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
          ((CraftWorld)loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
       } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException ex) {
          ex.printStackTrace();
       }
    }

    public static void registerAll() {
       Arrays.asList(values()).forEach(EntityTypes::addToMaps);
    }

    private static void addToMaps(EntityTypes type) {
       ((Map)getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(type.custom, type.name);
       ((Map)getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(type.custom, type.id);
    }

    public static Object getPrivateField(String fieldName, Class<?> clazz, PathfinderGoalSelector object) {
       Object o = null;

       try {
          Field field = clazz.getDeclaredField(fieldName);
          field.setAccessible(true);
          o = field.get(object);
       } catch (NoSuchFieldException | IllegalAccessException ex) {
          ex.printStackTrace();
       }
       return o;
    }

    public String getName() {
      return this.name;
   }

    public Class<? extends Entity> getEntityClass() {
      return this.custom;
   }

    public static void associate(Entity entity, Spawner spawner) {
      assoc.put(entity.hashCode(), spawner.getUid());
   }

    public static Spawner getSpawnerByEntity(Entity entity) {
       return Spawner.spawners.get(assoc.get(entity.hashCode()));
    }
}
