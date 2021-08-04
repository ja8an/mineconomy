package software.juno.mc.economy.listeners;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.Listener;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;

import java.util.Objects;

@Listener
public class InitializerListener extends BaseListener {

    public InitializerListener(MConomy mConomy) {
        super(mConomy);
    }

    @SneakyThrows
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerData playerData = MConomy.db.getPlayerDAO().findByPlayer(p);
        event.setJoinMessage(playerData.getName() + ChatColor.YELLOW + " chegou na cidade!");
        getLogger().info(p.getName() + ": " + p.getUniqueId());
    }

    @EventHandler
    public void sleep(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        Profession profession = getProfession(p.getName());
        if (Profession.WARRIOR.equals(profession)) {
            Objects.requireNonNull(p.getLocation().getWorld()).setTime(0L);
        }
    }

}
