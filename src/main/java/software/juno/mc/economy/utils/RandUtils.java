package software.juno.mc.economy.utils;

import java.util.Random;

public class RandUtils {

    public static int randomNumbers(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static boolean rollDice(int chances, int range) {
        int result = randomNumbers(1, range);
        return result <= chances;
    }
}
