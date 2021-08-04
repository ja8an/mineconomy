package software.juno.mc.economy.listeners;

import org.bukkit.event.Listener;
import software.juno.mc.economy.BaseApp;
import software.juno.mc.economy.MConomy;

public abstract class BaseListener extends BaseApp implements Listener {

    public BaseListener(MConomy mConomy) {
        super(mConomy);
    }

}
