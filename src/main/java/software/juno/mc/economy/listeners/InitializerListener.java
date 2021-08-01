package software.juno.mc.economy.listeners;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;

import java.util.Objects;
import java.util.logging.Logger;

public class InitializerListener implements Listener {

    private final Logger logger;

    public InitializerListener(Logger logger) {
        this.logger = logger;
    }

    @SneakyThrows
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerData playerData = MConomy.db.getPlayerDAO().findByPlayer(p);
        event.setJoinMessage(playerData.getName() + ChatColor.YELLOW + " chegou na cidade!");
        logger.info(p.getName() + ": " + p.getUniqueId());
    }

    @EventHandler
    public void sleep(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        Profession profession = MConomy.getProfession(p.getName());
        if (Profession.WARRIOR.equals(profession)) {
            Objects.requireNonNull(p.getLocation().getWorld()).setTime(0L);
        }
    }

}
