/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/29/2018 - 02:57 Wednesday
 *
 *******************************************************/
package sexy.criss.simple.prison.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import sexy.criss.simple.prison.commands.BoardCommand;
import sexy.criss.simple.prison.commands.ShopCommand;
import sexy.criss.simple.prison.utils.SexyEvent;

public class InventoryHandler extends SexyEvent {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        BoardCommand.handle(e);
        ShopCommand.handle(e);
    }

}
