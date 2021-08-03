package software.juno.mc.economy.daos;

import com.j256.ormlite.support.ConnectionSource;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.PlayerUtils;

import java.sql.SQLException;
import java.util.logging.Logger;

public class PlayerDAO extends BaseDAO<PlayerData, String> {

    protected PlayerDAO(ConnectionSource connectionSource, Class<PlayerData> playerDataClass, Logger logger) throws SQLException {
        super(connectionSource, playerDataClass, logger);
    }


    /**
     * Find player data by username
     *
     * @param name Player's username
     * @return Player's data
     */
    public PlayerData findByName(String name) {
        logger.info("FindByName " + name);
        PlayerData playerData = findById(name);
        logger.info("PlayerFound " + playerData);
        return playerData;
    }

    /**
     * Finds player data by player object
     *
     * @param player Current player
     * @return Player's data
     */
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
            PlayerUtils.addStartItems(player, Profession.UNEMPLOYED);
            return findByName(name);
        }
        return playerData;
    }

    /**
     * Credits certain amount of emeralds to player
     *
     * @param player Current player
     * @param amount Amount of emeralds
     */
    public void cashPlayer(Player player, int amount) {
        PlayerData playerData = findById(player.getName());
        playerData.setMoney(playerData.getMoney() + amount);
        update(playerData);
    }

    /**
     * Charges the player in defined amount of emeralds
     *
     * @param player Current player
     * @param amount Amount of emeralds
     * @return true if the player was successfully charged
     */
    public boolean chargePlayer(Player player, int amount) {
        PlayerData playerData = findById(player.getName());
        if (playerData.getMoney() >= amount) {
            playerData.setMoney(playerData.getMoney() - amount);
            update(playerData);
            return true;
        }
        return false;
    }


}
