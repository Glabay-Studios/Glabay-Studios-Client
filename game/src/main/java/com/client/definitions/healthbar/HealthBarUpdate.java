package com.client.definitions.healthbar;

import com.client.collection.node.Node;

public class HealthBarUpdate extends Node {

    public int cycle;

    public int health;

    public int health2;

    public int cycleOffset;

    HealthBarUpdate(int var1, int var2, int var3, int var4) {
        this.cycle = var1;
        this.health = var2;
        this.health2 = var3;
        this.cycleOffset = var4;
    }

    void set(int var1, int var2, int var3, int var4) {
        this.cycle = var1;
        this.health = var2;
        this.health2 = var3;
        this.cycleOffset = var4;
    }

}
