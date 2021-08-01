package software.juno.mc.economy.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.models.enums.Profession;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProfessionListener implements Listener {

    private final Logger logger;

    public ProfessionListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void mobKill(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            Profession profession = MConomy.getProfession(killer.getName());
            if (!profession.canKill(event.getEntityType())) {
                event.setDroppedExp(0);
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {


        logger.info("Player is trying to craft " + event.getRecipe().getResult().getType());

        for (HumanEntity entity : event.getViewers()) {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                Profession profession = MConomy.getProfession(player.getName());

                ItemStack[] item = event.getInventory().getMatrix();

                for (ItemStack itemStack : item) {
                    if (itemStack != null && !profession.canCraft(itemStack.getType())) {
                        event.setCancelled(true);
                        player.sendMessage("Você não pode craftar esse item!");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        logger.info("Trying to break " + event.getBlock().getType());
        Profession profession = MConomy.getProfession(player.getName());
        boolean canBreak = profession.canBreak(event.getBlock().getType());
        if (!canBreak) {
            logger.info("Cannot break " + event.getBlock().getType());
            event.setCancelled(true);
        }
    }

}
