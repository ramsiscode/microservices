package com.ng.auth.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateBackUpCodes {

    public static List<Integer> generateBackUpCodes() {
        List<Integer> secretCodes = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            Random random = new Random();
            secretCodes.add(Integer.parseInt(String.format("%06d", random.nextInt(999999))));
        }
        return secretCodes;
    }
}
