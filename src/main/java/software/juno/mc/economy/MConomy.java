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
            getServer().getPluginManager().registerEvents(listener.getDeclaredConstructor(MConomy.class).newInstance(this), this);
        }

        Set<Class<? extends BaseCommand>> commands = reflections.getSubTypesOf(BaseCommand.class);

        for (Class<? extends BaseCommand> command : commands) {
            CommandExecutor commandExecutor = command.getAnnotation(CommandExecutor.class);
            String time = commandExecutor != null ? commandExecutor.value() : command.getSimpleName().replace("Command", "").toLowerCase(Locale.ROOT);
            PluginCommand pluginCommand = getCommand(time);
            if (pluginCommand != null)
                pluginCommand.setExecutor(command.getDeclaredConstructor(MConomy.class).newInstance(this));
        }

        Set<Class<? extends BaseScheduler>> schedulers = reflections.getSubTypesOf(BaseScheduler.class);

        for (Class<? extends BaseScheduler> scheduler : schedulers) {
            Scheduled scheduled = scheduler.getAnnotation(Scheduled.class);
            int time = scheduled != null ? scheduled.value() : 100;
            getServer().getScheduler().runTaskTimerAsynchronously(this, scheduler.getDeclaredConstructor(MConomy.class).newInstance(this), 0, time);
        }

    }

    @SneakyThrows
    public static Profession getProfession(String name) {
        PlayerData playerID = db.getPlayerDAO().findByName(name);
        if (playerID == null) {
            return Profession.UNEMPLOYED;
        }
        return playerID.getProfession();
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        getLogger().info("Issued command");

        getLogger().info("Command " + command.getName());
        getLogger().info("Label " + label);
        getLogger().info("Args " + Arrays.asList(args));

        Player player = (Player) sender;
        PlayerData playerID = db.getPlayerDAO().findByName(player.getName());

        if (command.getName().equalsIgnoreCase("criar")) {
            String oque = args[0];
            if ("keeper".equalsIgnoreCase(oque)) {

                if (playerID.getVillager() != null) {
                    getLogger().info("Já existe, teleportando");
                    Villager vil = (Villager) getServer().getEntity(playerID.getVillager());
                    if (vil != null) {
                        vil.setInvulnerable(false);
                        vil.setAI(true);
                        vil.setHealth(0);
                        vil.damage(Double.MAX_VALUE);
                    }
                }

                getLogger().info("Não existe, criando");

                Profession profession = playerID.getProfession();

                Villager villager = (Villager) Objects.requireNonNull(player.getLocation().getWorld()).spawnEntity(player.getLocation(), EntityType.VILLAGER);
                villager.setCustomName(String.format("%s - %s", profession.getLabel(), player.getName()));
                villager.setProfession(profession.getDoll());
                villager.setCollidable(false);
                villager.setCanPickupItems(false);
                villager.setAI(false);
                villager.setInvulnerable(true);

                playerID.setVillager(villager.getUniqueId());
                db.getPlayerDAO().update(playerID);

                villager.setRecipes(profession.getMerchantRecipes());

                return true;
            } else if ("item".equalsIgnoreCase(oque)) {

                if (!PlayerUtils.isGod(player)) return false;

                String item = args[1];
                int qtd = Integer.parseInt(args.length > 2 ? args[2] : "1");
                Material m = Material.valueOf(item.toUpperCase(Locale.ROOT));
                PlayerUtils.addToPlayerInventory(player, new ItemStack(m, qtd));
            }
        } else if ("setjob".equalsIgnoreCase(command.getName())) {

            if (args.length != 1) {
                player.sendMessage("Você precisa dizer qual profissão");
                return false;
            }

            Profession prof = Profession.valueOf(args[0]);
            Profession profAtual = getProfession(player.getName());

            if (Profession.UNEMPLOYED.equals(profAtual)) {
                playerID.setProfession(prof);
                PlayerUtils.addStartItems(player, prof);
                db.getPlayerDAO().update(playerID);
                return true;
            } else {
                if (PlayerUtils.isGod(player) && db.getPlayerDAO().chargePlayer(player, 10000)) {
                    playerID.setProfession(prof);
                    PlayerUtils.addStartItems(player, prof);
                    db.getPlayerDAO().update(playerID);
                    return true;
                } else {
                    player.sendMessage("Você não tem esmeraldas suficientes no banco para realizar a troca de profissão!");
                    return false;
                }
            }

        }

        return false;

    }


}
