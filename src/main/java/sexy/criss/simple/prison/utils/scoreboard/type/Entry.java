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

import sexy.criss.simple.prison.utils.Utils;

public class Entry {
    private String name;
    private int position;

    public Entry(String name, int position) {
        this.name = Utils.f(name);
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
