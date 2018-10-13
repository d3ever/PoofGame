package sexy.criss.simple.prison.mobs;

import java.util.Random;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.PrisonPlayer;

public class EntityRat extends EntityMonster {

   Spawner spawner;
   private int hp_delay = 20;
   private Random rand = new Random();


   public EntityRat(Spawner spawner) {
      super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double) Main.RAT_HEALTH);
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
      this.getAttributeInstance(GenericAttributes.c).setValue(-1.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.RAT_SPEED);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)Main.RAT_DAMAGE);
      this.setHealth((float)Main.RAT_HEALTH);
      this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
      this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
      this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (double)Main.RAT_DAMAGE, true));
      this.spawner = spawner;
      this.setCustomName(Main.RAT_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
      this.setCustomNameVisible(true);
      this.canPickUpLoot = false;
      this.fireProof = true;
      if(this.spawner != null) this.spawner.register(this);

      this.persistent = true;
      this.expToDrop = 0;
   }

   public boolean damageEntity(DamageSource damagesource, float f) {
      this.setCustomName(Main.RAT_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
      return super.damageEntity(damagesource, f);
   }

   public void m() {
      if(this.hp_delay <= 0) {
         this.setCustomName(Main.RAT_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
         if(this.getHealth() < (float)Main.RAT_HEALTH) {
            this.heal(1.0F, RegainReason.REGEN);
         }

         this.hp_delay = 20;
      }

      --this.hp_delay;
      super.m();
      if(this.getGoalTarget() != null) {
         if(this.getGoalTarget() != null && this.getGoalTarget().world != this.world) {
            this.setGoalTarget(null);
         } else if(!(this.getGoalTarget() instanceof EntityPlayer)) {
            this.setGoalTarget(null);
         }
      }
   }

   public boolean r(Entity entity) {
      return super.r(entity);
   }

   public void dropDeathLoot(boolean flag, int i) {
      if((double)this.rand.nextFloat() <= 0.6D) {
         this.bukkitEntity.getWorld().dropItemNaturally(this.bukkitEntity.getLocation(), PrisonItem.getPrisonItem("rat_meat").getUsableItem());
      }

      if((double)this.rand.nextFloat() <= 0.1D) {
         this.bukkitEntity.getWorld().dropItemNaturally(this.bukkitEntity.getLocation(), new ItemStack(Material.SEEDS));
      }

   }

   public void die() {
      try {
         if(this.spawner != null) {
            this.spawner.iDead();
         }

         if(this.killer instanceof EntityPlayer) {
            Player e = (Player)this.killer.getBukkitEntity();
            if(PrisonPlayer.getPrisonPlayer(e) != null) {
               PrisonPlayer.getPrisonPlayer(e).giveMoney(rand.nextInt(5));
               if(this.spawner != null) {
                  PrisonPlayer.getPrisonPlayer(e).addMobKill(this.spawner.getType());
               }
            }
         }
      } catch (Exception var2) {
         System.err.println("RAT DIE ERROR! Report to dev");
      }

      super.die();
   }

   public String getName() {
      return "Тюремная крыса".replace("<3", "").replace("#", "");
   }
}
