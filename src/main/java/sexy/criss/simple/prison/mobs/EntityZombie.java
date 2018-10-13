package sexy.criss.simple.prison.mobs;

import java.util.Random;
import net.md_5.bungee.api.ChatColor;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;

public class EntityZombie extends EntityMonster {

   Spawner spawner;
   private int hp_delay = 10;
   private Random rand = new Random();


   public EntityZombie(Spawner spawner) {
      super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double) Main.ZOMBIE_HEALTH);
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
      this.getAttributeInstance(GenericAttributes.c).setValue(-1.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.ZOMBIE_SPEED);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)Main.ZOMBIE_DAMAGE);
      this.setHealth((float)Main.ZOMBIE_HEALTH);
      this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
      this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 10, true, false, null));
      this.targetSelector.a(5, new PathfinderGoalHurtByTarget(this, true));
      this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (double)Main.ZOMBIE_DAMAGE, true));
      this.spawner = spawner;
      this.setCustomName(Main.ZOMBIE_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
      this.setCustomNameVisible(true);
      this.canPickUpLoot = false;
      this.fireProof = true;
      if(this.spawner != null) {
         this.spawner.register(this);
      }

      this.persistent = true;
      this.expToDrop = 0;
   }

   public boolean damageEntity(DamageSource damagesource, float f) {
      this.setCustomName(Main.ZOMBIE_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
      return super.damageEntity(damagesource, f);
   }

   public void m() {
      if(this.hp_delay <= 0) {
         this.setCustomName(Main.ZOMBIE_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
         this.hp_delay = 10;
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
      if((double)this.rand.nextFloat() <= 0.75D) {
         this.bukkitEntity.getWorld().dropItemNaturally(this.bukkitEntity.getLocation(), new ItemStack(Material.ARROW));
      }

      if((double)this.rand.nextFloat() <= 0.01D) {
         ItemStack star = new ItemStack(Material.NETHER_STAR);
         ItemMeta starMeta = star.getItemMeta();
         starMeta.setDisplayName(ChatColor.GOLD + "Звезда");
         star.setItemMeta(starMeta);
         this.bukkitEntity.getWorld().dropItemNaturally(this.bukkitEntity.getLocation(), star);
      }

      if((double)this.rand.nextFloat() <= 0.08D) {
         this.bukkitEntity.getWorld().dropItemNaturally(this.bukkitEntity.getLocation(), new ItemStack(Material.GOLDEN_APPLE));
      }

   }

   public void die() {
      try {
         if(this.spawner != null) this.spawner.iDead();

         if(this.killer instanceof EntityPlayer) {
            Player e = (Player)this.killer.getBukkitEntity();
            if(PrisonPlayer.getPrisonPlayer(e) != null) {
               PrisonPlayer.getPrisonPlayer(e).giveMoney(rand.nextInt(15));
               PrisonPlayer.getPrisonPlayer(e).addMobKill(this.spawner.getType());
            }
         }
      } catch (Exception ex) {
         System.err.println("ZOMBIE DIE ERROR! Report to dev");
      }

      super.die();
   }

   public String getName() {
      return Main.ZOMBIE_NAME.replace("<3", "").replace("#", "");
   }
}
