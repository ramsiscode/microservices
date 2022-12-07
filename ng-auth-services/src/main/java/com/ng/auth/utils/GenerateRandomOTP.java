package com.ng.auth.utils;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GenerateRandomOTP {


    private static Random random = new SecureRandom();

    public static List<String> generateCodes(int amount, char[] chars, int codeLength, int groupNbr) {
        // Must generate at least one code
        if (amount < 1) {
            throw new InvalidParameterException("Amount must be at least 1.");
        }

        // Create an array and fill with generated codes
        String[] codes = new String[amount];
        Arrays.setAll(codes, i -> generateCode(chars, codeLength, groupNbr));
        List<String> backUpCodes =Arrays.asList(codes);
        return backUpCodes;
    }

    private static String generateCode(char[] chars, int codeLength, int groupNbr) {
        final StringBuilder code = new StringBuilder(codeLength + (codeLength / groupNbr) - 1);

        for (int i = 0; i < codeLength; i++) {
            // Append random character from authorized ones
            code.append(chars[random.nextInt(chars.length)]);
        }

        return code.toString();
    }
}
