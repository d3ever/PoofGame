package sexy.criss.simple.prison.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Utils;

public class GetDamageHandler extends SexyEvent {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        Utils.sendRedScreen(p, 1);
    }

}
