/*******************************************************
 * Copyright (C) 2016-2018 D3EVER <root@d3ever.cf>
 *
 * This file is part of SexyCode.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of D3EVER
 *
 * Date: 29/09/2018 - 17:12 суббота
 *
 *******************************************************/
package sexy.criss.simple.prison.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SexyText {
    private String text;

    public SexyText() {
        this.text = "{\"text\":\"" + "" + "\"" + ",\"extra\":[" + "{\"text\":\"" + "" + "\"}";
    }

    public SexyText addText(String message) {
        this.text = text + (",{\"text\":\"" + Utils.f(message) + "\"}");
        return this;
    }

    public void addHoverableText(String hoverable, String message) {
        this.text = text + ",{\"text\":\"" + Utils.f(hoverable) + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + Utils.f(message) + "\"}}";
    }

    public SexyText close() {
        this.text = this.text + "]}";
        return this;
    }

    public void send(Player player) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(this.text));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
