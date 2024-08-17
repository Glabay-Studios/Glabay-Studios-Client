package com.client.entity.model;

public final class Vertex {

    public int x;
    public int y;
    public int z;
    public int magnitude;

    public Vertex() {

    }

    public Vertex(Vertex var1) {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
        this.magnitude = var1.magnitude;
    }

}
