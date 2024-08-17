package com.client.collection;

import com.client.collection.node.DualNode;

public abstract class Wrapper extends DualNode {

    final int size;

    Wrapper(int var1) {
        this.size = var1;
    }


    abstract Object get();


    abstract boolean isSoft();
}
