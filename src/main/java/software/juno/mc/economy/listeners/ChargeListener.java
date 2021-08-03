package software.juno.mc.economy.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.juno.mc.economy.MConomy;

import java.util.List;
import java.util.logging.Logger;

public class ChargeListener extends BaseListener {

    public ChargeListener(MConomy mConomy) {
        super(mConomy);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Action action = e.getAction();
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        Block clicked = e.getClickedBlock();

        getLogger().info("Action " + action);
        getLogger().info("Using item " + item);
        if (clicked != null)
            getLogger().info("Into item " + clicked.getType());

        if (Action.RIGHT_CLICK_AIR.equals(action) && item != null && Material.PAPER.equals(item.getType())) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {

                if ("Cobrança".equalsIgnoreCase(meta.getDisplayName())) {

                    List<String> lore = meta.getLore();

                    if (lore == null || lore.size() < 2)
                        return;

                    Player outro = getServer().getPlayer(lore.get(0));
                    int qtd = Integer.parseInt(lore.get(1));

                    if (outro == null) {
                        player.sendMessage("O jogador não está online agora!");
                        return;
                    }

                    if (isGod(player) || MConomy.db.getPlayerDAO().chargePlayer(player, qtd)) {
                        MConomy.db.getPlayerDAO().cashPlayer(outro, qtd);
                        player.sendMessage("Você pagou " + qtd + " esmeraldas para " + outro.getName());
                        outro.sendMessage(player.getName() + " te pagou " + qtd + " esmeraldas");
                        player.getInventory().remove(item);
                        e.setCancelled(true);
                    } else {
                        player.sendMessage("Você não tem esmeraldas suficientes para fazer o pagamento!");
                    }


                }

            }
        } else if (Action.RIGHT_CLICK_BLOCK.equals(action) && item != null) {

        }
    }

}
