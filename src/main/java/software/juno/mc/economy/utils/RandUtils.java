package software.juno.mc.economy.utils;

import java.util.Random;
import java.util.logging.Logger;

public class RandUtils {

    public static boolean rollDice(int chances, int range) {
        int result = new Random().nextInt(range - 1) + 1;
        return result <= chances;
    }
}
