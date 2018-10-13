package sexy.criss.simple.prison.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import sexy.criss.simple.prison.utils.SexyEvent;

public class GravityHandler extends SexyEvent {

    @EventHandler
    public void onGravity(BlockPhysicsEvent e) {
        e.setCancelled(true);
    }

}
