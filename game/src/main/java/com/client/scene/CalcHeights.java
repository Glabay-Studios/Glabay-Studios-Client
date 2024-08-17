package com.client.scene;

import com.client.draw.Rasterizer3D;

public class CalcHeights {
    
    public static final int perlinNoise(int var0, int var1, int var2) {
        return method634(var0, var1, var2);
    }

    static final int method952(int var0, int var1, int var2, int var3) {
        int var4 = 65536 - Rasterizer3D.COSINE[var2 * 1024 / var3] >> 1;
        return ((65536 - var4) * var0 >> 16) + (var4 * var1 >> 16);
    }

    static final int method634(int var0, int var1, int var2) {
        int var3 = var0 / var2;
        int var4 = var0 & var2 - 1;
        int var5 = var1 / var2;
        int var6 = var1 & var2 - 1;
        int var7 = method2085(var3, var5);
        int var8 = method2085(var3 + 1, var5);
        int var9 = method2085(var3, var5 + 1);
        int var10 = method2085(var3 + 1, var5 + 1);
        int var11 = method952(var7, var8, var4, var2);
        int var12 = method952(var9, var10, var4, var2);
        return method952(var11, var12, var6, var2);
    }

    static final int method2085(int var0, int var1) {
        int var2 = method537(var0 - 1, var1 - 1) + method537(var0 + 1, var1 - 1) + method537(var0 - 1, var1 + 1) + method537(var0 + 1, var1 + 1);
        int var3 = method537(var0 - 1, var1) + method537(var0 + 1, var1) + method537(var0, var1 - 1) + method537(var0, var1 + 1);
        int var4 = method537(var0, var1);
        return var2 / 16 + var3 / 8 + var4 / 4;
    }

    static final int method537(int var0, int var1) {
        int var2 = var1 * 57 + var0;
        var2 ^= var2 << 13;
        int var3 = (var2 * var2 * 15731 + 789221) * var2 + 1376312589 & Integer.MAX_VALUE;
        return var3 >> 19 & 255;
    }
    
}
