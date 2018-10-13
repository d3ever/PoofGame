package sexy.criss.simple.prison;

import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SavePlayersTask extends BukkitRunnable {

   int saveFileDelay = 0;


   public void run() {
      Iterator var2 = Bukkit.getServer().getOnlinePlayers().iterator();

      while(var2.hasNext()) {
         Player cp = (Player)var2.next();
         PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(cp);
         pp.save();
      }

   }
}
