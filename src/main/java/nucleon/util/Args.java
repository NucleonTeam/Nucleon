package nucleon.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Args {

    public static <T> T notNull(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " is null");
        }
        return argument;
    }

    public static int notNegative(int argument, String name) {
        if (argument < 0) {
            throw new IllegalArgumentException(name + " is negative");
        }
        return argument;
    }

    public static short notNegative(short argument, String name) {
        if (argument < 0) {
            throw new IllegalArgumentException(name + " is negative");
        }
        return argument;
    }

    public static byte notNegative(byte argument, String name) {
        if (argument < 0) {
            throw new IllegalArgumentException(name + " is negative");
        }
        return argument;
    }

    public static long notNegative(long argument, String name) {
        if (argument < 0) {
            throw new IllegalArgumentException(name + " is negative");
        }
        return argument;
    }

    public static double notNegative(double argument, String name) {
        if (argument < 0) {
            throw new IllegalArgumentException(name + " is negative");
        }
        return argument;
    }

    public static float notNegative(float argument, String name) {
        if (argument < 0) {
            throw new IllegalArgumentException(name + " is negative");
        }
        return argument;
    }

}
