package com.client.collection;

import com.client.Client;
import com.client.collection.node.DualNode;
import com.client.collection.table.IterableNodeHashTable;
import net.runelite.rs.api.RSDualNode;
import net.runelite.rs.api.RSEvictingDualNodeHashTable;
import net.runelite.rs.api.RSIterableDualNodeQueue;
import net.runelite.rs.api.RSIterableNodeHashTable;

/**
 * A least-recently used cache of references, backed by a {@link HashTable} and a {@link Queue}.
 */
public final class EvictingDualNodeHashTable implements RSEvictingDualNodeHashTable {

    public float threshold;
    public int tmpCapacity;

    DualNode dualNode = new DualNode();

    int capacity;

    int remainingCapacity;

    IterableNodeHashTable hashTable;

    Queue deque = new Queue();

    public EvictingDualNodeHashTable(int var1) {
        this.capacity = var1;
        this.remainingCapacity = var1;

        int var2;
        for (var2 = 1; var2 + var2 < var1; var2 += var2) {
            ;
        }

        this.hashTable = new IterableNodeHashTable(var2);
        setTmpCapacity(this.getCapacity());
    }

    public DualNode get(long var1) {
        DualNode var3 = (DualNode) this.hashTable.get(var1);
        if (var3 != null) {
            this.deque.add(var3);
        }

        return var3;
    }

    @Override
    public void put(RSDualNode var1, long var2) {
        put((DualNode)var1, var2);
    }

    @Override
    public RSDualNode getDualNode() {
        return dualNode;
    }

    @Override
    public void reset() {
        clear();
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getRemainingCapacity() {
        return remainingCapacity;
    }

    @Override
    public void setRemainingCapacity(int remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    @Override
    public RSIterableDualNodeQueue getDeque() {
        return deque;
    }

    @Override
    public void setHashTable(RSIterableNodeHashTable hashTable) {
        this.hashTable = (IterableNodeHashTable) hashTable;
    }

    public void remove(long var1) {
        DualNode var3 = (DualNode) this.hashTable.get(var1);
        if (var3 != null) {
            var3.remove();
            var3.removeDual();
            ++this.remainingCapacity;
        }

    }

    public void put(DualNode var1, long var2) {
        if (this.remainingCapacity == 0) {
            DualNode var4 = this.deque.removeLast();
            var4.remove();
            var4.removeDual();
            if (var4 == this.dualNode) {
                var4 = this.deque.removeLast();
                var4.remove();
                var4.removeDual();
            }
        } else {
            --this.remainingCapacity;
        }

        this.hashTable.put(var1, var2);
        this.deque.add(var1);
    }

    public void clear() {
        this.deque.clear();
        this.hashTable.clear();
        this.dualNode = new DualNode();
        this.remainingCapacity = this.capacity;
    }


    public float getThreshold()
    {
        return threshold;
    }

    public void setThreshold(float threshold)
    {
        this.threshold = threshold;
    }

    public int getTmpCapacity()
    {
        return tmpCapacity;
    }

    public void setTmpCapacity(int tmpCapacity)
    {
        this.tmpCapacity = tmpCapacity;
    }

    public boolean isTrashing()
    {
        return this.getRemainingCapacity() <= 0 && this.getDualNode().previousDual() == null;
    }

    public void resize(int newSize)
    {
        this.increaseCapacity(newSize);
        this.tmpCapacity = this.getCapacity();
    }


    public void increaseCapacity(int newSize)
    {
        if (newSize > this.getCapacity())
        {
            int i;

            for (i = 1; i < newSize; i += i)
            {
                // ignore
            }

            this.setCapacity(i);
            this.reset();

            RSIterableNodeHashTable iterableNodeHashTable = Client.instance.createIterableNodeHashTable(i);
            this.setHashTable(iterableNodeHashTable);
        }
    }

}
