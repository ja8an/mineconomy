package software.juno.mc.economy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.juno.mc.economy.BaseApp;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.exceptions.CommandException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public abstract class BaseCommand extends BaseApp implements CommandExecutor {


    public BaseCommand(MConomy mConomy) {
        super(mConomy);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String name = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "index";

        List<Class<?>> typed = new ArrayList<>();
        typed.add(Player.class);

        List<Object> _args = new ArrayList<>();
        Player player = (Player) sender;
        _args.add(player);

        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                _args.add(args[i]);
                typed.add(args[i].getClass());
            }
        }

        getLogger().info("Method " + name);
        getLogger().info("Args " + _args);
        getLogger().info("Types " + typed);

        try {


            Method declaredMethod;

            try {
                declaredMethod = getClass().getDeclaredMethod(name, typed.toArray(new Class[0]));
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, e.getMessage());

                _args.clear();
                typed.clear();

                for (int i = 0; i < args.length; i++) {
                    if (i == 0) {
                        _args.add(player);
                        typed.add(Player.class);
                    }
                    _args.add(args[i]);
                    typed.add(args[i].getClass());
                }

                getLogger().info("Method " + name + " doesn't exist, trying index");

                getLogger().info("Method " + "index");
                getLogger().info("Args " + _args);
                getLogger().info("Types " + typed);

                declaredMethod = getClass().getDeclaredMethod("index", typed.toArray(new Class[0]));
            }


            Object response = declaredMethod.invoke(this, _args.toArray(new Object[0]));

            getLogger().info("Response " + response);

            // This should never happen, it's just to remove the warning
            if ("THROWS_FUCKING_EXCEPTION".equalsIgnoreCase(String.valueOf(response)))
                throw new CommandException(String.valueOf(response));

            if (Void.class.equals(declaredMethod.getReturnType())) {
                return true;
            } else {
                return Boolean.parseBoolean(String.valueOf(response));
            }

        } catch (CommandException commandException) {
            sender.sendMessage(commandException.getMessage());
            return true;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage());
        }
        return false;
    }
}
