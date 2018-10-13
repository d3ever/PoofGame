package sexy.criss.simple.prison.mobs;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonItem;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.Utils;

import java.util.*;

public class EntityWitchBoss extends EntityMonster {

    int interval = 60;
    String name = "§cОдержимая ведьма §c[<3]";
    int health = 650;
    double damage = 15.0D;
    double followRange = 128.0D;
    double knobackResistence = 2.147483647E9D;
    double speed = 0.0D;
    double money = 3000.0D;
    int skill1 = 450;
    int skill2 = 700;
    int skill3 = 300;
    int toSkil1;
    int toSkil2;
    int toSkil3;
    Spawner spawner;
    CraftEntity bukkitEntity;
    HashMap<String, Integer> attackers;
    int totalDamage;
    int hpDelay;
    Random rnd;

    public EntityWitchBoss(Spawner spawner) {
        super(((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
        this.toSkil1 = this.skill1;
        this.toSkil2 = this.skill2;
        this.toSkil3 = this.skill3;
        this.hpDelay = 20;
        this.rnd = new Random();
        ((LivingEntity)this.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 2), true);
        ((LivingEntity)this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 3), true);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double)this.health);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(this.followRange);
        this.getAttributeInstance(GenericAttributes.c).setValue(this.knobackResistence);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.damage);
        this.setHealth((float)this.health);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)this.followRange));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)(this.followRange / 2.0D)));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, this.damage, true));
        this.spawner = spawner;
        this.spawner.register(this);
        this.setCustomName(this.name.replace("<3", String.format("%.2f", this.getHealth())));
        this.setCustomNameVisible(true);
        this.canPickUpLoot = false;
        this.fireProof = true;
        this.persistent = true;
        this.expToDrop = 0;
        this.bukkitEntity = this.getBukkitEntity();
        this.attackers = new HashMap();
        this.totalDamage = 0;
    }

    public boolean damageEntity(DamageSource source, float a) {
        if ((this.passenger == null || source.getEntity() != this.passenger) && source != DamageSource.STUCK) {
            this.setCustomName(this.name.replace("<3", String.format("%.2f", this.getHealth())));
            if (this.passenger != null) {
                return source != DamageSource.projectile(this, this.passenger) && this.passenger.damageEntity(source, a);
            } else {
                Entity entity = source.i();
                if (entity != null && entity.getBukkitEntity().getType() == EntityType.PLAYER) {
                    Player pAttacker = (Player)entity.getBukkitEntity();
                    if (!this.attackers.containsKey(pAttacker.getName())) {
                        this.attackers.put(pAttacker.getName(), (int)a);
                    } else {
                        this.attackers.put(pAttacker.getName(), (int)((float)(Integer)this.attackers.get(pAttacker.getName()) + a));
                    }

                    this.totalDamage += (int)a;
                }

                if ((double)this.random.nextFloat() < 0.1D) {
                    Iterator var6 = this.getBukkitEntity().getNearbyEntities(10.0D, 5.0D, 10.0D).iterator();

                    while(var6.hasNext()) {
                        org.bukkit.entity.Entity e = (org.bukkit.entity.Entity)var6.next();
                        if (e.getType() == EntityType.PLAYER) {
                            ((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 0));
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
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > 1.0D) {
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0F, 0.0F);
        }

        if (this.hpDelay-- <= 0) {
            this.setCustomName(this.name.replace("<3", String.format("%.2f", this.getHealth())));
            Iterator var1 = this.getBukkitEntity().getNearbyEntities(7.0D, 5.0D, 7.0D).iterator();

            label69:
            while(true) {
                org.bukkit.entity.Entity entity;
                do {
                    if (!var1.hasNext()) {
                        if (this.getHealth() < (float)this.health) {
                            this.heal(3.0F, EntityRegainHealthEvent.RegainReason.REGEN);
                        }

                        this.hpDelay = 20;
                        break label69;
                    }

                    entity = (org.bukkit.entity.Entity)var1.next();
                } while(entity.getType() != EntityType.PLAYER);

                Player p = (Player)entity;
                RegionManager rgm = Main.getWorldGuard().getRegionManager(p.getWorld());
                Set<ProtectedRegion> ars = rgm.getApplicableRegions(p.getLocation()).getRegions();
                Iterator var6 = ars.iterator();

                while(var6.hasNext()) {
                    ProtectedRegion prg = (ProtectedRegion)var6.next();
                    if (prg.getId().toLowerCase().startsWith("boss")) {
                        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
                            return;
                        }

                        if (p.getAllowFlight() || p.isFlying()) {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                            p.sendMessage("§bЛетать на локации с боссом запрещено!");
                            return;
                        }
                    }
                }
            }
        }

        if (this.getGoalTarget() != null && this.passenger == null) {
            --this.toSkil1;
            if (this.toSkil1 <= 0) {
                this.toSkil1 = this.skill1;
                this.spiders();
            }

            --this.toSkil2;
            if (this.toSkil2 <= 0) {
                this.toSkil2 = this.skill2;
                this.debuff();
            }

            --this.toSkil3;
            if (this.toSkil3 <= 0) {
                this.toSkil3 = this.skill3;
                this.throwDebuff();
            }
        }

        super.m();
    }

    public void dropDeathLoot(boolean flag, int i) {
        if ((double)this.rnd.nextFloat() <= 0.05D) {
            ItemStack luckypickaxe = PrisonItem.getPrisonItem("rarepickaxe").getUsableItem();
            this.getBukkitEntity().getLocation().getWorld().dropItem(this.bukkitEntity.getLocation(), luckypickaxe);
        }

    }

    public void die() {
        if (this.spawner != null) {
            this.spawner.iDead();
        }

        if (this.killer != null) {
            Map<String, Integer> percents = Utils.calculatePercents(this.attackers, this.totalDamage);
            Iterator var2 = percents.keySet().iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(Bukkit.getPlayer(key));
                int money = (int) (percents.get(key) * this.money);
                if (money < 0.0D) money = 0;


                if (pp != null) {
                    pp.takeMoney(money);
                    pp.addMobKill(this.spawner.getType());
                }

                if (Bukkit.getPlayer(key) != null) {
                    Bukkit.getPlayer(key).sendMessage("§aНа ваш счет было зачислено %money%$".replace("%money%", String.format("%.2f", money)));
                }
            }
            Utils.bossInfo("Одержимая ведьма", percents);

            ItemStack star = PrisonItem.getPrisonItem("star").getUsableItem();
            star.setAmount((new Random()).nextInt(1) + 10);
            this.getBukkitEntity().getLocation().getWorld().dropItem(this.bukkitEntity.getLocation(), star);
        }

        super.die();
    }

    public void spiders() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            List<org.bukkit.entity.Entity> near = this.bukkitEntity.getNearbyEntities(10.5D, 10.5D, 10.5D);
            if (near.size() > 0) {
                Iterator var2 = near.iterator();

                while(true) {
                    org.bukkit.entity.Entity entity;
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        entity = (org.bukkit.entity.Entity)var2.next();
                    } while(entity.getType() != EntityType.PLAYER);

                    Player player = (Player)entity;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 255, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 255, true));
                    player.setFireTicks(20);

                    for(int i = 0; i < (new Random()).nextInt(3); ++i) {
                        Spider spider = (Spider)player.getWorld().spawnEntity(this.bukkitEntity.getLocation(), EntityType.SPIDER);
                        spider.setCustomName("§cПаук");
                        spider.setCustomNameVisible(true);
                        spider.setTarget(player);
                    }
                }
            }
        });
    }

    public void debuff() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            List<org.bukkit.entity.Entity> near = this.bukkitEntity.getNearbyEntities(15.0D, 15.0D, 15.0D);
            if (near.size() > 0) {
                Iterator var2 = near.iterator();

                while(var2.hasNext()) {
                    org.bukkit.entity.Entity entity = (org.bukkit.entity.Entity)var2.next();
                    if (entity.getType() == EntityType.PLAYER) {
                        Player player = (Player)entity;
                        double damage = 15.0D;
                        EntityDamageEvent event = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, damage);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            player.damage(damage, this.bukkitEntity);
                            if (player.isDead()) {
                                this.heal(40.0F, EntityRegainHealthEvent.RegainReason.REGEN);
                            }

                            player.setFireTicks(240);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 500, 0), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 1), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 254), true);
                            player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1.0F, 1000.0F);
                            entity.setVelocity(new Vector(Math.random() * (this.rnd.nextBoolean() ? 1.5D : -1.5D), Math.random() * 1.5D, Math.random() * (this.rnd.nextBoolean() ? 1.5D : -1.5D)));
                        }
                    }
                }
            }

        });
    }

    public void throwDebuff() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            List<org.bukkit.entity.Entity> near = this.bukkitEntity.getNearbyEntities(15.0D, 15.0D, 15.0D);
            if (near.size() > 0) {
                Iterator var2 = near.iterator();

                while(var2.hasNext()) {
                    org.bukkit.entity.Entity entity = (org.bukkit.entity.Entity)var2.next();
                    if (entity.getType() == EntityType.PLAYER) {
                        try {
                            Player player = (Player)entity;
                            Location star = player.getLocation();
                            Location end = this.bukkitEntity.getLocation();
                            player.setVelocity(new Vector(end.getX() - star.getX(), end.getY() + 1.5D - star.getY(), end.getZ() - star.getZ()));
                            if (this.rnd.nextBoolean()) {
                                double damage = (double)(this.rnd.nextInt(6) + 1);
                                EntityDamageEvent event = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.CUSTOM, damage);
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    player.damage(damage, this.bukkitEntity);
                                }
                            }
                        } catch (NullPointerException ignored) {
                        }
                    }
                }
            }

        });
    }

    public String getName() {
        return this.name.replace("<3", String.format("%.2f", this.getHealth()));
    }

}
