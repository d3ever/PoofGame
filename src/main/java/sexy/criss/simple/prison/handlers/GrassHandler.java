package sexy.criss.simple.prison.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import sexy.criss.simple.prison.utils.SexyEvent;

public class GrassHandler extends SexyEvent {

    @EventHandler
    public void onGrassChangeBlock(BlockGrowEvent e) {
        if(!(e.getBlock().getType().equals(Material.DIRT))) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeaves(LeavesDecayEvent e) {
        e.setCancelled(true);
    }

}
