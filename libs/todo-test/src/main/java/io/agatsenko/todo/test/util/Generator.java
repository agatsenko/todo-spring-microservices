/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-24
 */
package io.agatsenko.todo.test.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
    private static final char BEGIN_VALID_ASCII_CHAR = 32;
    private static final char END_VALID_ASCII_CHAR = 127;

    private static final int BEGIN_NEW_STR_LENGTH = 1;
    private static final int END_NEW_STR_LENGTH = 20;

    public static ThreadLocalRandom rand() {
        return ThreadLocalRandom.current();
    }

    public static UUID newUuid() {
        return UUID.randomUUID();
    }

    public static int newInt() {
        return rand().nextInt();
    }

    public static int newInt(int bound) {
        return rand().nextInt(bound);
    }

    public static int newInt(int origin, int bound) {
        return rand().nextInt(origin, bound);
    }

    public static String newUuidStr() {
        return newUuid().toString().replace("-", "");
    }

    public static String newStr(int length, char beginChar, char endChar) {
        if (length < 0) {
            throw new IllegalArgumentException("length less than 1");
        }
        final var strBuilder = new StringBuilder();
        for (var i = 0; i < length; ++i) {
            strBuilder.append((char) newInt(beginChar, endChar + 1));
        }
        return strBuilder.toString();
    }

    public static String newStr(int beginLength, int endLength, char beginChar, char endChar) {
        if (beginLength < 1) {
            throw new IllegalArgumentException("beginLength less than 1");
        }
        if (endLength < 1) {
            throw new IllegalArgumentException("endLength less than 1");
        }
        if (endLength >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("endLength equal to max int");
        }
        return newStr(newInt(beginLength, endLength + 1), beginChar, endChar);
    }

    public static String newStr(int beginLength, int endLength) {
        return newStr(beginLength, endLength, BEGIN_VALID_ASCII_CHAR, END_VALID_ASCII_CHAR);
    }

    public static String newStr(int length) {
        return newStr(length, BEGIN_VALID_ASCII_CHAR, END_VALID_ASCII_CHAR);
    }

    public static String newStr() {
        return newStr(BEGIN_NEW_STR_LENGTH, END_NEW_STR_LENGTH);
    }

    public static String newEmail() {
        return newUuidStr() + "@homeaccounting.com";
    }
}
