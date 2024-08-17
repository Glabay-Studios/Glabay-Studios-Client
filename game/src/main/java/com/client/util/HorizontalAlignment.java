package com.client.util;

import net.runelite.rs.api.RSHorizontalAlignment;

public enum HorizontalAlignment implements EnumExtension, RSHorizontalAlignment {
    field2008(2, 0),
    HorizontalAlignment_centered(1, 1),
    field2010(0, 2);

    public final int value;

    final int id;

    HorizontalAlignment(int var3, int var4) {
        this.value = var3;
        this.id = var4;
    }


    public int rsOrdinal() {
        return this.id;
    }

}
