package sexy.criss.simple.prison.handlers;

import com.google.common.collect.Maps;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import sexy.criss.simple.prison.utils.SexyEvent;
import sexy.criss.simple.prison.utils.Utils;

import java.util.Map;

public class ShopHandler extends SexyEvent {

    public static Map<ItemStack, Integer> price_map = Maps.newHashMap();

    @EventHandler
    public void onClick(PlayerInteractEntityEvent e) {
        make_sell(e);
    }

    @EventHandler
    public void onNPCClick(NPCRightClickEvent e) {
        make_sell(e);
    }

    private void make_sell(Event event) {
        if(event instanceof NPCRightClickEvent) {
            NPCRightClickEvent e = (NPCRightClickEvent) event;
            NPC npc = e.getNPC();
            if(!(npc.getName().equals(Utils.f("&aСкупщик")))) return;
            Utils.sellInventory(e.getClicker());
            return;
        }

        if(event instanceof PlayerInteractEntityEvent) {
            PlayerInteractEntityEvent e = (PlayerInteractEntityEvent) event;
            Player p = e.getPlayer();
            if(!(e.getRightClicked().getType().equals(EntityType.ITEM_FRAME))) return;
            ItemFrame frame = (ItemFrame) e.getRightClicked();
            if(!(frame.getItem().getType().equals(Material.EMERALD))) return;
            e.setCancelled(true);

            Utils.sellInventory(p);
        }

    }

}
