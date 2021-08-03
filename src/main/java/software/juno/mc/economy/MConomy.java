package software.juno.mc.economy;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import software.juno.mc.economy.commands.BankCommand;
import software.juno.mc.economy.commands.TransportCommand;
import software.juno.mc.economy.daos.DB;
import software.juno.mc.economy.listeners.ChargeListener;
import software.juno.mc.economy.listeners.FurnaceListener;
import software.juno.mc.economy.listeners.InitializerListener;
import software.juno.mc.economy.listeners.ProfessionListener;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.schedulers.SalaryScheduler;
import software.juno.mc.economy.utils.ItemUtils;

import java.util.*;

public class MConomy extends JavaPlugin implements Listener {

    public static DB db;

    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        getLogger().info("onEnable has been invoked!");

        db = DB.connect("jdbc:sqlite:data.db", getLogger());

        getServer().getPluginManager().registerEvents(new InitializerListener(this), this);
        getServer().getPluginManager().registerEvents(new ProfessionListener(this), this);
        getServer().getPluginManager().registerEvents(new ChargeListener(this), this);
        getServer().getPluginManager().registerEvents(new FurnaceListener(this), this);

        Objects.requireNonNull(getCommand("banco")).setExecutor(new BankCommand(this));
        Objects.requireNonNull(getCommand("t")).setExecutor(new TransportCommand(this));

        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new SalaryScheduler(this), 0, 6000);

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

        if ("cobrar".equalsIgnoreCase(command.getName())) {
            int qtd = Integer.parseInt(args[0]);
            player.getInventory().addItem(ItemUtils.chargeBlock(player, qtd));
            return true;
        } else if (command.getName().equalsIgnoreCase("criar")) {
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
                String item = args[1];
                int qtd = Integer.parseInt(args.length > 2 ? args[2] : "1");
                Material m = Material.valueOf(item.toUpperCase(Locale.ROOT));
                db.getPlayerDAO().addToPlayerInventory(player, new ItemStack(m, qtd));
            }
        } else if ("setjob".equalsIgnoreCase(command.getName())) {
            if (args != null && args.length != 1) {
                player.sendMessage("Você precisa dizer qual profissão");
                return false;
            }
            Profession prof = Profession.valueOf(args[0]);
            Profession profAtual = getProfession(player.getName());

            if (Profession.UNEMPLOYED.equals(profAtual)) {
                playerID.setProfession(prof);
                db.getPlayerDAO().addToPlayerInventoryIfNotContains(player, prof.getStartItems().toArray(new ItemStack[0]));
                db.getPlayerDAO().update(playerID);
                return true;
            } else {
                if (db.getPlayerDAO().chargePlayer(player, 10000)) {
                    playerID.setProfession(prof);
                    db.getPlayerDAO().addToPlayerInventoryIfNotContains(player, prof.getStartItems().toArray(new ItemStack[0]));
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
