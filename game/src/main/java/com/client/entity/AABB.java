package com.client.entity;

import net.runelite.rs.api.RSAABB;

public class AABB implements RSAABB {
    public int xMid;
    public int yMid;
    public int zMid;
    public int xMidOffset;
    public int yMidOffset;
    public int zMidOffset;

    public AABB(int xMid, int yMid, int zMid, int xMidOffset, int yMidOffset, int zMidOffset) {
        this.xMid = xMid;
        this.yMid = yMid;
        this.zMid = zMid;
        this.xMidOffset = xMidOffset;
        this.yMidOffset = yMidOffset;
        this.zMidOffset = zMidOffset;
    }

    @Override
    public int getCenterX() {
        return xMid;
    }

    @Override
    public int getCenterY() {
        return yMid;
    }

    @Override
    public int getCenterZ() {
        return zMid;
    }

    @Override
    public int getExtremeX() {
        return xMidOffset;
    }

    @Override
    public int getExtremeY() {
        return yMidOffset;
    }

    @Override
    public int getExtremeZ() {
        return zMidOffset;
    }
}