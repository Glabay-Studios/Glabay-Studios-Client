package com.client.util;

public class MonotonicClock {

    private static long leapMillis;

    private static long previous;

    public static synchronized long currentTimeMillis() {
        long now = System.currentTimeMillis();
        if (now < previous) {
            leapMillis += previous - now;
        }
        previous = now;
        return now + leapMillis;
    }
}
