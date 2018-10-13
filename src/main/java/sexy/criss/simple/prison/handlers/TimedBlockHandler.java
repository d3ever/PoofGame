package sexy.criss.simple.prison.handlers;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.manager.Reference;
import sexy.criss.simple.prison.manager.builders.TimedBlock;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Map;

public class TimedBlockHandler extends SexyEvent {

    private static Map<Player, Location> playerLocation = Maps.newHashMap();

    @EventHandler
            (priority = EventPriority.MONITOR)
    public void onBlockBreakTwo(BlockBreakEvent e) {
        if(e.isCancelled()) return;
        Player p = e.getPlayer();

        if(!TimedBlock.isTimedBlock(e.getBlock().getLocation())) {
            if(p.hasPermission("prison.admin") || p.isOp()) return;
            e.setCancelled(true);
            return;
        }

        TimedBlock tb = TimedBlock.getTimedBlockByLoc(e.getBlock().getLocation());
        if(TimedBlock.players_activity.containsKey(p)) {
            tb.remove();
            p.sendMessage(Utils.f("&7Блок был удалён."));
            return;
        }

        if(playerLocation.containsKey(p) && playerLocation.get(p).equals(tb.getLocation())) {
            p.sendMessage(Utils.f("&cНе ломай один и тот же блок."));
            p.damage(0.0);
            Utils.play(p, Sound.CREEPER_HISS);
            e.setCancelled(true);
            return;
        } else playerLocation.put(p, tb.getLocation());


        switch (e.getBlock().getType()) {
            case LEAVES:
                p.getInventory().addItem(new ItemStack(Material.LEAVES));
                break;
            case LEAVES_2:
                p.getInventory().addItem(new ItemStack(Material.LEAVES));
                break;
            case GRASS:
                p.getInventory().addItem(new ItemStack(Material.DIRT));
                break;
            default:
                p.getInventory().addItem(new ItemStack(e.getBlock().getType(), 1, (short) 0, e.getBlock().getData()));
        }
        e.getBlock().setType(Material.AIR);
        Utils.play(p, Sound.BAT_HURT);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), tb::updateBlock, tb.getInterval());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission("prison.admin") || !p.isOp()) {
            p.sendMessage(Utils.f("&7Извините, у вас недосточно прав, чтобы использовать это."));
            e.setCancelled(true);
            return;
        }

        if(TimedBlock.players_activity.containsKey(p)) {
            new TimedBlock(e.getBlockPlaced().getLocation(), e.getBlockPlaced().getType(), e.getBlockPlaced().getData(), TimedBlock.players_activity.get(p));
            p.sendMessage(Utils.f("&7Блок был установлен."));
        }

    }

}
