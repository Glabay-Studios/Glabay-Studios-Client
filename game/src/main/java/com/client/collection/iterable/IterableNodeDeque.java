package com.client.collection.iterable;

import com.client.collection.node.Node;
import net.runelite.rs.api.RSIterableNodeDeque;
import net.runelite.rs.api.RSNode;

import java.util.Collection;
import java.util.Iterator;

public class IterableNodeDeque implements Iterable, Collection, RSIterableNodeDeque {

    Node sentinel = new Node();

    Node field3581;

    public IterableNodeDeque() {
        this.sentinel.previous = this.sentinel;
        this.sentinel.next = this.sentinel;
    }

    public void rsClear() {
        while (this.sentinel.previous != this.sentinel) {
            this.sentinel.previous.remove();
        }

    }

    public void addFirst(Node node) {
        if (node.next != null) {
            node.remove();
        }

        node.next = this.sentinel.next;
        node.previous = this.sentinel;
        node.next.previous = node;
        node.previous.next = node;
    }

    public void addLast(Node node) {
        if (node.next != null) {
            node.remove();
        }

        node.next = this.sentinel;
        node.previous = this.sentinel.previous;
        node.next.previous = node;
        node.previous.next = node;
    }

    public Node last() {
        return this.method1979((Node) null);
    }

    Node method1979(Node node) {
        Node var2;
        if (node == null) {
            var2 = this.sentinel.previous;
        } else {
            var2 = node;
        }

        if (var2 == this.sentinel) {
            this.field3581 = null;
            return null;
        } else {
            this.field3581 = var2.previous;
            return var2;
        }
    }

    public Node previous() {
        Node var1 = this.field3581;
        if (var1 == this.sentinel) {
            this.field3581 = null;
            return null;
        } else {
            this.field3581 = var1.previous;
            return var1;
        }
    }

    int method1977() {
        int var1 = 0;

        for (Node var2 = this.sentinel.previous; var2 != this.sentinel; var2 = var2.previous) {
            ++var1;
        }

        return var1;
    }

    public boolean hasActiveHealthBars() {
        return this.sentinel.previous == this.sentinel;
    }

    Node[] method1978() {
        Node[] var1 = new Node[this.method1977()];
        int var2 = 0;

        for (Node var3 = this.sentinel.previous; var3 != this.sentinel; var3 = var3.previous) {
            var1[var2++] = var3;
        }

        return var1;
    }

    boolean method1972(Node node) {
        this.addFirst(node);
        return true;
    }

    public boolean removeAll(Collection collection) {
        throw new RuntimeException();
    }

    public int size() {
        return this.method1977();
    }

    public boolean contains(Object var1) {
        throw new RuntimeException();
    }

    public Object[] toArray() {
        return this.method1978();
    }

    public Object[] toArray(Object[] objects) {
        int var2 = 0;

        for (Node var3 = this.sentinel.previous; var3 != this.sentinel; var3 = var3.previous) {
            objects[var2++] = var3;
        }

        return objects;
    }

    public static void IterableNodeDeque_addBefore(Node var0, Node var1) {
        if (var0.next != null) {
            var0.remove();
        }

        var0.next = var1;
        var0.previous = var1.previous;
        var0.next.previous = var0;
        var0.previous.next = var0;
    }

    public boolean remove(Object var1) {
        throw new RuntimeException();
    }

    public boolean retainAll(Collection var1) {
        throw new RuntimeException();
    }

    public void clear() {
        this.rsClear();
    }

    public boolean add(Object var1) {
        return this.method1972((Node) var1);
    }

    public boolean equals(Object var1) {
        return super.equals(var1);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean addAll(Collection var1) {
        throw new RuntimeException();
    }

    public boolean containsAll(Collection var1) {
        throw new RuntimeException();
    }

    public Iterator iterator() {
        return new IterableNodeDequeDescendingIterator(this);
    }

    public boolean isEmpty() {
        return this.hasActiveHealthBars();
    }

    @Override
    public RSNode getCurrent() {
        return sentinel;
    }
}
