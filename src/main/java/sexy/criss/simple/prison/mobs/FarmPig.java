package sexy.criss.simple.prison.mobs;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.PrisonPlayer;

import java.util.Random;

public class FarmPig extends EntityPig {

   Spawner spawner;
   private int hp_delay = 20;
   private Random rand = new Random();


   public FarmPig(Spawner spawner) {
      super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(666);
      this.setHealth(666f);
      this.canPickUpLoot = false;
      this.fireProof = true;
      if(this.spawner != null) this.spawner.register(this);

      this.persistent = true;
      this.expToDrop = 0;
   }

   public boolean damageEntity(DamageSource damagesource, float f) {
      if(rand.nextInt(100) < 30) {
         getBukkitEntity().getWorld().dropItemNaturally(getBukkitEntity().getLocation(), new ItemStack(Material.PORK, rand.nextInt(5)));
         getBukkitEntity().getWorld().playEffect(getBukkitEntity().getLocation(), Effect.HAPPY_VILLAGER, 15, 5);
      }
      return super.damageEntity(damagesource, 0f);
   }
}
