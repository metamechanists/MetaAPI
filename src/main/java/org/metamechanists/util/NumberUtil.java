package org.metamechanists.util;

import java.util.Random;

public class NumberUtil {

    private static final Random random = new Random();

    public static int getRandomNumber(int min, int max) {
        // min <= x < max
        return random.nextInt(max - min) + min;
    }
}
