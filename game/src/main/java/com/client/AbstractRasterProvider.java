package com.client;

import net.runelite.rs.api.RSAbstractRasterProvider;

public abstract class AbstractRasterProvider implements RSAbstractRasterProvider {
    public int[] pixels;
    public int width;
    public int height;
    protected float[] pixelsFloat;

    public abstract void drawFull(int var1, int var2);
    public abstract void draw(int var1, int var2, int var3, int var4);

    public final void init() {
        Rasterizer2D.init(this.width, this.height,this.pixels,this.pixelsFloat); // L: 11
    }

    public final void hasDepthPixels(boolean var1) {
        this.pixelsFloat = var1 ? new float[this.height * this.width + 1] : null;
    }

}
