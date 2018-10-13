/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/26/2018 - 23:18 Sunday
 *
 *******************************************************/
package sexy.criss.simple.prison.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Utils;

public class JoinHandler extends SexyEvent {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.hasPlayedBefore()) return;
        Utils.teleport(p, Main.getInstance().getSpawnManager().getSpawnLocation());
    }

}
