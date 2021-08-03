package software.juno.mc.economy.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.ItemUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class TransportCommand extends BaseCommand {

    public TransportCommand(MConomy mConomy) {
        super(mConomy);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;
        PlayerData playerID = MConomy.db.getPlayerDAO().findByName(player.getName());

        if ("t".equalsIgnoreCase(command.getName())) {

            if (args.length == 0) {
                return false;
            }

            String playerName = args[0];
            Player outroPlayer = getServer().getPlayer(playerName);

            if (player.equals(outroPlayer)) {
                player.sendMessage("Você não precisa se teleportar a si mesmo!");
                return false;
            }

            if (outroPlayer != null) {

                if (Profession.WIZARD.equals(playerID.getProfession())) {
                    player.teleport(outroPlayer);
                } else if (player.getInventory().contains(Material.PAPER)) {
                    Optional<ItemStack> stack = Arrays.stream(player.getInventory().getContents()).filter(itemStack -> ItemUtils.TRANSPORT_TICKET.equalsIgnoreCase(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName())).findFirst();
                    if (stack.isPresent()) {
                        player.getInventory().remove(stack.get());
                        player.teleport(outroPlayer);
                    } else {
                        player.sendMessage("Você não tem nenhum ticket de transporte!");
                        return false;
                    }
                }

            } else {
                player.sendMessage("Player de destino não encontrado!");
                return false;
            }

            return true;

        }

        return false;

    }
}
