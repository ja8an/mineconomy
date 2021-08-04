package software.juno.mc.economy.commands;

import org.bukkit.entity.Player;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.CommandExecutor;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.PlayerUtils;

import java.util.Locale;

@CommandExecutor("jobs")
public class JobsCommand extends BaseCommand {

    public JobsCommand(MConomy mConomy) {
        super(mConomy);
    }

    public void index(Player player, String profession) {
        index(player, player.getName(), profession);
    }

    public void index(Player player, String destinationPlayer, String profession) {

        if (!player.getName().equals(destinationPlayer) && !isGod(player)) {
            return;
        }

        Profession prof;
        try {
            prof = Profession.valueOf(profession.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            player.sendMessage("Profissão não encontrada!");
            return;
        }

        Player destino = getServer().getPlayer(destinationPlayer);

        if (destino == null) {
            player.sendMessage("O player " + destinationPlayer + " não foi encontrado!");
            return;
        }

        PlayerData playerID = getPlayerData(destino);
        Profession profAtual = playerID.getProfession();

        if (Profession.UNEMPLOYED.equals(profAtual)) {
            playerID.setProfession(prof);
            PlayerUtils.addStartItems(destino, prof);
            db().getPlayerDAO().update(playerID);
        } else {
            if (PlayerUtils.isGod(player) || db().getPlayerDAO().chargePlayer(destino, 10000)) {
                playerID.setProfession(prof);
                PlayerUtils.addStartItems(destino, prof);
                db().getPlayerDAO().update(playerID);
            } else {
                player.sendMessage("Você não tem esmeraldas suficientes no banco para realizar a troca de profissão!");
            }
        }
    }
}
