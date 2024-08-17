package com.client.util;

import java.util.Arrays;

public class EnumUtils {

    public static EnumExtension findEnumerated(EnumExtension[] var0, int var1) {
        EnumExtension[] var2 = var0;

        for(int var3 = 0; var3 < var2.length; ++var3) {
            EnumExtension var4 = var2[var3];
            if (var1 == var4.rsOrdinal()) {
                return var4;
            }
        }

        return null;
    }

    public static <E extends EnumExtension> E findEnumeratedd(E[] values, int serialId) {
        return Arrays.stream(values).filter(e -> serialId == e.rsOrdinal()).findFirst().orElse(null);
    }

}
