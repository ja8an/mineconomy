package software.juno.mc.economy.utils;

import java.util.Random;
import java.util.logging.Logger;

public class RandUtils {

    public static boolean rollDice(int chances, int range, Logger logger) {
        logger.info("Dice: " + chances + " in " + range);
        int result = new Random().nextInt(range - 1) + 1;
        logger.info("Result " + result);
        boolean ok = result <= chances;
        logger.info("Lucky ? " + (ok ? "Yes" : "No"));
        return ok;
    }
}
