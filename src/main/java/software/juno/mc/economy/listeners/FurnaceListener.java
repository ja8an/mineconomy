package software.juno.mc.economy.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.models.enums.Profession;

import java.util.List;

public class FurnaceListener extends BaseListener {

    public FurnaceListener(MConomy mConomy) {
        super(mConomy);
    }

    @EventHandler
    public void permissions(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (isGod(player)) return;

        getLogger().info("Player in inventory " + player.getName());
        Profession profession = MConomy.getProfession(player.getName());

        getLogger().info("Inventory type " + e.getClickedInventory().getType());
        getLogger().info("From " + e.getView().getType());
        getLogger().info("Item " + e.getCurrentItem().getType());
        getLogger().info("To player" + e.getView().getPlayer());

        ItemStack itemStack = e.getCurrentItem();

        if (itemStack != null && InventoryType.FURNACE.equals(e.getClickedInventory().getType()) && !profession.canSmelt(itemStack.getType())) {
            e.setCancelled(true);
            player.sendMessage("Você não pode usar este item!");
        }
    }
}
