/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:04 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard.type;

import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardHandler {

    String getTitle(Player player);

    List<Entry> getEntries(Player player);
}
