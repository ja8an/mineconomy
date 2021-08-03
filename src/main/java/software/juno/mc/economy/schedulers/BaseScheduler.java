package software.juno.mc.economy.schedulers;

import software.juno.mc.economy.BaseApp;
import software.juno.mc.economy.MConomy;

public abstract class BaseScheduler extends BaseApp implements Runnable {
    public BaseScheduler(MConomy mConomy) {
        super(mConomy);
    }
}
