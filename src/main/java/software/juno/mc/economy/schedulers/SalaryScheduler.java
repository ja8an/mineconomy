package software.juno.mc.economy.schedulers;

import org.bukkit.entity.Player;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.Scheduled;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.ItemUtils;

@Scheduled(6000)
public class SalaryScheduler extends BaseScheduler {

    public SalaryScheduler(MConomy mConomy) {
        super(mConomy);
    }

    @Override
    public void run() {
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            int amount = 50 * (1 + (onlinePlayer.getLevel() / 10));
            MConomy.db.getPlayerDAO().cashPlayer(onlinePlayer, amount);
            if (Profession.WIZARD.equals(getProfession(onlinePlayer.getName()))) {
                int qtd = 10 * (1 + (onlinePlayer.getLevel() / 10));
                for (int i = 0; i < qtd; i++) {
                    onlinePlayer.getInventory().addItem(ItemUtils.transportTicket());
                }
                onlinePlayer.sendMessage("Você recebeu " + qtd + " tickets de transporte");
            }
            onlinePlayer.sendMessage("Você recebeu " + amount + " de salário!");
        }
    }
}
