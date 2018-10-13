package sexy.criss.simple.prison.mobs;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.utils.Stack;
import sexy.criss.simple.prison.utils.Utils;
import sexy.criss.simple.prison.wrapper.particle.ParticleEffect;

import java.util.Random;

public class EntityPig extends net.minecraft.server.v1_8_R3.EntityPig {
   Spawner spawner;


   public EntityPig(World world) {
      super(world);
   }

   public EntityPig(Spawner spawner) {
      super(((CraftWorld) spawner.getSpawnLocation().getWorld()).getHandle());
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(666);
      this.setHealth(666);
      spawner.register(this);
      this.fireProof = true;
   }

   public boolean damageEntity(DamageSource source, float a) {
      if(!(source.getEntity() instanceof Player)) { return false; }
      if(new Random().nextInt(100) < 30) {
         getBukkitEntity().getWorld().dropItemNaturally(getBukkitEntity().getLocation(), new Stack(Material.PORK).displayName("&cСвинина"));
         //Utils.sendParticle(ParticleEffect.VILLAGER_HAPPY).display(5, 5, 5, .2f, 3000, getBukkitEntity().getLocation(), (Player) source.getEntity());
      }
      return false;
   }

   public void m() {
      if(getHealth() < 666) {
         this.heal(666 - getHealth(), EntityRegainHealthEvent.RegainReason.REGEN);
      }
      super.m();
   }

}
