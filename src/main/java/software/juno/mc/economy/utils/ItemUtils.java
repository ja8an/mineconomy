package software.juno.mc.economy.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemUtils {
    private ItemUtils() {
    }

    public static ItemStack transportTicket() {
        ItemStack stack = new ItemStack(Material.PAPER);

        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("Ticket de Transporte");
            meta.setLore(Arrays.asList("1 uso", "Use com /tp [playername]"));
            meta.setUnbreakable(true);
            stack.setItemMeta(meta);
        }

        return stack;
    }
}
