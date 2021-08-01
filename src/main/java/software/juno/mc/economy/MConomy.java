package software.juno.mc.economy;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import software.juno.mc.economy.daos.DB;
import software.juno.mc.economy.listeners.InitializerListener;
import software.juno.mc.economy.listeners.ProfessionListener;
import software.juno.mc.economy.listeners.WizardProfession;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
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
        getServer().getPluginManager().registerEvents(new InitializerListener(getLogger()), this);
        getServer().getPluginManager().registerEvents(new ProfessionListener(getLogger()), this);
        getServer().getPluginManager().registerEvents(new WizardProfession(getLogger()), this);
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

        if (command.getName().equalsIgnoreCase("banco")) {

            if (args.length == 0) {
                player.sendMessage("Você tem " + playerID.getMoney() + " esmeraldas no banco!");
            } else if (args.length >= 2) {

                String action = args[0];
                int qtd = Integer.parseInt(args[1]);

                getLogger().info("Action " + action);
                getLogger().info("QTd " + qtd);

                if ("sacar".equalsIgnoreCase(action) && db.getPlayerDAO().chargePlayer(player, qtd)) {
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, qtd));
                    player.sendMessage("Você sacou " + qtd + " do banco e tem " + playerID.getMoney() + " esmeraldas na conta!");
                } else if ("depositar".equalsIgnoreCase(action) && player.getInventory().contains(Material.EMERALD)) {
                    player.getInventory().remove(new ItemStack(Material.EMERALD, qtd));
                    db.getPlayerDAO().cashPlayer(player, qtd);
                } else {
                    player.sendMessage("Você não tem esmeraldas suficientes!");
                }

            }

            return true;
        } else if (command.getName().equalsIgnoreCase("criar")) {
            String oque = args[0];
            if ("keeper".equalsIgnoreCase(oque)) {

                if (playerID.getVillager() != null) {
                    Villager vil = (Villager) getServer().getEntity(playerID.getVillager());
                    if (vil != null)
                        vil.teleport(player);
                    return true;
                }

                Profession profession = playerID.getProfession();

                Villager villager = (Villager) Objects.requireNonNull(player.getLocation().getWorld()).spawnEntity(player.getLocation(), EntityType.VILLAGER);
                villager.setCustomName(profession.getLabel());
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
                int qtd = Integer.parseInt(args.length > 2 ? args[2] : "0");
                Material m = Material.valueOf(item.toUpperCase(Locale.ROOT));
                db.getPlayerDAO().addToPlayerInventory(player, new ItemStack(m, qtd));
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

        } else if ("ctp".equalsIgnoreCase(command.getName())) {
            if (db.getPlayerDAO().chargePlayer(player, 0)) {
                player.getInventory().addItem(ItemUtils.transportTicket());
            } else {
                player.sendMessage("Você não tem esmeraldas suficientes!");
            }
        } else if ("transporte".equalsIgnoreCase(command.getName())) {
            String playerName = args[0];
            Player outroPlayer = getServer().getPlayer(playerName);

            if (player.equals(outroPlayer)) {
                player.sendMessage("Você não precisa se teleportar a si mesmo!");
                return false;
            }

            if (outroPlayer != null) {

                if (Profession.WIZARD.equals(playerID.getProfession())) {
                    player.teleport(outroPlayer);
                    return true;
                }

                if (player.getInventory().contains(Material.PAPER)) {
                    Optional<ItemStack> stack = Arrays.stream(player.getInventory().getContents()).filter(itemStack -> "Ticket de transporte".equalsIgnoreCase(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())).findFirst();
                    if (stack.isPresent()) {
                        player.getInventory().remove(stack.get());
                        player.teleport(outroPlayer);
                        return true;
                    } else {
                        player.sendMessage("Você não tem nenhum ticket de transporte!");
                        return false;
                    }
                }
            } else {
                player.sendMessage("Player de destino não encontrado!");
                return false;
            }
        }

        return false;

    }


}
