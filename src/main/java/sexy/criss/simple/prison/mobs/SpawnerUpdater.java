package sexy.criss.simple.prison.mobs;

import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerUpdater extends BukkitRunnable {

   public void run() {
      Spawner.spawners.values().forEach(Spawner::update);
   }
}
