package software.juno.mc.economy;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import software.juno.mc.economy.annotations.CommandExecutor;
import software.juno.mc.economy.annotations.Listener;
import software.juno.mc.economy.annotations.Scheduled;
import software.juno.mc.economy.commands.BankCommand;
import software.juno.mc.economy.commands.BaseCommand;
import software.juno.mc.economy.commands.TransportCommand;
import software.juno.mc.economy.daos.DB;
import software.juno.mc.economy.listeners.*;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.schedulers.BaseScheduler;
import software.juno.mc.economy.schedulers.SalaryScheduler;
import software.juno.mc.economy.utils.ItemUtils;
import software.juno.mc.economy.utils.PlayerUtils;

import java.sql.SQLException;
import java.util.*;

public class MConomy extends JavaPlugin {

    public static DB db;

    public MConomy() throws SQLException {
        super();
        MConomy.db = DB.connect("jdbc:sqlite:data.db", getLogger());
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        getLogger().info("onEnable has been invoked!");

        Reflections reflections = new Reflections("software.juno.mc.economy");

        Set<Class<? extends BaseListener>> listeners = reflections.getSubTypesOf(BaseListener.class);

        for (Class<? extends BaseListener> listener : listeners) {
            getLogger().info("Listener " + listener.getSimpleName() + " carregado com sucesso!");
            getServer().getPluginManager().registerEvents(listener.getDeclaredConstructor(MConomy.class).newInstance(this), this);
        }

        Set<Class<? extends BaseCommand>> commands = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<? extends BaseCommand> command : commands) {
            getLogger().info("Command executor " + command.getSimpleName() + " carregado com sucesso!");
            CommandExecutor commandExecutor = command.getAnnotation(CommandExecutor.class);
            String time = commandExecutor != null ? commandExecutor.value() : command.getSimpleName().replace("Command", "").toLowerCase(Locale.ROOT);
            PluginCommand pluginCommand = getCommand(time);
            if (pluginCommand != null)
                pluginCommand.setExecutor(command.getDeclaredConstructor(MConomy.class).newInstance(this));
        }

        Set<Class<? extends BaseScheduler>> schedulers = reflections.getSubTypesOf(BaseScheduler.class);

        for (Class<? extends BaseScheduler> scheduler : schedulers) {
            getLogger().info("Scheduler " + scheduler.getSimpleName() + " carregado com sucesso!");
            Scheduled scheduled = scheduler.getAnnotation(Scheduled.class);
            int time = scheduled != null ? scheduled.value() : 1000;
            getServer().getScheduler().runTaskTimerAsynchronously(this, scheduler.getDeclaredConstructor(MConomy.class).newInstance(this), 0, time);
        }

    }

}
