package software.juno.mc.economy;

import org.bukkit.entity.Player;
import software.juno.mc.economy.utils.PlayerUtils;

public abstract class BaseApp {

    public boolean isGod(Player player) {
        return player != null && PlayerUtils.isGod(player);
    }

}
