package software.juno.mc.economy.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemUtils {

    public static final String TRANSPORT_TICKET = "Feitiço de Transporte";

    private ItemUtils() {
    }

    public static ItemStack transportTicket() {
        return transportTicket(1);
    }

    public static ItemStack transportTicket(int qtd) {
        ItemStack stack = new ItemStack(Material.PAPER, qtd);

        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(TRANSPORT_TICKET);
            meta.setLore(Arrays.asList("1 uso", "Use com /t [player]"));
            meta.setUnbreakable(true);
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public static ItemStack chargeBlock(Player destination, int amount) {
        ItemStack itemStack = new ItemStack(Material.PAPER);

        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("Cobrança");
            meta.setLore(Arrays.asList(destination.getName(), String.valueOf(amount)));
            meta.setUnbreakable(true);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
