package com.harryfreeborough.nesemu.utils;

/**
 * Utility functions relating to taking input from the user.
 */
public class InputUtils {

    private InputUtils() {}

    public static int parseInteger(String input) {
        int base = 10;
        if (input.startsWith("$")) {
            base = 16;
            input = input.substring(1);
        }

        return Integer.parseUnsignedInt(input, base);
    }

}
