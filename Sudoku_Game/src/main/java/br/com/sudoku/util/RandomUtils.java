package br.com.sudoku.util;

import java.util.Random;

public class RandomUtils {
    private static final Random RANDOM = new Random();

    private RandomUtils() {} // evita instanciação

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }
}
