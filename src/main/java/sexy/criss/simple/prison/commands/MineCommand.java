package sexy.criss.simple.prison.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sexy.criss.simple.prison.Main;
import sexy.criss.simple.prison.PrisonMine;
import sexy.criss.simple.prison.utils.ACommand;
import sexy.criss.simple.prison.utils.Item;
import sexy.criss.simple.prison.utils.MenuUtils;
import sexy.criss.simple.prison.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MineCommand extends ACommand {

    public MineCommand() {
        super("mine", "mines", "shaft");
        this.unavailableFromConsole();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        int mineItems = 0;
        int locItems = 0;
        Iterator var9 = Main.mines_storage.getConfigurationSection("mines").getKeys(false).iterator();

        while(var9.hasNext()) {
            String key = (String)var9.next();
            if (!Main.mines_storage.contains("mines." + key + ".level")) ++locItems;
            else ++mineItems;

        }

        int mineSlots = Utils.slotCounter(mineItems);
        int locSlots = Utils.slotCounter(locItems);
        Inventory inventory = Bukkit.createInventory(null, mineSlots + 9 + locSlots, Main.MINES_MENU_NAME);

        int minesCount;
        for(minesCount = 0; minesCount < inventory.getSize(); ++minesCount) {
            inventory.setItem(minesCount, Item.getItem(Material.STAINED_GLASS_PANE, 1, (short) 15, " "));
        }

        minesCount = 0;
        int locsCount = mineSlots + 9;
        Iterator it = PrisonMine.mines.iterator();

        while(it.hasNext()) {
            PrisonMine mine = (PrisonMine) it.next();
            String boss = mine.getBoss();
            String description = mine.getDescription();
            String ingameName = mine.getIngame();
            Integer level = mine.getLevel();
            Material material = mine.getMaterial();
            PrisonMine.MineType type = mine.getType();
            List<String> lore = new ArrayList();
            lore.add("§b" + description);
            lore.add("");
            if (type == PrisonMine.MineType.MINE) {
                lore.add("§bУровень: §a" + level);
            }

            lore.add("§bБосс: §a" + boss);
            ItemStack mineItem = Item.getItem(material, 1, (short) 0, "§a" + ingameName, lore);
            if (type == PrisonMine.MineType.MINE) {
                inventory.setItem(minesCount, mineItem);
                ++minesCount;
            }

            if (type == PrisonMine.MineType.LOCATION) {
                inventory.setItem(locsCount, mineItem);
                ++locsCount;
            }
        }

        ItemStack stack = new ItemStack(Material.EMERALD);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Донат шахты");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Нажмите, чтобы открыть", ChatColor.GRAY + "интерфейс донат-шахт."));
        stack.setItemMeta(meta);
        inventory.setItem(inventory.getSize() - 1, stack);
        p.openInventory(inventory);
        return true;
    }
}
