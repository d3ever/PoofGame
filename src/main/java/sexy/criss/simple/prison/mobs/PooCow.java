package sexy.criss.simple.prison.mobs;

import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Item;
import sexy.criss.simple.prison.PrisonItem;

public class PooCow extends EntityCow {

   private int pooTimer;
   private CopyOnWriteArrayList<Item> log;
   Spawner spawner;


   public PooCow(World world) {
      super(world);
   }

   public PooCow(Spawner spawner) {
      super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
      this.pooTimer = this.random.nextInt(1500);
      this.log = new CopyOnWriteArrayList();
      spawner.register(this);
   }

   public boolean damageEntity(DamageSource source, float a) {
      return false;
   }

   private void poo() {
      this.log.stream().filter(o -> ((o == null) || (!o.isValid()))).forEach(log::remove);

      if(this.log.size() <= 3)
         this.log.add(this.getBukkitEntity().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(), PrisonItem.getPrisonItem("poo").getUsableItem()));
   }

   public void m() {
      super.m();
      if(this.pooTimer-- <= 0) {
         this.poo();
         this.pooTimer = this.random.nextInt(1500);
      }

   }
}
