package software.juno.mc.economy.commands;

import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.MagmaCube;
import software.juno.mc.economy.BaseApp;
import software.juno.mc.economy.MConomy;

import java.util.logging.Logger;

public abstract class BaseCommand extends BaseApp implements CommandExecutor {

    public BaseCommand(MConomy mConomy) {
        super(mConomy);
    }

}
