package com.client.collection.node;

import net.runelite.rs.api.RSIntegerNode;

public class IntegerNode extends Node implements RSIntegerNode {

    public int integer;

    public IntegerNode(int var1) {
        this.integer = var1;
    }

    @Override
    public int getValue() {
        return integer;
    }

    @Override
    public void setValue(int value) {
        integer = value;
    }
}