/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:03 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard.type;

import org.bukkit.entity.Player;

public interface Scoreboard {
    void activate();
    void deactivate();

    boolean isActivated();

    ScoreboardHandler getHandler();

    long getUpdateInterval();

    Scoreboard setHandler(ScoreboardHandler handler);
    Scoreboard setUpdateInterval(long updateInterval);

    Player getHolder();
}
