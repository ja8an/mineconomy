package software.juno.mc.economy.daos;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.bukkit.entity.Player;
import software.juno.mc.economy.models.entities.PlayerData;

import java.sql.SQLException;
import java.util.logging.Logger;

public class DB {

    // create a connection source to our database
    final ConnectionSource connectionSource;
    final Logger logger;

    final PlayerDAO playerDAO;

    DB(String databaseUrl, Logger logger) throws SQLException {
        this.connectionSource = new JdbcConnectionSource(databaseUrl);
        this.logger = logger;
        this.playerDAO = new PlayerDAO(connectionSource, PlayerData.class, logger);
    }

    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public static DB connect(String databaseUrl, Logger logger) throws SQLException {
        return new DB(databaseUrl, logger);
    }
}
