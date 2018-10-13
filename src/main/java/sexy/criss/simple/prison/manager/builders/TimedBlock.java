package sexy.criss.simple.prison.manager.builders;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class TimedBlock {
    public static Map<Player, Integer> players_activity = Maps.newHashMap();
    public static Map<Location, TimedBlock> timedBlocks_map = Maps.newHashMap();

    private Location location;
    private Material regenType;
    private byte data;
    private int interval;

    public TimedBlock(Location location, Material regenType, byte data, int interval) {
        this.location = location;
        this.regenType = regenType;
        this.data = data;
        this.interval = interval;

        timedBlocks_map.put(location, this);
    }

    public static TimedBlock getTimedBlockByLoc(Location loc) {
        return timedBlocks_map.get(loc);
    }

    public static boolean isTimedBlock(Location loc) {
        return timedBlocks_map.containsKey(loc);
    }

    public Location getLocation() {
        return location;
    }

    public byte getData() {
        return data;
    }

    public Material getRegenType() {
        return regenType;
    }

    public int getInterval() {
        return interval;
    }

    public void updateBlock() {
        this.getLocation().getBlock().setType(this.regenType);
        this.getLocation().getBlock().setData(this.data);
    }

    public void remove() {
        timedBlocks_map.remove(this.location);
    }

}
