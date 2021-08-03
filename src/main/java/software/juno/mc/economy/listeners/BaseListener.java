package software.juno.mc.economy.listeners;

import org.bukkit.Server;
import org.bukkit.entity.MagmaCube;
import org.bukkit.event.Listener;
import software.juno.mc.economy.MConomy;

import java.util.List;
import java.util.logging.Logger;

public abstract class BaseListener implements Listener {

    protected final MConomy mConomy;

    public BaseListener(MConomy mConomy) {
        this.mConomy = mConomy;
    }

    public Logger getLogger() {
        return mConomy.getLogger();
    }

    public Server getServer() {
        return mConomy.getServer();
    }

}
