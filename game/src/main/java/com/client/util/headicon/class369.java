package com.client.util.headicon;

import com.client.Bounds;

import java.util.HashMap;

public class class369 {

    static String otp;

    final HashMap spriteMap;


    Bounds bounds;

    int[] field4361;

    int[] field4362;

    int field4365;

    public class369() {
        this.spriteMap = new HashMap();
        this.bounds = new Bounds(0, 0);
        this.field4361 = new int[2048];
        this.field4362 = new int[2048];
        this.field4365 = 0;
        method6647();
    }

    static int[] field2285;

    static void method6647() {
        field2285 = new int[2000];
        int var0 = 0;
        int var1 = 240;

        int var3;
        for (byte var2 = 12; var0 < 16; var1 -= var2) {
            var3 = method2248((double)((float)var1 / 360.0F), 0.9998999834060669D, (double)((float)var0 * 0.425F / 16.0F + 0.075F));
            field2285[var0] = var3;
            ++var0;
        }

        var1 = 48;

        for (int var5 = var1 / 6; var0 < field2285.length; var1 -= var5) {
            var3 = var0 * 2;

            for (int var4 = method2248((double)((float)var1 / 360.0F), 0.9998999834060669D, 0.5D); var0 < var3 && var0 < field2285.length; ++var0) {
                field2285[var0] = var4;
            }
        }

    }

    public static final int method2248(double var0, double var2, double var4) {
        double var6 = var4;
        double var8 = var4;
        double var10 = var4;
        if (var2 != 0.0D) {
            double var12;
            if (var4 < 0.5D) {
                var12 = var4 * (var2 + 1.0D);
            } else {
                var12 = var4 + var2 - var4 * var2;
            }

            double var14 = var4 * 2.0D - var12;
            double var16 = var0 + 0.3333333333333333D;
            if (var16 > 1.0D) {
                --var16;
            }

            double var20 = var0 - 0.3333333333333333D;
            if (var20 < 0.0D) {
                ++var20;
            }

            if (var16 * 6.0D < 1.0D) {
                var6 = var16 * (var12 - var14) * 6.0D + var14;
            } else if (var16 * 2.0D < 1.0D) {
                var6 = var12;
            } else if (var16 * 3.0D < 2.0D) {
                var6 = 6.0D * (0.6666666666666666D - var16) * (var12 - var14) + var14;
            } else {
                var6 = var14;
            }

            if (6.0D * var0 < 1.0D) {
                var8 = var0 * 6.0D * (var12 - var14) + var14;
            } else if (2.0D * var0 < 1.0D) {
                var8 = var12;
            } else if (3.0D * var0 < 2.0D) {
                var8 = (var12 - var14) * (0.6666666666666666D - var0) * 6.0D + var14;
            } else {
                var8 = var14;
            }

            if (var20 * 6.0D < 1.0D) {
                var10 = var20 * 6.0D * (var12 - var14) + var14;
            } else if (2.0D * var20 < 1.0D) {
                var10 = var12;
            } else if (var20 * 3.0D < 2.0D) {
                var10 = (var12 - var14) * (0.6666666666666666D - var20) * 6.0D + var14;
            } else {
                var10 = var14;
            }
        }

        int var22 = (int)(var6 * 256.0D);
        int var13 = (int)(var8 * 256.0D);
        int var23 = (int)(var10 * 256.0D);
        int var15 = var23 + (var13 << 8) + (var22 << 16);
        return var15;
    }




    public final void method6898(int var1, int var2) {
        if (this.field4365 < this.field4361.length) {
            this.field4361[this.field4365] = var1;
            this.field4362[this.field4365] = var2;
            ++this.field4365;
        }
    }


    public final void method6899() {
        this.field4365 = 0;
    }




}

