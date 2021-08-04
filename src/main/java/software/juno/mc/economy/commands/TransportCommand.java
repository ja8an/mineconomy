package software.juno.mc.economy.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.CommandExecutor;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.ItemUtils;

@CommandExecutor("t")
public class TransportCommand extends BaseCommand {

    public TransportCommand(MConomy mConomy) {
        super(mConomy);
    }

    public void index(Player player, String playerName) {

        PlayerData playerID = MConomy.db.getPlayerDAO().findByName(player.getName());

        Player outroPlayer = getServer().getPlayer(playerName);

        if (outroPlayer == null) {
            player.sendMessage("Player de destino não encontrado!");
            return;
        }

        if (player.equals(outroPlayer)) {
            player.sendMessage("Você não pode se teleportar a si mesmo!");
            return;
        }

        if (Profession.WIZARD.equals(playerID.getProfession())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 600, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 600, 0));
            player.teleport(outroPlayer);
        } else if (player.getInventory().contains(ItemUtils.transportTicket())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 600, 0));
            player.getInventory().removeItem(ItemUtils.transportTicket());
            player.teleport(outroPlayer);
        } else {
            player.sendMessage("Você não tem nenhum feitiço de transporte!");
        }

    }

}
