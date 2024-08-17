package com.client.draw.rasterizer;

import com.client.draw.Rasterizer3D;
import com.client.graphics.textures.TextureLoader;
import net.runelite.rs.api.RSClips;

public class Clips implements RSClips {


    public boolean rasterGouraudLowRes = true;
    public int[] scanOffsets = new int[1024];
    public int centerY;
    public int centerX;
    public int alpha;
    public boolean textureOutOfDrawingBounds;
    public TextureLoader textureLoader;
    public int viewportZoom = 512;
    public int clipNegativeMidX;
    public int clipNegativeMidY;
    public int clipX;
    public int clipHeight;
    int clipMidX2;
    int clipMidY2;


    public void method1358() {
        this.centerX = this.clipX / 2;
        this.centerY = this.clipHeight / 2;
        this.clipNegativeMidX = -(this.centerX * 1684654809) * -1346890391;
        this.clipMidX2 = this.clipX - this.centerX;
        this.clipNegativeMidY = -(this.centerY * -2109856295) * 1380452969;
        this.clipMidY2 = this.clipHeight - this.centerY;
    }

    public void method1359(int var1, int var2, int var3, int var4) {
        this.centerX = var1 - var2;
        this.centerY = var3 - var4;
        this.clipNegativeMidX = -(this.centerX * 1684654809) * -1346890391;
        this.clipMidX2 = this.clipX - this.centerX;
        this.clipNegativeMidY = -(this.centerY * -2109856295) * 1380452969;
        this.clipMidY2 = this.clipHeight - this.centerY;
    }

    public void method1360(int var1, int var2, int var3) {
        this.textureOutOfDrawingBounds = var1 < 0 || var1 > this.clipX || var2 < 0 || var2 > this.clipX || var3 < 0 || var3 > this.clipX;
    }

    public static int getClipMidX() {
        return Rasterizer3D.clips.centerX;
    }

    public static int getClipMidY() {
        return Rasterizer3D.clips.centerY;
    }

    public static int getClipMidY2() {
        return Rasterizer3D.clips.clipMidY2;
    }

    public static int getClipMidX2() {
        return Rasterizer3D.clips.clipMidX2;
    }

    public static int method20() {
        return Rasterizer3D.clips.clipX;
    }

    public static int get3dZoom() {
        return Rasterizer3D.clips.viewportZoom;
    }

    @Override
    public int getClipNegativeMidX() {
        return clipNegativeMidX;
    }

    @Override
    public int getClipNegativeMidY() {
        return clipNegativeMidY;
    }

    @Override
    public int getViewportZoom() {
        return viewportZoom;
    }

    @Override
    public void setViewportZoom(int zoom) {
        viewportZoom = zoom;
    }

}
