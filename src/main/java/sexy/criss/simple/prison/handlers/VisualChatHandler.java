/*******************************************************
 * Copyright (C) 2016-2018 D3EVER <root@d3ever.cf>
 *
 * This file is part of SexyCode.
 *
 * sexy can not be copied and/or distributed without the express
 * permission of D3EVER
 *
 * Date: 29/09/2018 - 17:14 суббота
 *
 *******************************************************/
package sexy.criss.simple.prison.handlers;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import sexy.criss.simple.prison.PrisonPlayer;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.SexyText;
import sexy.criss.simple.prison.utils.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class VisualChatHandler extends SexyEvent {

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(e.isCancelled()) return;
        PrisonPlayer pp = PrisonPlayer.getPrisonPlayer(p);
        e.setCancelled(true);

        p.sendMessage(e.getMessage());
        Set<Player> notify_set = Utils.getAllPlayers();

        if(notify_set.size() < 2) {
            p.sendMessage(Utils.f("&cВас никто не слышит."));
            return;
        }

        String f = "\n&r";
        final String late = e.getMessage();
        notify_set.forEach(t -> {
                    SexyText text = new SexyText();
                    text.addHoverableText(
                             p.getDisplayName(),
                            "&7Статистика игрока &c" + p.getName() + f + f +
                                    "&7Уровень: &e" + pp.getLevel() + f +
                                    "&7Фракция: &c" + (pp.hasFaction() ? pp.getFaction().getName() : "нет") + f +
                                    "&7Баланс: &e" + Utils.format(pp.getBalance()) + " ⛃" + f +
                                    "&7Убийств: &e" + pp.getKills() + f +
                                    "&7Смертей: &e" + pp.getDeaths() + f + f +
                                    "&7Множители:" + f +
                                    "&7  Деньги: &cx" + pp.getMoneyMultiplier() + f +
                                    "&7  Блоки: &cx" + pp.getBlocksMultiplier() + f +
                                    "&7  Ключи: &cx" + pp.getKeysMultiplier());
                    text.addText("&8 [" + (pp.hasFaction() ? pp.getFaction().getPrefix() : "") +"&6LVL " + pp.getLevel() + "&8]: &r" + late)
                    .close().send(t);
                }
        );

    }

}
