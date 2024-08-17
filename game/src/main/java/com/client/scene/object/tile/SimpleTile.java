package com.client.scene.object.tile;

import net.runelite.rs.api.RSSceneTilePaint;

public final class SimpleTile implements RSSceneTilePaint {

    public SimpleTile(int hsl_a, int hsl_b, int hsl_c, int hsl_d, int texture_id, int color_id, boolean flat)
    {
        this.northEastColor = hsl_a;
        this.northColor = hsl_b;
        this.centerColor = hsl_c;
        this.eastColor = hsl_d;
        this.texture = texture_id;
        this.colorRGB = color_id;
        this.flat = flat;
    }

    public int northEastColor;
    public final int northColor;
    public final int centerColor;
    public int eastColor;
    public int texture;
    public boolean flat;
    public int colorRGB;

    public int getNorthEastColor() {
        return northEastColor;
    }

    public int getNorthColor() {
        return northColor;
    }

    public int getCenterColor() {
        return centerColor;
    }

    public int getEastColor() {
        return eastColor;
    }

    public int getTexture() {
        return texture;
    }

    public boolean isFlat() {
        return flat;
    }

    public int getColourRGB() {
        return colorRGB;
    }

    public int bufferOffset = -1;
    public int uVBufferOffset = -1;
    public int bufferLength = -1;

    @Override
    public int getBufferOffset() {
        return bufferOffset;
    }

    @Override
    public void setBufferOffset(int bufferOffset) {
        this.bufferOffset = bufferOffset;
    }

    @Override
    public int getUvBufferOffset() {
        return uVBufferOffset;
    }

    @Override
    public void setUvBufferOffset(int bufferOffset) {
        uVBufferOffset = bufferOffset;
    }

    @Override
    public int getBufferLen() {
        return bufferLength;
    }

    @Override
    public void setBufferLen(int bufferLen) {
        bufferLength = bufferLen;
    }

    @Override
    public int getRBG() {
        return colorRGB;
    }

    @Override
    public int getSwColor() {
        return northEastColor;
    }

    @Override
    public int getSeColor() {
        return northColor;
    }

    @Override
    public int getNwColor() {
        return eastColor;
    }

    @Override
    public int getNeColor() {
        return centerColor;
    }

    @Override
    public boolean getIsFlat() {
        return flat;
    }

    @Override
    public void setRBG(int val) {
        this.colorRGB = val;
    }

    @Override
    public void setSwColor(int val) {

    }

    @Override
    public void setSeColor(int val) {
        this.eastColor = val;
    }

    @Override
    public void setNwColor(int val) {

    }

    @Override
    public void setNeColor(int val) {
        this.northEastColor = val;
    }

    @Override
    public void setIsFlat(boolean val) {
        this.flat = val;
    }

    @Override
    public void setTexture(int val) {
        this.texture = val;
    }

}
