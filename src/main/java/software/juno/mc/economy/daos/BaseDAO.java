package software.juno.mc.economy.daos;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;
import software.juno.mc.economy.models.entities.PlayerData;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public abstract class BaseDAO<T, U> {

    protected final Dao<T, U> dao;
    protected final Logger logger;

    BaseDAO(ConnectionSource connectionSource, Class<T> tClass, Logger logger) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, tClass);
        this.logger = logger;
        TableUtils.createTableIfNotExists(connectionSource, tClass);
    }

    @SneakyThrows
    public T findById(U id) {
        return dao.queryForId(id);
    }

    @SneakyThrows
    public List<T> findAll() {
        return dao.queryForAll();
    }

    @SneakyThrows
    public void create(T entity) {
        dao.create(entity);
    }

    @SneakyThrows
    public void update(T entity) {
        dao.update(entity);
    }

}
