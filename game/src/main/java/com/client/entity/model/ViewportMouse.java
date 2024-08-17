package com.client.entity.model;

import com.client.draw.Rasterizer3D;
import com.client.draw.rasterizer.Clips;
import com.client.scene.SceneGraph;

public class ViewportMouse {

    public static boolean isInViewport = false;

    public static int viewportMouseX = 0;

    public static int viewportMouseY = 0;

    static boolean inbounds = false;

    public static int field2744;

    public static int entityCount = 0;

    public static long[] entityTags = new long[1000];

    public static final void setClick(int var0, int var1) {
        ViewportMouse.viewportMouseX = var0;
        ViewportMouse.viewportMouseY = var1;
        ViewportMouse.isInViewport = true;
        ViewportMouse.entityCount = 0;
        ViewportMouse.inbounds = false;
    }

    public static final void method3167() {
        ViewportMouse.isInViewport = false;
        ViewportMouse.entityCount = 0;
    }

    public static int field2207;
    public static int field2715;
    public static int field2208;
    public static int field2209;
    public static int field1157;
    public static int field166;
    public static int field1536;
    public static int field2211;
    public static int field29;
    public static final void method737() {
        if (!ViewportMouse.inbounds) {
            int var0 = SceneGraph.pitchSineY;
            int var1 = SceneGraph.pitchCosineY;
            int var2 = SceneGraph.yawSineX;
            int var3 = SceneGraph.yawCosineX;
            byte var4 = 50;
            short var5 = 3500;
            int var6 = (ViewportMouse.viewportMouseX - Clips.getClipMidX()) * var4 / Clips.get3dZoom();
            int var7 = (ViewportMouse.viewportMouseY - Clips.getClipMidY()) * var4 / Clips.get3dZoom();
            int var8 = (ViewportMouse.viewportMouseX - Clips.getClipMidX()) * var5 / Clips.get3dZoom();
            int var9 = (ViewportMouse.viewportMouseY - Clips.getClipMidY()) * var5 / Clips.get3dZoom();
            int var11 = var1 * var7 + var0 * var4 >> 16;
            int var12 = var1 * var4 - var0 * var7 >> 16;
            int var13 = var5 * var0 + var1 * var9 >> 16;
            int var14 = var1 * var5 - var9 * var0 >> 16;
            int var10 = Rasterizer3D.method2093(var6, var12, var3, var2);
            int var15 = Rasterizer3D.method2295(var6, var12, var3, var2);
            var6 = var10;
            var10 = Rasterizer3D.method2093(var8, var14, var3, var2);
            int var16 = Rasterizer3D.method2295(var8, var14, var3, var2);
            field2207 = (var6 + var10) / 2;
            field2715 = (var11 + var13) / 2;
            field2208 = (var16 + var15) / 2;
            field2209 = (var10 - var6) / 2;
            field1157 = (var13 - var11) / 2;
            field166 = (var16 - var15) / 2;
            field1536 = Math.abs(field2209);
            field2211 = Math.abs(field1157);
            field29 = Math.abs(field166);
        }
    }

    public static final void setEntity(long var0) {
        ViewportMouse.entityTags[++ViewportMouse.entityCount - 1] = var0;
    }

    public static int Entity_unpackID(long arg0) {
        return (int)(arg0 >>> 17 & 4294967295L);
    }
    public static boolean method1417(long arg0) {
        int var2 = (int)(arg0 >>> 14 & 3L);
        return var2 == 2;
    }


    public static boolean method5519(long arg0) {
        boolean var2 = arg0 != 0L;
        if (var2) {
            boolean var3 = 1 == (int)(arg0 >>> 16 & 1L);
            var2 = !var3;
        }

        return var2;
    }

    public static int method3636(long arg0) {
        return (int)(arg0 >>> 7 & 127L);
    }

    public static int method7552(long arg0) {
        return (int)(arg0 >>> 0 & 127L);
    }

    public static int method134(int arg0) {
        return method3636(entityTags[arg0]);
    }

    public static int method1116() {
        return entityCount;
    }

}