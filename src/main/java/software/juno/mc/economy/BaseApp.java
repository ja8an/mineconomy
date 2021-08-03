package software.juno.mc.economy;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import software.juno.mc.economy.utils.PlayerUtils;

import java.util.logging.Logger;

public abstract class BaseApp {

    final MConomy mConomy;

    public BaseApp(MConomy mConomy) {
        this.mConomy = mConomy;
    }

    public Logger getLogger() {
        return mConomy.getLogger();
    }

    public Server getServer() {
        return mConomy.getServer();
    }

    public boolean isGod(Player player) {
        return player != null && PlayerUtils.isGod(player);
    }

}
