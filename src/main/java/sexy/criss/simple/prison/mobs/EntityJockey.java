//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison.mobs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IRangedEntity;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;

public class EntityJockey extends EntityMonster implements IRangedEntity {
    Spawner spawner;
    private int hp_delay;
    private int shot_delay;

    public EntityJockey(Spawner spawner) {
        this(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
        this.spawner = spawner;
    }

    private EntityJockey(World world) {
        super(world);
        int delay = 100;
        this.hp_delay = 10;
        this.shot_delay = Main.SKELETON_SHOOTDELAY;
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double)Main.SKELETON_HEALTH);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
        this.getAttributeInstance(GenericAttributes.c).setValue(2.147483647E9D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(1.0D);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)Main.SKELETON_HEALTH);
        this.setHealth((float)Main.SKELETON_HEALTH);
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 10, true, false, null));
        this.targetSelector.a(5, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (double)Main.SKELETON_DAMAGE, true));
        this.setEquipment(0, new ItemStack(Items.BOW));
        this.setCustomName(Main.SKELETON_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.fireProof = true;
        if (this.spawner != null) {
            this.spawner.register(this);
        }

        this.persistent = true;
        this.expToDrop = 0;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource.getEntity() != this && damagesource != DamageSource.STUCK) {
            this.setCustomName(Main.SKELETON_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            return super.damageEntity(damagesource, f);
        } else {
            return false;
        }
    }

    private void shot(EntityLiving entityliving) {
        Set<EntityArrow> arrows = new HashSet<>();

        for(int a = 0; a < 3; ++a) {
            EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, (float)(14 - this.world.getDifficulty().a() * 4));
            int i = Main.SKELETON_DAMAGE;
            int j = Main.SKELETON_KNOCKBACK;
            entityarrow.b((double)(1.0F * 2.0F) + this.random.nextGaussian() * 0.75D + (double)((float)this.world.getDifficulty().a() * 0.11F));
            if (i > 0) {
                entityarrow.b(entityarrow.j() + (double)i * 0.5D + 0.5D);
            }

            if (j > 0) {
                entityarrow.setKnockbackStrength(j);
            }

            arrows.add(entityarrow);
            this.makeSound("random.bow", 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
            this.world.addEntity(entityarrow);
        }

        (new EntityJockey.ArrowRemoverTask(arrows)).runTaskLater(Main.getInstance(), 100L);
    }

    public void a(EntityLiving entityliving, float f) {
    }

    public void m() {
        if (this.hp_delay <= 0) {
            this.setCustomName(Main.SKELETON_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            this.hp_delay = 10;
        }

        --this.hp_delay;
        super.m();
        if (this.getGoalTarget() != null) {
            if (this.getGoalTarget() != null && this.getGoalTarget().world != this.world) {
                this.setGoalTarget(null);
            } else {
                --this.shot_delay;
                if (this.shot_delay <= 0) {
                    this.shot_delay = Main.SKELETON_SHOOTDELAY;
                    this.shot(this.getGoalTarget());
                }

                double distance = this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation());
                if (this.getGoalTarget() != null && distance > 16.0D) {
                    this.setGoalTarget(null);
                } else {
                    if (distance < 7.0D) {
                        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
                    } else {
                        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(1.0D);
                    }

                    if (!(this.getGoalTarget() instanceof EntityPlayer)) {
                        this.setGoalTarget(null);
                    }
                }
            }
        }
    }

    public boolean r(Entity entity) {
        return false;
    }

    public void dropDeathLoot(boolean flag, int i) {
    }

    public void die() {
        if (this.spawner != null) {
            this.spawner.iDead();
        }

        if (this.killer instanceof EntityPlayer) {
            Player p = (Player)this.killer.getBukkitEntity();
            if (this.spawner != null) {
                PrisonPlayer.getPrisonPlayer(p).addMobKill(this.spawner.getType());
            }
        }

        super.die();
    }

    public String getName() {
        return Main.SKELETON_NAME.replace("<3", "").replace("#", "");
    }

    class ArrowRemoverTask extends BukkitRunnable {
        Collection<EntityArrow> arrows;

        ArrowRemoverTask(Collection<EntityArrow> arrows) {
            this.arrows = arrows;
        }

        public void run() {
            this.arrows.forEach(a -> a.getBukkitEntity().remove());
        }
    }
}
