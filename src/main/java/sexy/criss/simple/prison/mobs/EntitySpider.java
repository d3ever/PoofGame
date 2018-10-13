//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison.mobs;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_8_R3.DamageSource;
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
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.Utils;

public class EntitySpider extends EntityMonster {
    Spawner spawner;
    Entity jockey;
    private HashMap<String, Integer> attackers;
    private int totalDamage;
    private int jump_delay;
    private int hp_delay;

    public EntitySpider(Spawner spawner) {
        super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
        this.jump_delay = Main.SPIDER_JUMPDELAY;
        this.hp_delay = 5;
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double)Main.SPIDER_HEALTH);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
        this.getAttributeInstance(GenericAttributes.c).setValue(2.147483647E9D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.SPIDER_SPEED);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)Main.SPIDER_DAMAGE);
        this.setHealth((float)Main.SPIDER_HEALTH);
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 1, true, false, null));
        this.targetSelector.a(5, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (double)Main.SPIDER_DAMAGE, true));
        this.setCustomName(Main.SPIDER_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.fireProof = true;
        this.spawner = spawner;
        this.spawner.register(this);
        this.persistent = true;
        this.expToDrop = 0;
        Entity bukkitEntity = this.getBukkitEntity();
        this.attackers = new HashMap<>();
        this.totalDamage = 0;
    }

    private void superJump() {
        this.getBukkitEntity().getWorld().playEffect(this.getBukkitEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        this.getBukkitEntity().setVelocity((new Vector(0, 1, 0)).multiply(1));
        (new BukkitRunnable() {
            public void run() {
                EntitySpider.this.getBukkitEntity().getWorld().playSound(EntitySpider.this.getBukkitEntity().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);

                for (Entity entity : EntitySpider.this.getBukkitEntity().getNearbyEntities(7.0D, 4.0D, 7.0D)) {
                    if (entity.getType() == EntityType.PLAYER) {
                        entity.setVelocity(entity.getVelocity().add(entity.getLocation().getDirection()).multiply(-3));
                        EntityDamageEvent event = new EntityDamageEvent(entity, DamageCause.MAGIC, 5);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) ((LivingEntity) entity).damage(5.0D);
                    }
                }

            }
        }).runTaskLater(Main.getInstance(), 25L);
    }

    public boolean damageEntity(DamageSource source, float a) {
        if (source.getEntity() != this.passenger && source != DamageSource.STUCK) {
            this.setCustomName(Main.SPIDER_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            if (this.passenger != null) {
                return source != DamageSource.projectile(this, this.passenger) && this.passenger.damageEntity(source, a);
            } else {
                net.minecraft.server.v1_8_R3.Entity entity = source.i();
                if (entity != null && entity.getBukkitEntity().getType() == EntityType.PLAYER) {
                    PrisonPlayer pp = PrisonPlayer.getPrisonPlayer((Player)entity.getBukkitEntity());
                    if (!this.attackers.containsKey(pp.getName())) {
                        this.attackers.put(pp.getName(), (int)a);
                    } else {
                        this.attackers.put(pp.getName(), (int)((float)(Integer)this.attackers.get(pp.getName()) + a));
                    }

                    this.totalDamage = (int)((float)this.totalDamage + a);
                }

                if ((double)this.random.nextFloat() < 0.1D) {

                    for (Entity e : this.getBukkitEntity().getNearbyEntities(10.0D, 5.0D, 10.0D)) {
                        if (e.getType() == EntityType.PLAYER) {
                            ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
                        }
                    }
                }

                return super.damageEntity(source, a);
            }
        } else {
            return false;
        }
    }

    public void m() {
        if (this.hp_delay-- <= 0) {
            this.setCustomName(Main.SPIDER_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            if (this.getHealth() < (float)Main.SPIDER_HEALTH) {
                this.heal(1.0F, RegainReason.REGEN);
            }

            this.hp_delay = 5;
        }

        if (this.getGoalTarget() != null) {
            if (this.passenger == null) {
                --this.jump_delay;
                if (this.jump_delay <= 0) {
                    this.jump_delay = Main.SPIDER_JUMPDELAY;
                    this.superJump();
                }
            } else {
                double distance = this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation());
                if (distance < 9.0D) {
                    this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
                } else {
                    this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.SPIDER_SPEED);
                }
            }
        }

        super.m();
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

        if (this.killer != null) {
            Map<String, Integer> percents = Utils.calculatePercents(this.attackers, this.totalDamage);

            for (String key : percents.keySet()) {
                int money = (int) (percents.get(key) * Main.SPIDER_MONEY / 100);
                PrisonPlayer.getPrisonPlayer(Bukkit.getPlayer(key)).giveMoney(money);
            }

            Utils.bossInfo("&cБосс-паук", percents);
            Main.dropSpiderDropOn(this.getBukkitEntity().getLocation());
        }

        super.die();
    }

    public String getName() {
        return Main.SPIDER_NAME.replace("<3", "").replace("#", "");
    }
}
