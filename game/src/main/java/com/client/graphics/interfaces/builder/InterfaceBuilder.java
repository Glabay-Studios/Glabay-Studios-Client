package com.client.graphics.interfaces.builder;

import com.client.graphics.interfaces.RSInterface;

public abstract class InterfaceBuilder extends RSInterface {

    private final int baseInterfaceId;
    private final RSInterface root;
    private int nextChildId;
    private int nextInterfaceId;

    public abstract void build();

    public InterfaceBuilder(int baseInterfaceId) {
        this.baseInterfaceId = baseInterfaceId;
        root = RSInterface.addInterface(baseInterfaceId);
        root.totalChildren(0);
        init();
    }

    public void init() {
        nextChildId = 0;
        nextInterfaceId = baseInterfaceId + 1;
    }

    public void child(int x, int y) {
        root.child(nextChild(), nextInterfaceId - 1, x, y);
    }

    public void child(int interfaceId, int x, int y) {
        root.child(nextChild(), interfaceId, x, y);
    }

    public int getBaseInterfaceId() {
        return baseInterfaceId;
    }

    public int nextChild() {
        RSInterface.expandChildren(1, root);
        return nextChildId++;
    }

    public int lastInterface() {
        return nextInterfaceId - 1;
    }

    public int nextInterface() {
        return nextInterfaceId++;
    }

    public int getCurrentInterfaceId() {
        return nextInterfaceId;
    }

    public void setNextInterfaceId(int nextInterfaceId) {
        this.nextInterfaceId = nextInterfaceId;
    }

    public RSInterface getLastChild() {
        return RSInterface.get(nextInterfaceId - 1);
    }

    public RSInterface getRoot() {
        return root;
    }
}
