package software.juno.mc.economy.listeners;

import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.Listener;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.ItemUtils;
import software.juno.mc.economy.utils.RandUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Listener
public class ProfessionListener extends BaseListener {

    public ProfessionListener(MConomy mConomy) {
        super(mConomy);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {


        Player killer = event.getEntity().getKiller();

        if (isGod(killer)) return;

        if (killer != null) {

            getLogger().info(killer + " killed a " + event.getEntityType());
            getLogger().info("Entity category " + event.getEntity().getCategory());

            if (event.getEntity() instanceof Monster) {
                Profession profession = MConomy.getProfession(killer.getName());

                if (!Profession.WARRIOR.equals(profession))
                    event.setDroppedExp(0);

                if (Profession.WIZARD.equals(profession)) {
                    event.getDrops().clear();
                    if (EntityType.ENDERMAN.equals(event.getEntityType())) {
                        event.getDrops().add(ItemUtils.transportTicket(25));
                    } else if (RandUtils.rollDice(1, 5)) {
                        event.getDrops().add(ItemUtils.transportTicket(1));
                    }
                } else if (!profession.canKill(event.getEntityType())) {
                    event.getDrops().clear();
                }
            }

        }

    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {

        getLogger().info("Player is trying to craft " + event.getRecipe().getResult().getType());

        for (HumanEntity entity : event.getViewers()) {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                if (isGod(player)) continue;

                Profession profession = MConomy.getProfession(player.getName());

                if (!profession.canCraft(event.getRecipe().getResult().getType())) {
                    event.setCancelled(true);
                    player.sendMessage("Você não pode craftar esse item!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = player.getItemInUse();

        if (isGod(player)) return;

        getLogger().info("Trying to break " + event.getBlock().getType());

        if (itemStack != null)
            getLogger().info("Breaking with " + itemStack.getType());

        Profession profession = MConomy.getProfession(player.getName());
        boolean canBreak = profession.canBreak(event.getBlock().getType());
        if (!canBreak) {
            getLogger().info("Cannot break " + event.getBlock().getType());
            event.setCancelled(true);
        }
    }

}
