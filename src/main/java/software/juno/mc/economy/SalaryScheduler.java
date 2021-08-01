package software.juno.mc.economy;

import org.bukkit.entity.Player;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.ItemUtils;

public class SalaryScheduler implements Runnable {

    final MConomy mConomy;

    public SalaryScheduler(MConomy mConomy) {
        this.mConomy = mConomy;
    }

    @Override
    public void run() {
        for (Player onlinePlayer : mConomy.getServer().getOnlinePlayers()) {
            int amount = 50 * (1 + (onlinePlayer.getLevel() / 10));
            MConomy.db.getPlayerDAO().cashPlayer(onlinePlayer, amount);
            if (Profession.WIZARD.equals(MConomy.getProfession(onlinePlayer.getName()))) {
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
