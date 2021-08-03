package software.juno.mc.economy.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.BaseApp;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.CommandExecutor;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.utils.ItemUtils;

@CommandExecutor("banco")
public class BankCommand extends BaseCommand {

    public BankCommand(MConomy mConomy) {
        super(mConomy);
    }

    public void index(Player player) {
        int qtd = MConomy.db.getPlayerDAO().findByPlayer(player).getMoney();
        player.sendMessage("Você tem " + qtd + " esmeraldas");
    }

    public void sacar(Player player, String _qtd) {
        int qtd = Integer.parseInt(_qtd);
        PlayerData playerID = MConomy.db.getPlayerDAO().findByPlayer(player);

        if (MConomy.db.getPlayerDAO().chargePlayer(player, qtd)) {
            player.getInventory().addItem(new ItemStack(Material.EMERALD, qtd));
            player.sendMessage("Você sacou " + qtd + " do banco e tem " + (playerID.getMoney() - qtd) + " esmeraldas na conta!");
        }

        player.sendMessage("Você não tem " + qtd + " no banco!");
    }

    public void depositar(Player player, String _qtd) {
        int qtd = Integer.parseInt(_qtd);

        if (player.getInventory().contains(Material.EMERALD, qtd)) {
            player.getInventory().removeItem(new ItemStack(Material.EMERALD, qtd));
            MConomy.db.getPlayerDAO().cashPlayer(player, qtd);
            PlayerData playerData = MConomy.db.getPlayerDAO().findByPlayer(player);
            player.sendMessage("Você depositou " + qtd + " no banco e agora tem " + playerData.getMoney() + " esmeraldas!");
        }

        player.sendMessage("Você não tem " + qtd + " esmeralda(s) no seu inventário!");
    }

    public void cobrar(Player player, String _qtd) {
        int qtd = Integer.parseInt(_qtd);
        player.getInventory().addItem(ItemUtils.chargeBlock(player, qtd));
        player.sendMessage("Cobrança adicionada ao seu inventário!");
    }

}
