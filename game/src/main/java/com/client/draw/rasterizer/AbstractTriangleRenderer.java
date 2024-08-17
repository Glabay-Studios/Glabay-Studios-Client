package com.client.draw.rasterizer;

import com.client.Rasterizer2D;
import com.client.draw.Rasterizer3D;

public abstract class AbstractTriangleRenderer extends Rasterizer2D {
    public boolean isTransparent = false;

    public boolean isLowDetail = false;

    public int[] hslToRgb;

    public Clips clips;

    public AbstractTriangleRenderer(Clips clips) {
        this.hslToRgb = Rasterizer3D.hslToRgb;
        this.clips = clips;
    }


    public void method5286(int[] var1, int var2, int var3, float[] var4) {
        Rasterizer2D.method9607(var1, var2, var3, var4);
    }

    public void drawShadedTriangleColorOverride(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, int var10, int var11, int var12, byte var13, byte var14, byte var15, byte var16) {
        var10 = adjustColor(var10, var13, var14, var15, var16);
        var11 = adjustColor(var11, var13, var14, var15, var16);
        var12 = adjustColor(var12, var13, var14, var15, var16);
        this.vmethod1374(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
    }

    public void drawFlatTriangleColorOverride(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, int var10, byte var11, byte var12, byte var13, byte var14) {
        int var15 = adjustColor(var10, var11, var12, var13, var14);
        var10 = this.hslToRgb[var15];
        this.vmethod1366(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
    }

    public abstract void vmethod1374(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, int var10, int var11, int var12);

    public abstract void vmethod1366(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, int var10);

    public abstract void vmethod1378(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22);

    public abstract void vmethod1362(int var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22);

    /**
     * Adjusts the color values of the given pixel based on the provided color offsets.
     *
     * @param pixel the pixel to adjust
     * @param redOffset the red color offset, from 0 to 255, or -1 to leave unchanged
     * @param greenOffset the green color offset, from 0 to 255, or -1 to leave unchanged
     * @param blueOffset the blue color offset, from 0 to 255, or -1 to leave unchanged
     * @param alphaOffset the alpha color offset, from 0 to 255, or -1 to leave unchanged
     * @return the adjusted pixel value
     */
    public static int adjustColor(int pixel, byte redOffset, byte greenOffset, byte blueOffset, byte alphaOffset) {
        int red = pixel >> 10 & 63;
        int green = pixel >> 7 & 7;
        int blue = pixel & 127;
        int alpha = alphaOffset & 255;
        if (redOffset != -1) {
            red += alpha * (redOffset - red) >> 7;
        }

        if (greenOffset != -1) {
            green += alpha * (greenOffset - green) >> 7;
        }

        if (blueOffset != -1) {
            blue += alpha * (blueOffset - blue) >> 7;
        }

        return (red << 10 | green << 7 | blue) & '\uffff';
    }

}
