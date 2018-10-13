package sexy.criss.simple.prison.manager;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.utils.Utils;

public class SpawnManager {
    private Location spawn_location;

    public Location getSpawnLocation() {
        return spawn_location;
    }

    public void setSpawn(Location loc) {
        this.spawn_location = loc;
    }

    public void teleport(Player p, boolean timed) {
        if(timed) Utils.teleport(p, spawn_location, 3);
        else Utils.teleport(p, spawn_location);
    }

    public void save() {
        Location l = this.spawn_location;
        Main.config.set("spawn.world", l.getWorld().getName());
        Main.config.set("spawn.x", l.getX());
        Main.config.set("spawn.y", l.getY());
        Main.config.set("spawn.z", l.getZ());
    }

}
