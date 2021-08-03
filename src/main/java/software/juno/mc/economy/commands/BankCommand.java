package software.juno.mc.economy.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.models.entities.PlayerData;

public class BankCommand extends BaseCommand {

    public BankCommand(MConomy mConomy) {
        super(mConomy);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        Player player = (Player) commandSender;
        PlayerData playerID = MConomy.db.getPlayerDAO().findByName(player.getName());

        if ("banco".equalsIgnoreCase(command.getName())) {

            if (args.length == 0) {
                player.sendMessage("Você tem " + playerID.getMoney() + " esmeraldas no banco!");
            } else if (args.length == 2) {

                String action = args[0];
                int qtd = Integer.parseInt(args[1]);

                if ("sacar".equalsIgnoreCase(action) && MConomy.db.getPlayerDAO().chargePlayer(player, qtd)) {
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, qtd));
                    player.sendMessage("Você sacou " + qtd + " do banco e tem " + (playerID.getMoney() - qtd) + " esmeraldas na conta!");
                } else if ("depositar".equalsIgnoreCase(action) && player.getInventory().contains(Material.EMERALD)) {
                    player.getInventory().remove(new ItemStack(Material.EMERALD, qtd));
                    MConomy.db.getPlayerDAO().cashPlayer(player, qtd);
                    PlayerData playerData = MConomy.db.getPlayerDAO().findByPlayer(player);
                    player.sendMessage("Você depositou " + qtd + " no banco e agora tem " + playerData.getMoney() + " esmeraldas!");
                } else {
                    return false;
                }

            } else {
                return false;
            }

            return true;
        }

        return false;
    }
}
