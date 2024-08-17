package com.client;

public class Cacheable extends Linkable {

    public final void unlinkSub() {
        if (nextNodeSub == null) {
        } else {
            nextNodeSub.prevNodeSub = prevNodeSub;
            prevNodeSub.nextNodeSub = nextNodeSub;
            prevNodeSub = null;
            nextNodeSub = null;
        }
    }

    public Cacheable() {
    }

    public Cacheable prevNodeSub;
    Cacheable nextNodeSub;
}
