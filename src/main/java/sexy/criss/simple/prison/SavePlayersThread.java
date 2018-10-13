package sexy.criss.simple.prison;

import java.util.logging.Level;

public class SavePlayersThread extends Thread {

   public void run() {
      Main.getInstance().getLogger().log(Level.INFO, "Save-thread was started!");

      for(; Main.saveEnabled; Main.saveConfig(Main.players_storage, "players.yml")) {
         try {
            sleep(10000L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      System.out.println("Save-thread was stopped.");
   }
}
