package com.client.collection.iterable;

import com.client.collection.IterableDualNodeQueue;
import com.client.collection.node.DualNode;
import net.runelite.rs.api.RSIterableDualNodeQueueIterator;

import java.util.Iterator;

public class IterableDualNodeQueueIterator implements Iterator, RSIterableDualNodeQueueIterator {

    IterableDualNodeQueue queue;

    DualNode head;

    DualNode last = null;

    public IterableDualNodeQueueIterator(IterableDualNodeQueue queue) {
        this.queue = queue;
        this.head = this.queue.sentinel.previousDual;
        this.last = null;
    }

    public Object next() {
        DualNode dualNode = this.head;
        if (dualNode == this.queue.sentinel) {
            dualNode = null;
            this.head = null;
        } else {
            this.head = dualNode.previousDual;
        }

        this.last = dualNode;
        return dualNode;
    }

    public boolean hasNext() {
        return this.queue.sentinel != this.head;
    }

    public void remove() {
        if (this.last == null) {
            throw new IllegalStateException();
        } else {
            this.last.removeDual();
            this.last = null;
        }
    }
}
