package software.juno.mc.economy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.Listener;
import software.juno.mc.economy.models.enums.Profession;

import java.util.Objects;

@Listener
public class InventoryListener extends BaseListener {

    public InventoryListener(MConomy mConomy) {
        super(mConomy);
    }

    @EventHandler
    public void permissions(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (isGod(player)) return;

        getLogger().info("Player in inventory " + player.getName());
        Profession profession = getProfession(player.getName());

        if (Objects.nonNull(e.getClickedInventory()))
            getLogger().info("Inventory type " + e.getClickedInventory().getType());
        getLogger().info("From " + e.getView().getType());
        if (Objects.nonNull(e.getCurrentItem()))
            getLogger().info("Item " + e.getCurrentItem().getType());
        getLogger().info("To player" + e.getView().getPlayer());

        ItemStack itemStack = e.getCurrentItem();

        if (itemStack != null && InventoryType.FURNACE.equals(e.getClickedInventory().getType()) && !profession.canSmelt(itemStack.getType())) {
            e.setCancelled(true);
            player.sendMessage("Você não pode usar este item!");
        }
    }
}
