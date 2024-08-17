package com.client.util.headicon;

import com.client.definitions.NpcDefinition;

public class class515 {

    int[] field5113;

    short[] field5114;


    public class515(NpcDefinition var1) {
        this.field5113 = new int[8];
        this.field5114 = new short[8];
        int var2 = 0;
        if (var1.headIconArchiveAvailable()) {
            var2 = var1.getHeadIconArchiveIds().length;
            System.arraycopy(var1.getHeadIconArchiveIds(), 0, this.field5113, 0, var2);
            System.arraycopy(var1.headIconIndex(), 0, this.field5114, 0, var2);
        }

        for (int var3 = var2; var3 < 8; ++var3) {
            this.field5113[var3] = -1;
            this.field5114[var3] = -1;
        }

    }


    public int[] method9299() {
        return this.field5113;
    }


    public short[] method9300() {
        return this.field5114;
    }


    public void method9301(int var1, int var2, short var3) {
        this.field5113[var1] = var2;
        this.field5114[var1] = var3;
    }


    public void method9302(int[] var1, short[] var2) {
        this.field5113 = var1;
        this.field5114 = var2;
    }
}