/*******************************************************
 * Copyright (C) 2017-2018 d3ever <d3ever@rewforce.cf>
 *
 * This file is part of sexy.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of d3ever
 *
 * Date: 8/27/2018 - 20:06 Monday
 *
 *******************************************************/
package sexy.criss.simple.prison.utils.scoreboard.common;

import sexy.criss.simple.prison.utils.Utils;
import sexy.criss.simple.prison.utils.scoreboard.type.Entry;

import java.util.LinkedList;
import java.util.List;

public class EntryBuilder {
    private final LinkedList<Entry> entries = new LinkedList<>();

    public EntryBuilder blank() {
        return next("");
    }

    public EntryBuilder next(String string) {
        entries.add(new Entry(adapt(string), entries.size()));
        return this;
    }

    public List<Entry> build() {
        for (Entry entry : entries) entry.setPosition(entries.size() - entry.getPosition());

        return entries;
    }

    private String adapt(String entry) {
        if (entry.length() > 48) entry = entry.substring(0, 47);
        return Utils.f(entry);
    }
}
