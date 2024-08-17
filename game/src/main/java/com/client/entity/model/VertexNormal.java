package com.client.entity.model;

import lombok.Data;
import net.runelite.rs.api.RSVertexNormal;

@Data
public class VertexNormal implements RSVertexNormal {
    public int x;
    public int y;
    public int z;
    public int magnitude;

    public VertexNormal() {
    }

    public VertexNormal(VertexNormal var1) {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
        this.magnitude = var1.magnitude;
    }
}