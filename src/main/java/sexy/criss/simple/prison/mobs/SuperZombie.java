package sexy.criss.simple.prison.mobs;

import java.util.List;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sexy.criss.simple.prison.PrisonPlayer;

public class SuperZombie extends EntityMonster {

   Spawner spawner;


   public SuperZombie(Spawner spawner) {
      super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, true));
      this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 1, true, false, null));
      this.setEquipment(0, new ItemStack(Items.GOLDEN_AXE));
      this.setCustomNameVisible(true);
      this.canPickUpLoot = false;
      this.fireProof = true;
      this.spawner = spawner;
      this.spawner.register(this);
      this.persistent = true;
      LivingEntity entity = (LivingEntity)this.getBukkitEntity();
      entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
   }

   public void die() {
      if(this.spawner != null) this.spawner.iDead();

      if(this.killer instanceof EntityPlayer) {
         Player p = (Player)this.killer.getBukkitEntity();
         PrisonPlayer.getPrisonPlayer(p).giveMoney(100);
      }

      super.die();
   }
}
