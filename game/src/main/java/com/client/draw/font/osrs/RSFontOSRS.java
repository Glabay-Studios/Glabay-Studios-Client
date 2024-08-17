package com.client.draw.font.osrs;

import com.client.Rasterizer2D;

public final class RSFontOSRS extends AbstractFont implements net.runelite.rs.api.RSFont {

    public RSFontOSRS(byte[] fontData, int[] leftBearings, int[] topBearings, int[] widths, int[] heights, int[] var6, byte[][] pixels) {
        super(fontData, leftBearings, topBearings, widths, heights, var6, pixels);
    }

    public RSFontOSRS(byte[] fontData) {
        super(fontData);
    }

    public void drawGlyph(byte[] fontData, int x, int y, int width, int height, int charIndex) {
        int pixelIndex = y * Rasterizer2D.width + x;
        int rowWidth = Rasterizer2D.width - width;
        int deltaX = 0;
        int deltaY = 0;
        int delta;

        if (y < Rasterizer2D.yClipStart) {
            delta = Rasterizer2D.yClipStart - y;
            height -= delta;
            y = Rasterizer2D.yClipStart;
            deltaY += delta * width;
            pixelIndex += delta * Rasterizer2D.width;
        }

        if (y + height > Rasterizer2D.yClipEnd) {
            height -= y + height - Rasterizer2D.yClipEnd;
        }

        if (x < Rasterizer2D.xClipStart) {
            delta = Rasterizer2D.xClipStart - x;
            width -= delta;
            x = Rasterizer2D.xClipStart;
            deltaY += delta;
            pixelIndex += delta;
            deltaX += delta;
            rowWidth += delta;
        }

        if (x + width > Rasterizer2D.xClipEnd) {
            delta = x + width - Rasterizer2D.xClipEnd;
            width -= delta;
            deltaX += delta;
            rowWidth += delta;
        }

        if (width > 0 && height > 0) {
            AbstractFont.placeGlyph(Rasterizer2D.pixels, fontData, charIndex, deltaY, pixelIndex, width, height, rowWidth, deltaX);
        }
    }


    public void drawGlyphAlpha(byte[] imageData, int x, int y, int width, int height, int color, int alpha) {
        int pixelIndex = y * Rasterizer2D.width + x;
        int rightOverlap = Rasterizer2D.width - width;
        int currentWidth = 0;
        int currentHeight = 0;
        int temp;
        if (y < Rasterizer2D.yClipStart) {
            temp = Rasterizer2D.yClipStart - y;
            height -= temp;
            y = Rasterizer2D.yClipStart;
            currentHeight += temp * width;
            pixelIndex += temp * Rasterizer2D.width;
        }

        if (y + height > Rasterizer2D.yClipEnd) {
            height -= y + height - Rasterizer2D.yClipEnd;
        }

        if (x < Rasterizer2D.xClipStart) {
            temp = Rasterizer2D.xClipStart - x;
            width -= temp;
            x = Rasterizer2D.xClipStart;
            currentHeight += temp;
            pixelIndex += temp;
            currentWidth += temp;
            rightOverlap += temp;
        }

        if (x + width > Rasterizer2D.xClipEnd) {
            temp = x + width - Rasterizer2D.xClipEnd;
            width -= temp;
            currentWidth += temp;
            rightOverlap += temp;
        }

        if (width > 0 && height > 0) {
            placeGlyphAlpha(Rasterizer2D.pixels, imageData, color, currentHeight, pixelIndex, width, height, rightOverlap, currentWidth, alpha);
        }
    }

    @Override
    public int getBaseline() {
        return ascent;
    }

    @Override
    public void drawTextLeftAligned(String text, int x, int y, int fontColor, int shadowColor) {
        draw(text,x,y,fontColor,shadowColor);
    }
}