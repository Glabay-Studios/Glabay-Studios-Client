package com.client.collection.iterable;

import com.client.collection.node.Node;
import net.runelite.rs.api.RSIterableNodeDequeDescendingIterator;

import java.util.Iterator;

public class IterableNodeDequeDescendingIterator implements Iterator, RSIterableNodeDequeDescendingIterator {

    IterableNodeDeque deque;

    Node field3579;

    Node last = null;

    IterableNodeDequeDescendingIterator(IterableNodeDeque var1) {
        this.setDeque(var1);
    }

    void setDeque(IterableNodeDeque iterableNodeDeque) {
        this.deque = iterableNodeDeque;
        this.start();
    }

    void start() {
        this.field3579 = this.deque != null ? this.deque.sentinel.previous : null;
        this.last = null;
    }

    public boolean hasNext() {
        return this.deque.sentinel != this.field3579 && this.field3579 != null;
    }

    public void remove() {
        if (this.last == null) {
            throw new IllegalStateException();
        } else {
            this.last.remove();
            this.last = null;
        }
    }

    public Object next() {
        Node var1 = this.field3579;
        if (var1 == this.deque.sentinel) {
            var1 = null;
            this.field3579 = null;
        } else {
            this.field3579 = var1.previous;
        }

        this.last = var1;
        return var1;
    }
}
