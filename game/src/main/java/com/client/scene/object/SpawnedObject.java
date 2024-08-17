package com.client.scene.object;

import com.client.collection.node.Node;

public final class SpawnedObject extends Node {

    public SpawnedObject()
    {
        getLongetivity = -1;
    }

    int filter_ops = 31;

    public int id;
    public int orientation;
    public int type;
    public int getLongetivity;
    public int plane;
    public int group;
    public int x;
    public int y;
    public int getPreviousId;
    public int previousOrientation;
    public int previousType;
    public int delay;

    public void set_filter_op(int arg0) {
        this.filter_ops = arg0;
    }

    public boolean show_op(int arg0) {
        if (arg0 >= 0 && arg0 <= 4) {
            return (this.filter_ops & 1 << arg0) != 0;
        } else {
            return true;
        }
    }
}
