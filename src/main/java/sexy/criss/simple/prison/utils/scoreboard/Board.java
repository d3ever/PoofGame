/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:01 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard;

import org.bukkit.entity.Player;
import sexy.criss.simple.prison.utils.scoreboard.type.Scoreboard;
import sexy.criss.simple.prison.utils.scoreboard.type.SimpleScoreboard;

public class Board {

    public static Scoreboard createBoard(Player holder) {
        return new SimpleScoreboard(holder);
    }

}
