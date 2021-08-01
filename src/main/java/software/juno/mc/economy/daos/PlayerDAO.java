package software.juno.mc.economy.daos;

import com.j256.ormlite.support.ConnectionSource;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;

import java.sql.SQLException;
import java.util.logging.Logger;

public class PlayerDAO extends BaseDAO<PlayerData, String> {

    protected PlayerDAO(ConnectionSource connectionSource, Class<PlayerData> playerDataClass, Logger logger) throws SQLException {
        super(connectionSource, playerDataClass, logger);
    }

    public PlayerData findByName(String name) {
        logger.info("FindByName " + name);
        PlayerData playerData = findById(name);
        logger.info("PlayerFound " + playerData);
        return playerData;
    }

    @SneakyThrows
    public PlayerData findByPlayer(Player player) {
        String name = player.getName();
        PlayerData playerData = findByName(name);
        if (playerData == null) {
            logger.info("Player not found... creating...");
            playerData = new PlayerData();
            playerData.setName(name);
            playerData.setMoney(0);
            playerData.setProfession(Profession.UNEMPLOYED);
            create(playerData);
            addToPlayerInventory(player, Profession.UNEMPLOYED.getStartItems().toArray(new ItemStack[0]));
            return findByName(name);
        }
        return playerData;
    }

    public void cashPlayer(Player player, int amount) {
        PlayerData playerData = findById(player.getName());
        playerData.setMoney(playerData.getMoney() + amount);
        update(playerData);
    }

    public boolean chargePlayer(Player player, int amount) {
        PlayerData playerData = findById(player.getName());
        if (playerData.getMoney() >= amount) {
            playerData.setMoney(playerData.getMoney() - amount);
            update(playerData);
            return true;
        }
        return false;
    }

    public void addToPlayerInventory(Player player, ItemStack... itemStack) {
        Inventory inventory = player.getInventory();
        if (itemStack != null)
            for (ItemStack stack : itemStack) {
                inventory.addItem(stack);
            }
    }

    public void addToPlayerInventoryIfNotContains(Player player, ItemStack... itemStack) {
        Inventory inventory = player.getInventory();
        if (itemStack != null)
            for (ItemStack stack : itemStack) {
                if (!inventory.contains(stack.getType()))
                    inventory.addItem(stack);
            }
    }

}
