//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sexy.criss.simple.prison.mobs;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class EntityZombieGiant extends EntityMonster {
    Spawner spawner;
    Entity jockey;
    private Map<String, Integer> attackers;
    private int totalDamage;
    private int jump_delay = 0;
    private int harm_delay = 0;
    private int hp_delay = 5;

    public EntityZombieGiant(Spawner spawner) {
        super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double) Main.ZOMBIEGIANT_HEALTH);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(128.0D);
        this.getAttributeInstance(GenericAttributes.c).setValue(2.147483647E9D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.ZOMBIEGIANT_SPEED);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)Main.ZOMBIEGIANT_DAMAGE);
        this.setHealth((float)Main.ZOMBIEGIANT_HEALTH);
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 128.0F));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        this.targetSelector.a(5, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 64.0F));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, (double)Main.ZOMBIEGIANT_DAMAGE, true));
        this.setCustomName(Main.ZOMBIEGIANT_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
        this.setCustomNameVisible(true);
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

    private void superJump() {
        this.getBukkitEntity().getWorld().playEffect(this.getBukkitEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
        (new BukkitRunnable() {
            public void run() {
                EntityZombieGiant.this.getBukkitEntity().getWorld().playSound(EntityZombieGiant.this.getBukkitEntity().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                getBukkitEntity().getNearbyEntities(12.5D, 5D, 12.5D).forEach(e -> e.setVelocity(e.getVelocity().add(new Vector(0, 1, 0).multiply(1))));
            }
        }).runTaskLater(Main.getInstance(), 25L);
    }

    public boolean damageEntity(DamageSource source, float a) {
        if (source.getEntity() != this.passenger && source != DamageSource.STUCK) {
            this.setCustomName(Main.ZOMBIEGIANT_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            if (this.passenger != null) {
                return source != DamageSource.projectile(this, this.passenger) && this.passenger.damageEntity(source, a);
            } else {
                net.minecraft.server.v1_8_R3.Entity entity = source.i();
                if (entity != null && entity.getBukkitEntity().getType() == EntityType.PLAYER) {
                    Player p = (Player)entity.getBukkitEntity();
                    p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
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
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > 20.0D) {
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0F, 0.0F);
        }

        if ((double)this.getHealth() < (double)Main.ZOMBIEGIANT_HEALTH * 0.35D) {
            this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue((double)(Main.ZOMBIEGIANT_DAMAGE * 2));
        }

        Entity e;
        Iterator it;
        if ((double)this.getHealth() < (double)Main.ZOMBIEGIANT_HEALTH * 0.6D && this.harm_delay-- <= 0) {
            it = this.getBukkitEntity().getNearbyEntities(25.0D, 5.0D, 25.0D).iterator();

            while(it.hasNext()) {
                e = (Entity) it.next();
                if (e.getType() == EntityType.PLAYER) {
                    ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
                    ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                    this.harm_delay = 240;
                }
            }
        }

        it = this.getBukkitEntity().getNearbyEntities(25.0D, 5.0D, 25.0D).iterator();

        while(it.hasNext()) {
            e = (Entity) it.next();
            if (e.getType() == EntityType.PLAYER) {
                ((LivingEntity)e).removePotionEffect(PotionEffectType.CONFUSION);
                ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
            }
        }

        if (this.hp_delay-- <= 0) {
            this.setCustomName(Main.ZOMBIEGIANT_NAME.replace("#", String.valueOf((int)this.getHealth())).replace("<3", "❤"));
            if (this.getHealth() < (float)Main.ZOMBIEGIANT_HEALTH) {
                this.heal(7.0F, RegainReason.REGEN);
            }

            this.hp_delay = 5;
        }

        if ((double)this.getHealth() < (double)Main.ZOMBIEGIANT_HEALTH * 0.2D) {
            --this.jump_delay;
            if (this.jump_delay <= 0) {
                this.jump_delay = 100;
                this.superJump();
            }
        }

        if (this.getGoalTarget() != null && this.passenger != null) {
            double distance = this.getBukkitEntity().getLocation().distance(this.getGoalTarget().getBukkitEntity().getLocation());
            if (distance < 9.0D) {
                this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
            } else {
                this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Main.ZOMBIEGIANT_SPEED);
            }
        }

        super.m();
    }

    public void die() {
        if (this.spawner != null) this.spawner.iDead();

        if (this.killer instanceof EntityPlayer) {
            Player p = (Player)this.killer.getBukkitEntity();
            PrisonPlayer.getPrisonPlayer(p).addMobKill(this.spawner.getType());
        }

        if (this.killer != null) {
            Map<String, Integer> percents = Utils.calculatePercents(this.attackers, this.totalDamage);

            for (String key : percents.keySet()) {
                int money = (int) (percents.get(key) * (double) Main.ZOMBIEGIANT_MONEY / 100.0D);
                PrisonPlayer.getPrisonPlayer(Bukkit.getPlayer(key)).giveMoney(money);
            }

            Utils.bossInfo("&cЗомби-гигант", percents);
            Main.dropGiantDropOn(this.getBukkitEntity().getLocation());
            Random rnd = new Random();
            int amount = rnd.nextInt(18) + 8;

            ItemStack star = new ItemStack(Material.NETHER_STAR);
            ItemMeta starMeta = star.getItemMeta();
            starMeta.setDisplayName(ChatColor.GOLD + "Звезда");
            star.setItemMeta(starMeta);
            star.setAmount(amount);
            this.getBukkitEntity().getLocation().getWorld().dropItemNaturally(this.getBukkitEntity().getLocation(), star);
        }

        super.die();
    }

    public String getName() {
        return Main.ZOMBIEGIANT_NAME.replace("<3", "").replace("#", "");
    }
}
