/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:14 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard.common.animation;

public class StaticString implements AnimatableString {
    private String string;

    public StaticString(String string) {
        this.string = string;
    }

    @Override
    public String current() {
        return string;
    }

    @Override
    public String previous() {
        return string;
    }

    @Override
    public String next() {
        return string;
    }

}
