package com.client.scene.region;

public class BitTools {
    public static int field3209;
    static int[] BIT_RANGES;

    static {
        new Object();
        BIT_RANGES = new int[33];
        BIT_RANGES[0] = 0;
        int var0 = 2;

        for (int var1 = 1; var1 < 33; ++var1) {
            BIT_RANGES[var1] = var0 - 1;
            var0 += var0;
        }

    }

    BitTools() throws Throwable {
        throw new Error();
    }

    public static int get_range(int arg0) {
        return BIT_RANGES[arg0];
    }

    public static int bit_count(int arg0) {
        int var1 = (arg0 >>> 1 & 0x55555555) + (arg0 & 0x55555555);
        int var2 = (var1 >>> 2 & 0x33333333) + (var1 & 0x33333333);
        int var3 = (var2 >>> 4) + var2 & 0xF0F0F0F;
        int var4 = var3 + (var3 >>> 8);
        int var5 = var4 + (var4 >>> 16);
        return var5 & 0xFF;
    }

    public static int clearbit_range(int value, int start, int end) {
        int var4 = get_range(1 + (end - start));
        int var5 = var4 << start;
        return value & ~var5;
    }

    public static int setbit_range(int value, int start, int end) {
        int var4 = get_range(1 + (end - start));
        int var5 = var4 << start;
        return value | var5;
    }

    public static int get_next_pow2(int arg0) {
        int var1 = arg0 - 1;
        int var2 = var1 | var1 >>> 1;
        int var3 = var2 | var2 >>> 2;
        int var4 = var3 | var3 >>> 4;
        int var5 = var4 | var4 >>> 8;
        int var6 = var5 | var5 >>> 16;
        return var6 + 1;
    }

    public static int method5210(int arg0, int arg1) {
        int var3 = arg0 >>> 31;
        return (var3 + arg0) / arg1 - var3;
    }

    public static int intlog2(int arg0) {//
        int n = 0;
        if (arg0 < 0 || arg0 >= 65536) {
            arg0 >>>= 16;
            n += 16;
        }

        if (arg0 >= 256) {
            arg0 >>>= 8;
            n += 8;
        }

        if (arg0 >= 16) {
            arg0 >>>= 4;
            n += 4;
        }

        if (arg0 >= 4) {
            arg0 >>>= 2;
            n += 2;
        }

        if (arg0 >= 1) {
            arg0 >>>= 1;
            ++n;
        }

        return n + arg0;
    }

    public static long nbit_max_unsigned(int bits) {
        if (bits > 63) {
            //throw new FormattedIllegalArgumentException("Cannot generate max unsigned value for more than 63 bits as this is greater than the boundaries of a java long. Value provided: %d", new Object[]{bits});
        } else {
            return (long) Math.pow(2.0D, (double) bits) - 1L;
        }
        return 0;
    }
}
