package com.client.model;

public class GameItem {

    private final int id;
    private final int amount;

    public GameItem(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public GameItem(int id) {
        this(id, 1);
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "GameItem{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
