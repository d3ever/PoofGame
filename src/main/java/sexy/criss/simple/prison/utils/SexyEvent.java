/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/26/2018 - 23:17 Sunday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import sexy.criss.simple.prison.Main;

public class SexyEvent implements Listener {

    public SexyEvent() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

}
