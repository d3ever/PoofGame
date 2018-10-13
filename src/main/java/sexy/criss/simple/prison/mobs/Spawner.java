package sexy.criss.simple.prison.mobs;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

public class Spawner {

   public static Map<UUID, Spawner> spawners = Maps.newHashMap();
   private Location location;
   private EntityTypes type;
   private Entity current;
   private long deathTime;
   private int interval;
   private UUID uid;


   public Spawner(Location location, EntityTypes type, int interval) {
      this.location = location;
      this.current = null;
      this.deathTime = -1L;
      this.uid = UUID.randomUUID();
      this.type = type;
      this.interval = interval;
      spawners.put(this.uid, this);
   }

   private void spawn() {
      if(this.location.getChunk().isLoaded()) {
         EntityTypes.spawnEntity(this.type, this.location.clone().add(0.0D, 1.0D, 0.0D), this);
      }

   }

   void iDead() {
      this.current = null;
      this.deathTime = System.currentTimeMillis() / 1000L;
   }

   Location getSpawnLocation() {
      return this.location;
   }

   public void update() {
      if(this.current == null) if(System.currentTimeMillis() / 1000L - this.deathTime >= (long)this.interval) this.spawn();
      else if(this.current.getBukkitEntity().getLocation().distance(this.location) > 50) this.reset();
   }

   public void register(Entity me) {
      this.current = me;
   }

   public void reset() {
      if(this.current != null) {
         if(this.current.passenger != null) this.current.passenger.getBukkitEntity().remove();

         this.current.getBukkitEntity().remove();
      }

      this.deathTime = -1L;
      this.getCurrent().teleportTo(getSpawnLocation(), false);
   }

   UUID getUid() {
      return this.uid;
   }

   public Entity getCurrent() {
      return this.current;
   }

   public EntityTypes getType() {
      return this.type;
   }
}
