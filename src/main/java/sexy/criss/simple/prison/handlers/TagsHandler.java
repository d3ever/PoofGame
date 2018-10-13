/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/29/2018 - 16:38 Wednesday
 *
 *******************************************************/
package sexy.criss.simple.prison.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Utils;

public class TagsHandler extends SexyEvent {

    @EventHandler
    public void onTags(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        update(p);
    }

    private void update(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
                    String c1 = p.isOp() || p.hasPermission("prison.admin") ? "&c" : pp.hasFaction() ? pp.getFaction().getColor() : "&f";
                    String f = "%s%s %s&7Уровень %s%d";
                    String fac = pp.hasFaction() ? pp.getFaction().getPrefix() : "";

                    //Utils.setPrefix(p, Utils.f(f, c1, p.getPlayerListName(), fac, c, pp.getLevel()));
                    p.setPlayerListName(Utils.f(c1 + p.getName() + " &8[&c" + pp.getLevel() + "&8]"));
                }catch (Exception ex) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 5);
    }

}
