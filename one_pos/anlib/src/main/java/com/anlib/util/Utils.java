package com.anlib.util;

import java.util.Random;

public class Utils {

    private static Random r = new Random();

    public static int random(int min, int max) {
        return r.nextInt(max)%(max-min+1) + min;
    }
}
