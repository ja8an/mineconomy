package software.juno.mc.economy.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.models.enums.Profession;

public class PlayerUtils {

    private PlayerUtils() {
    }

    public static boolean isGod(Player player) {
        return GameMode.CREATIVE.equals(player.getGameMode());
    }


    public static void addToPlayerInventory(Player player, ItemStack... itemStacks) {
        addToPlayerInventory(player, false, itemStacks);
    }

    /**
     * Adds the itemStacks to player inventory depending on parameter
     *
     * @param player          Current player
     * @param checkIfContains true if inventory must not contain the items before being added
     * @param itemStack       Contents that need to be added
     */
    public static void addToPlayerInventory(Player player, boolean checkIfContains, ItemStack... itemStack) {
        Inventory inventory = player.getInventory();
        if (itemStack != null)
            for (ItemStack stack : itemStack) {
                if (!checkIfContains || !inventory.contains(stack.getType()))
                    inventory.addItem(stack);
            }
    }

    public static void addToPlayerInventoryIfNotContains(Player player, ItemStack... itemStack) {
        addToPlayerInventory(player, true, itemStack);
    }

    public static void addStartItems(Player player, Profession profession) {
        addToPlayerInventoryIfNotContains(player, profession.getStartItems().toArray(new ItemStack[0]));
    }

}
