package software.juno.mc.economy.listeners;

import org.bukkit.Server;
import org.bukkit.entity.MagmaCube;
import org.bukkit.event.Listener;
import software.juno.mc.economy.BaseApp;
import software.juno.mc.economy.MConomy;

import java.util.List;
import java.util.logging.Logger;

public abstract class BaseListener extends BaseApp implements Listener {

    public BaseListener(MConomy mConomy) {
        super(mConomy);
    }

}
