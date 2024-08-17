package com.client.util;

import net.runelite.rs.api.RSVerticalAlignment;

public enum VerticalAlignment implements EnumExtension, RSVerticalAlignment {
    field2073(0, 0),
    VerticalAlignment_centered(2, 1),
    field2072(1, 2);

    public final int value;

    final int id;

    VerticalAlignment(int var3, int var4) {
        this.value = var3;
        this.id = var4;
    }

    public int rsOrdinal() {
        return this.id;
    }

}