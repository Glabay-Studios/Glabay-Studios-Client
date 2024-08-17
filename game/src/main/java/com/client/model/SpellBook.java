package com.client.model;

public enum SpellBook {
    MODERN(12424),
    MODERN2(938),
    ANCIENT(12855),
    LUNAR(29999);

    private final int interfaceId;

    SpellBook(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }
}
