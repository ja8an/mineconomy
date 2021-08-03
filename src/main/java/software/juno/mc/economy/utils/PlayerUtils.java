package software.juno.mc.economy.utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerUtils {

    private PlayerUtils() {
    }

    public static boolean isGod(Player player) {
        return GameMode.CREATIVE.equals(player.getGameMode());
    }

}
