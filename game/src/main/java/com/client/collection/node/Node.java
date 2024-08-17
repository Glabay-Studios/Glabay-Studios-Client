package com.client.collection.node;

import net.runelite.rs.api.RSNode;

public class Node implements RSNode {

    public long key;

    public Node previous;

    public Node next;

    public void remove() {
        if (this.next != null) {
            this.next.previous = this.previous;
            this.previous.next = this.next;
            this.previous = null;
            this.next = null;
            onUnlink();
        }
    }

    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public RSNode getNext() {
        return next;
    }

    @Override
    public RSNode getPrevious() {
        return previous;
    }

    @Override
    public long getHash() {
        return key;
    }

    @Override
    public void unlink() {
        remove();
        onUnlink();
    }

    @Override
    public void onUnlink() {

    }
}
