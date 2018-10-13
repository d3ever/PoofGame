//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison.mobs;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Map;
import java.util.Random;

public class EntityBigSlime extends EntityMonster {
    Spawner spawner;
    Entity jockey;
    private Map<String, Integer> attackers;
    private int totalDamage;
    private int debuff = 80;
    private int hp_delay = 5;

    public EntityBigSlime(Spawner spawner) {
        super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double) Main.BIGSLIME_HEALTH);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
        this.getAttributeInstance(GenericAttributes.c).setValue(2.147483647E9D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.BIGSLIME_SPEED);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)Main.BIGSLIME_DAMAGE);
        this.setHealth((float)Main.BIGSLIME_HEALTH);
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(5, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (double)Main.BIGSLIME_DAMAGE, true));
        this.setCustomName(Main.BIGSLIME_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
        this.setCustomNameVisible(true);
        this.setSize(6);
        this.canPickUpLoot = false;
        this.fireProof = true;
        this.spawner = spawner;
        this.spawner.register(this);
        this.persistent = true;
        this.expToDrop = 0;
        Entity bukkitEntity = this.getBukkitEntity();
        this.attackers = Maps.newHashMap();
        this.totalDamage = 0;
    }

    public void setSize(int i) {
        this.datawatcher.a(10, (byte) i);
        this.a(0.6F * (float)i, 0.6F * (float)i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.setHealth(this.getMaxHealth());
        this.b_ = i;
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    protected float bB() {
        return 0.4F * (float)this.getSize();
    }

    public boolean damageEntity(DamageSource source, float a) {
        if (source.getEntity() != this.passenger && source != DamageSource.STUCK) {
            this.setCustomName(Main.BIGSLIME_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            if (this.passenger != null) {
                return source != DamageSource.projectile(this, this.passenger) && this.passenger.damageEntity(source, a);
            } else {
                net.minecraft.server.v1_8_R3.Entity entity = source.i();
                if (entity != null && entity.getBukkitEntity().getType() == EntityType.PLAYER) {
                    Player p = (Player)entity.getBukkitEntity();
                    if ((double)(new Random()).nextFloat() <= 0.1D) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
                    }

                    PrisonPlayer pp = PrisonPlayer.getPrisonPlayer((Player)entity.getBukkitEntity());
                    if (!this.attackers.containsKey(pp.getName())) {
                        this.attackers.put(pp.getName(), (int)a);
                    } else {
                        this.attackers.put(pp.getName(), (int)((float)(Integer)this.attackers.get(pp.getName()) + a));
                    }

                    this.totalDamage = (int)((float)this.totalDamage + a);
                }

                return super.damageEntity(source, a);
            }
        } else {
            return false;
        }
    }

    public void m() {
        if (this.hp_delay-- <= 0) {
            this.setCustomName(Main.BIGSLIME_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            if (this.getHealth() < (float)Main.BIGSLIME_HEALTH) {
                this.heal(2.0F, RegainReason.REGEN);
            }

            this.hp_delay = 5;
        }

        if ((double)this.getHealth() < (double)Main.BIGSLIME_HEALTH * 0.7D) {
            --this.debuff;
            if (this.debuff <= 0) {
                this.debuff = 160;
                this.debuff();
            }
        }

        if (this.getGoalTarget() != null && this.passenger != null) {
            double distance = this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation());
            if (distance < 9.0D) {
                this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            } else {
                this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.BIGSLIME_SPEED);
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
                int money = (int) (percents.get(key) * (double) Main.BIGSLIME_MONEY / 100.0D);
                PrisonPlayer.getPrisonPlayer(Bukkit.getPlayer(key)).giveMoney(money);
            }

            Utils.bossInfo("Древний слизень", percents);
            Main.dropBigSlimeDropOn(this.getBukkitEntity().getLocation());
        }

        super.die();
    }

    private void debuff() {
        (new BukkitRunnable() {
            public void run() {

                for (Entity entity : EntityBigSlime.this.getBukkitEntity().getNearbyEntities(7.0D, 5.0D, 7.0D)) {
                    if (entity.getType() == EntityType.PLAYER) {
                        Player p = (Player) entity;
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 10));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 200));
                        p.damage(6.0D);
                    }
                }

            }
        }).runTaskLater(Main.getInstance(), 25L);
    }

    public String getName() {
        return Main.BIGSLIME_NAME.replace("<3", "").replace("#", "");
    }
}
