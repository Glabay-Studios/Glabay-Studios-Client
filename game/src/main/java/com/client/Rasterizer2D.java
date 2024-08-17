package com.client;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.util.Hashtable;

import com.client.collection.node.DualNode;
import net.runelite.rs.api.RSRasterizer2D;

public class Rasterizer2D extends DualNode implements RSRasterizer2D {


    public static void drawAlpha(int[] pixels, int index, int value, int alpha) {
        if (! Client.instance.isGpu() || pixels != Client.instance.getBufferProvider().getPixels())
        {
            pixels[index] = value;
            return;
        }

        // (int) x * 0x8081 >>> 23 is equivalent to (short) x / 255
        int outAlpha = alpha + ((pixels[index] >>> 24) * (255 - alpha) * 0x8081 >>> 23);
        pixels[index] = value & 0x00FFFFFF | outAlpha << 24;
    }

    public static void initDrawingArea(int height, int width, int[] pixels, float[] depth) {
        Rasterizer2D.pixels = pixels;
        Rasterizer2D.width = width;
        Rasterizer2D.height = height;
        Rasterizer2D.depth = depth;
        setDrawingAreaOSRS(height, 0, width, 0);
    }

    /**
     * Draws an item box filled with a certain colour.
     *
     * @param leftX     The left edge X-Coordinate of the box.
     * @param topY      The top edge Y-Coordinate of the box.
     * @param width     The width of the box.
     * @param height    The height of the box.
     * @param rgbColour The RGBColour of the box.
     */
    public static void drawItemBox(int leftX, int topY, int width, int height, int rgbColour) {
        if (leftX < Rasterizer2D.xClipStart) {
            width -= Rasterizer2D.xClipStart - leftX;
            leftX = Rasterizer2D.xClipStart;
        }
        if (topY < Rasterizer2D.yClipStart) {
            height -= Rasterizer2D.yClipStart - topY;
            topY = Rasterizer2D.yClipStart;
        }
        if (leftX + width > xClipEnd)
            width = xClipEnd - leftX;
        if (topY + height > yClipEnd)
            height = yClipEnd - topY;
        int leftOver = Rasterizer2D.width - width;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++)
                //pixels[pixelIndex++] = rgbColour;
                drawAlpha(pixels, pixelIndex++, rgbColour, 0);
            pixelIndex += leftOver;
        }
    }

    public static void drawAlphaGradient(int x, int y, int gradientWidth, int gradientHeight, int startColor, int endColor, int alpha) {
        int k1 = 0;
        int l1 = 0x10000 / gradientHeight;
        if (x < xClipStart) {
            gradientWidth -= xClipStart - x;
            x = xClipStart;
        }
        if (y < yClipStart) {
            k1 += (yClipStart - y) * l1;
            gradientHeight -= yClipStart - y;
            y = yClipStart;
        }
        if (x + gradientWidth > xClipEnd)
            gradientWidth = xClipEnd - x;
        if (y + gradientHeight > yClipEnd)
            gradientHeight = yClipEnd - y;
        int i2 = width - gradientWidth;
        int result_alpha = 256 - alpha;
        int total_pixels = x + y * width;
        for (int k2 = -gradientHeight; k2 < 0; k2++) {
            int gradient1 = 0x10000 - k1 >> 8;
            int gradient2 = k1 >> 8;
            int gradient_color = ((startColor & 0xff00ff) * gradient1 + (endColor & 0xff00ff) * gradient2 & 0xff00ff00) + ((startColor & 0xff00) * gradient1 + (endColor & 0xff00) * gradient2 & 0xff0000) >>> 8;
            int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
            for (int k3 = -gradientWidth; k3 < 0; k3++) {
                int colored_pixel = pixels[total_pixels];
                colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff) + ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
                drawAlpha(pixels, total_pixels++, color + colored_pixel, alpha);
            }
            total_pixels += i2;
            k1 += l1;
        }
    }


    public static void init(int width, int height, int[] pixels, float[] depth) {
        Rasterizer2D.depth = depth;
        Rasterizer2D.pixels = pixels;
        Rasterizer2D.width = width;
        Rasterizer2D.height = height;
        setDrawingArea(0, 0, width, height);
    }

    protected static void method9607(int[] var0, int var1, int var2, float[] var3) {
        pixels = var0;
        width = var1;
        height = var2;
        depth = var3;
        setClip(0, 0, var1, var2);
    }

    public static void getClipArray(int[] clip) {
        clip[0] = xClipStart;
        clip[1] = yClipStart;
        clip[2] = xClipEnd;
        clip[3] = yClipEnd;
    }

    public static void setClipArray(int[] clip) {
        xClipStart = clip[0];
        yClipStart = clip[1];
        xClipEnd = clip[2];
        yClipEnd = clip[3];
    }

    public static void resetClip() {
        xClipStart = 0;
        yClipStart = 0;
        xClipEnd = width;
        yClipEnd = height;
    }

    public static void resetDepth() {
        if (depth != null) {
            int var0;
            int var1;
            int var2;
            if (xClipStart == 0 && xClipEnd == width && yClipStart == 0 && yClipEnd == height) {
                var0 = depth.length;
                var1 = var0 - (var0 & 7);

                for(var2 = 0; var2 < var1; depth[var2++] = 0.0F) {
                    depth[var2++] = 0.0F;
                    depth[var2++] = 0.0F;
                    depth[var2++] = 0.0F;
                    depth[var2++] = 0.0F;
                    depth[var2++] = 0.0F;
                    depth[var2++] = 0.0F;
                    depth[var2++] = 0.0F;
                }

                while(var2 < var0) {
                    depth[var2++] = 0.0F;
                }
            } else {
                var0 = xClipEnd - xClipStart;
                var1 = yClipEnd - yClipStart;
                var2 = width - var0;
                int var3 = yClipStart * width + xClipStart;
                int var4 = var0 >> 3;
                int var5 = var0 & 7;
                var0 = var3 - 1;

                for(int var7 = -var1; var7 < 0; ++var7) {
                    int var6;
                    if (var4 > 0) {
                        var6 = var4;

                        do {
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            ++var0;
                            depth[var0] = 0.0F;
                            --var6;
                        } while(var6 > 0);
                    }

                    if (var5 > 0) {
                        var6 = var5;

                        do {
                            ++var0;
                            depth[var0] = 0.0F;
                            --var6;
                        } while(var6 > 0);
                    }

                    var0 += var2;
                }
            }

        }
    }

    public static void setClip(int leftX, int topY, int rightX, int bottomY) {
        setDrawingAreaOSRS(bottomY,leftX,rightX,topY);
    }

    public static void rasterizerDrawGradientAlpha(int x, int y, int w, int h, int rgbTop, int rgbBottom, int alphaTop, int alphaBottom) {
        if (w > 0 && h > 0) {
            int var8 = 0;
            int var9 = 65536 / h;
            if (x < Rasterizer2D.xClipStart) {
                w -= Rasterizer2D.xClipStart - x;
                x = Rasterizer2D.xClipStart;
            }

            if (y < Rasterizer2D.yClipStart) {
                var8 += (Rasterizer2D.yClipStart - y) * var9;
                h -= Rasterizer2D.yClipStart - y;
                y = Rasterizer2D.yClipStart;
            }

            if (x + w > Rasterizer2D.xClipEnd) {
                w = Rasterizer2D.xClipEnd - x;
            }

            if (h + y > Rasterizer2D.yClipEnd) {
                h = Rasterizer2D.yClipEnd - y;
            }

            int var10 = Rasterizer2D.width - w;
            int var11 = x + Rasterizer2D.width * y;

            for(int var12 = -h; var12 < 0; ++var12) {
                int var13 = 65536 - var8 >> 8;
                int var14 = var8 >> 8;
                int var15 = (var13 * alphaTop + var14 * alphaBottom & '\uff00') >>> 8;
                if (var15 == 0) {
                    var11 += Rasterizer2D.width;
                    var8 += var9;
                } else {
                    int var16 = (var14 * (rgbBottom & 16711935) + var13 * (rgbTop & 16711935) & -16711936) + (var14 * (rgbBottom & '\uff00') + var13 * (rgbTop & '\uff00') & 16711680) >>> 8;
                    int var17 = 255 - var15;
                    int var18 = ((var16 & 16711935) * var15 >> 8 & 16711935) + (var15 * (var16 & '\uff00') >> 8 & '\uff00');

                    for(int var19 = -w; var19 < 0; ++var19) {
                        int var20 = Rasterizer2D.pixels[var11];
                        if (var20 == 0) {
                            drawAlpha(Rasterizer2D.pixels, var11++, var18, var15);
                        } else {
                            var20 = ((var20 & 16711935) * var17 >> 8 & 16711935) + (var17 * (var20 & '\uff00') >> 8 & '\uff00');
                            drawAlpha(Rasterizer2D.pixels, var11++, var18 + var20, var15);
                        }
                    }

                    var11 += var10;
                    var8 += var9;
                }
            }
        }
    }

    /**
     * Draws a coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     */
    public static void drawVerticalLine(int xPosition, int yPosition, int height, int rgbColour){
        if(xPosition < xClipStart || xPosition >= xClipEnd)
            return;
        if(yPosition < yClipStart){
            height -= yClipStart - yPosition;
            yPosition = yClipStart;
        }
        if(yPosition + height > yClipEnd)
            height = yClipEnd - yPosition;
        int pixelIndex = xPosition + yPosition * width;
        for(int rowIndex = 0; rowIndex < height; rowIndex++)
            drawAlpha(pixels, pixelIndex + rowIndex * width, rgbColour, 255);
    }


    /**
     * Draws a transparent box with a gradient that changes from top to bottom.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The width of the box.
     * @param height The height of the box.
     * @param topColour The top rgbColour of the gradient.
     * @param bottomColour The bottom rgbColour of the gradient.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentGradientBox(int leftX, int topY, int width, int height, int topColour, int bottomColour, int opacity) {
        int gradientProgress = 0;
        int progressPerPixel = 0x10000 / height;
        if (leftX < Rasterizer2D.xClipStart) {
            width -= Rasterizer2D.xClipStart - leftX;
            leftX = Rasterizer2D.xClipStart;
        }
        if (topY < Rasterizer2D.yClipStart) {
            gradientProgress += (Rasterizer2D.yClipStart - topY) * progressPerPixel;
            height -= Rasterizer2D.yClipStart - topY;
            topY = Rasterizer2D.yClipStart;
        }
        if (leftX + width > xClipEnd)
            width = xClipEnd - leftX;
        if (topY + height > yClipEnd)
            height = yClipEnd - topY;
        int leftOver = Rasterizer2D.width - width;
        int transparency = 256 - opacity;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            int gradient = 0x10000 - gradientProgress >> 8;
            int inverseGradient = gradientProgress >> 8;
            int gradientColour = ((topColour & 0xff00ff) * gradient + (bottomColour & 0xff00ff) * inverseGradient & 0xff00ff00) + ((topColour & 0xff00) * gradient + (bottomColour & 0xff00) * inverseGradient & 0xff0000) >>> 8;
            int transparentPixel = ((gradientColour & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((gradientColour & 0xff00) * opacity >> 8 & 0xff00);
            for (int columnIndex = 0; columnIndex < width; columnIndex++) {
                int backgroundPixel = pixels[pixelIndex];
                backgroundPixel = ((backgroundPixel & 0xff00ff) * transparency >> 8 & 0xff00ff) + ((backgroundPixel & 0xff00) * transparency >> 8 & 0xff00);
                drawAlpha(pixels,pixelIndex++,transparentPixel + backgroundPixel,opacity);
            }
            pixelIndex += leftOver;
            gradientProgress += progressPerPixel;
        }
    }

    /**
     * Sets the drawingArea to the default size and position.
     * Position: Upper left corner.
     * Size: As specified before.
     */
    public static void set_default_size() {
        xClipStart = 0;
        yClipStart = 0;
        xClipEnd = width;
        yClipEnd = height;

    }

    public static void setDrawingArea(int x, int y, int width, int height) {
        if(x < 0) {
            x = 0;
        }
        if(y < 0) {
            y = 0;
        }
        if (width > Rasterizer2D.width) {
            width = Rasterizer2D.width;
        }
        if (height > Rasterizer2D.height) {
            height = Rasterizer2D.height;
        }
        xClipStart = x;
        yClipStart = y;
        xClipEnd = width;
        yClipEnd = height;

    }

    public static void expandClip(int var0, int var1, int var2, int var3) {
        if (xClipStart < var0) {
            xClipStart = var0;
        }

        if (yClipStart < var1) {
            yClipStart = var1;
        }

        if (xClipEnd > var2) {
            xClipEnd = var2;
        }

        if (yClipEnd > var3) {
            yClipEnd = var3;
        }
    }

    /**
     * Sets the drawingArea based on the coordinates of the edges.
     * @param bottomY The bottom edge Y-Coordinate.
     * @param leftX The left edge X-Coordinate.
     * @param rightX The right edge X-Coordinate.
     * @param topY The top edge Y-Coordinate.
     */
    public static void setDrawingAreaOSRS(int bottomY, int leftX, int rightX, int topY) {
        if(leftX < 0) {
            leftX = 0;
        }
        if(topY < 0) {
            topY = 0;
        }
        if(rightX > width) {
            rightX = width;
        }
        if(bottomY > height) {
            bottomY = height;
        }
        Rasterizer2D.xClipStart = leftX;
        Rasterizer2D.yClipStart = topY;
        Rasterizer2D.xClipEnd = rightX;
        Rasterizer2D.yClipEnd = bottomY;

    }

    /**
     * Clears the drawingArea by setting every pixel to 0 (black).
     */
    public static void clear()    {
        int size = width * height;
        for(int coordinates = 0; coordinates < size; coordinates++) {
            pixels[coordinates] = 0;
        }
        resetDepth();
    }

    public static void Rasterizer2D_clear() {
        int var0 = 0;

        int var1;
        for (var1 = width * height - 7; var0 < var1; pixels[var0++] = 0) {
            pixels[var0++] = 0;
            pixels[var0++] = 0;
            pixels[var0++] = 0;
            pixels[var0++] = 0;
            pixels[var0++] = 0;
            pixels[var0++] = 0;
            pixels[var0++] = 0;
        }

        for (var1 += 7; var0 < var1; pixels[var0++] = 0) {
        }

        resetDepth();
    }

    public static void draw_filled_rect(int x, int y, int width, int height, int color) {
        draw_filled_rect(x, y, width, height, color, 255);
    }

    /**
     * Draws a transparent box.
     * @param leftX The left edge X-Coordinate of the box.
     * @param topY The top edge Y-Coordinate of the box.
     * @param width The box width.
     * @param height The box height.
     * @param rgbColour The box colour.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void draw_filled_rect(int leftX, int topY, int width, int height, int rgbColour, int opacity) {
        if (leftX < Rasterizer2D.xClipStart) {
            width -= Rasterizer2D.xClipStart - leftX;
            leftX = Rasterizer2D.xClipStart;
        }
        if (topY < Rasterizer2D.yClipStart) {
            height -= Rasterizer2D.yClipStart - topY;
            topY = Rasterizer2D.yClipStart;
        }
        if (leftX + width > xClipEnd)
            width = xClipEnd - leftX;
        if (topY + height > yClipEnd)
            height = yClipEnd - topY;
        int transparency = 256 - opacity;
        int red = (rgbColour >> 16 & 0xff) * opacity;
        int green = (rgbColour >> 8 & 0xff) * opacity;
        int blue = (rgbColour & 0xff) * opacity;
        int leftOver = Rasterizer2D.width - width;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++) {
                int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
                int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
                int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
                int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
                drawAlpha(pixels,pixelIndex++,transparentColour,opacity);
            }
            pixelIndex += leftOver;
        }
    }

    public static void drawPixels(int height, int posY, int posX, int color, int w) {
        if (posX < xClipStart) {
            w -= xClipStart - posX;
            posX = xClipStart;
        }
        if (posY < yClipStart) {
            height -= yClipStart - posY;
            posY = yClipStart;
        }
        if (posX + w > xClipEnd) {
            w = xClipEnd - posX;
        }
        if (posY + height > yClipEnd) {
            height = yClipEnd - posY;
        }
        int k1 = width - w;
        int l1 = posX + posY * width;
        for (int i2 = -height; i2 < 0; i2++) {
            for (int j2 = -w; j2 < 0; j2++) {
                drawAlpha(pixels,l1++,color,255);
            }

            l1 += k1;
        }
    }

    /**
     * Draws a 1 pixel thick box outline in a certain colour.
     * @param x The left edge X-Coordinate.
     * @param y The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     */
    public static void draw_rect_outline(int x, int y, int width, int height, int rgbColour) {
        draw_horizontal_line(x, y, width, rgbColour);
        draw_horizontal_line(x, (y + height) - 1, width, rgbColour);
        draw_vertical_line(x, y, height, rgbColour);
        draw_vertical_line((x + width) - 1, y, height, rgbColour);
    }

    /**
     * Draws a coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     */
    public static void draw_horizontal_line(int xPosition, int yPosition, int width, int rgbColour) {
        if (yPosition < yClipStart || yPosition >= yClipEnd)
            return;
        if (xPosition < xClipStart) {
            width -= xClipStart - xPosition;
            xPosition = xClipStart;
        }
        if (xPosition + width > xClipEnd)
            width = xClipEnd - xPosition;
        int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
        for (int i = 0; i < width; i++)
            drawAlpha(pixels,pixelIndex + i,rgbColour,255);
    }

    public static void drawStroke(int xPos, int yPos, int width, int height, int color, int strokeWidth) {

        drawVerticalStrokeLine(xPos, yPos, height, color, strokeWidth);
        drawVerticalStrokeLine((xPos + width) - strokeWidth, yPos, height, color, strokeWidth);
        drawHorizontalStrokeLine(xPos, yPos, width, color, strokeWidth);
        drawHorizontalStrokeLine(xPos, (yPos + height) - strokeWidth, width, color, strokeWidth);

    }

    /**
     * Draws a coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     */
    public static void drawHorizontalLine(int xPosition, int yPosition, int width, int rgbColour){
        if(yPosition < yClipStart || yPosition >= yClipEnd)
            return;
        if(xPosition < xClipStart){
            width -= xClipStart - xPosition;
            xPosition = xClipStart;
        }
        if(xPosition + width > xClipEnd)
            width = xClipEnd - xPosition;
        int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
        for(int i = 0; i < width; i++)
            drawAlpha(pixels, pixelIndex + i, rgbColour, 255);
    }

    public static void drawHorizontalStrokeLine(int xPos, int yPos, int w, int hexColor, int strokeWidth) {

        if (yPos < yClipStart || yPos >= yClipEnd)
            return;
        if (xPos < xClipStart) {
            w -= xClipStart - xPos;
            xPos = xClipStart;
        }
        if (xPos + w > xClipEnd)
            w = xClipEnd - xPos;
        int index = xPos + yPos * width;
        int leftWidth = width - w;
        for (int x = 0; x < strokeWidth; x++) {
            for (int y = 0; y < w; y++) {
                drawAlpha(pixels,index++,hexColor,255);
            }
            index += leftWidth;
        }

    }

    public static void drawVerticalStrokeLine(int xPosition, int yPosition, int height, int hexColor, int strokeWidth) {
        if (xPosition < xClipStart || xPosition >= xClipEnd)
            return;
        if (yPosition < yClipStart) {
            height -= yClipStart - yPosition;
            yPosition = yClipStart;
        }
        if (yPosition + height > yClipEnd)
            height = yClipEnd - yPosition;
        int pixelIndex = xPosition + yPosition * width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int x = 0; x < strokeWidth; x++) {
                drawAlpha(pixels,pixelIndex + x + rowIndex * width,hexColor,255);
            }
        }
    }

    /**
     * Draws a coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     */
    public static void draw_vertical_line(int xPosition, int yPosition, int height, int rgbColour) {
        if (xPosition < xClipStart || xPosition >= xClipEnd)
            return;
        if (yPosition < yClipStart) {
            height -= yClipStart - yPosition;
            yPosition = yClipStart;
        }
        if (yPosition + height > yClipEnd)
            height = yClipEnd - yPosition;
        int pixelIndex = xPosition + yPosition * width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++)
            drawAlpha(pixels,pixelIndex + rowIndex * width,rgbColour,255);
    }

    public static void drawHorizontalLine(int x, int y, int length, int color, int alpha) {
        if (y < yClipStart || y >= yClipEnd) {
            return;
        }
        if (x < xClipStart) {
            length -= xClipStart - x;
            x = xClipStart;
        }
        if (x + length > xClipEnd) {
            length = xClipEnd - x;
        }
        final int j1 = 256 - alpha;
        final int k1 = (color >> 16 & 0xff) * alpha;
        final int l1 = (color >> 8 & 0xff) * alpha;
        final int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for (int j3 = 0; j3 < length; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
            drawAlpha(pixels,i3++,k3,255);
        }
    }

    public static void draw_line(int i, int j, int k, int l)
    {
        if (i < yClipStart || i >= yClipEnd)
            return;
        if (l < xClipStart)
        {
            k -= xClipStart - l;
            l = xClipStart;
        }
        if (l + k > xClipEnd)
            k = xClipEnd - l;
        int i1 = l + i * width;
        for (int j1 = 0; j1 < k; j1++)
            drawAlpha(pixels,i1 + j1,j,255);

    }

    public static void drawAlphaBox(int x, int y, int lineWidth, int lineHeight, int color, int alpha) {
        if (y < yClipStart) {
            if (y > (yClipStart - lineHeight)) {
                lineHeight -= (yClipStart - y);
                y += (yClipStart - y);
            } else {
                return;
            }
        }
        if (y + lineHeight > yClipEnd) {
            lineHeight -= y + lineHeight - yClipEnd;
        }
        //if (y >= bottomY - lineHeight)
        //return;
        if (x < xClipStart) {
            lineWidth -= xClipStart - x;
            x = xClipStart;
        }
        if (x + lineWidth > xClipEnd)
            lineWidth = xClipEnd - x;
        for(int yOff = 0; yOff < lineHeight; yOff++) {
            int i3 = x + (y + (yOff)) * width;
            for (int j3 = 0; j3 < lineWidth; j3++) {
                //int alpha2 = (lineWidth-j3) / (lineWidth/alpha);
                int j1 = 256 - alpha;//alpha2 is for gradient
                int k1 = (color >> 16 & 0xff) * alpha;
                int l1 = (color >> 8 & 0xff) * alpha;
                int i2 = (color & 0xff) * alpha;
                int j2 = (pixels[i3] >> 16 & 0xff) * j1;
                int k2 = (pixels[i3] >> 8 & 0xff) * j1;
                int l2 = (pixels[i3] & 0xff) * j1;
                int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
                        + (i2 + l2 >> 8);
                drawAlpha(pixels,i3 ++,k3,alpha);
            }
        }
    }

    /**
     * Draws a 1 pixel thick transparent box outline in a certain colour.
     * @param leftX The left edge X-Coordinate
     * @param topY The top edge Y-Coordinate.
     * @param width The width.
     * @param height The height.
     * @param rgbColour The RGB-Colour.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentBoxOutline(int leftX, int topY, int width, int height, int rgbColour, int opacity) {
        drawTransparentHorizontalLine(leftX, topY, width, rgbColour, opacity);
        drawTransparentHorizontalLine(leftX, topY + height - 1, width, rgbColour, opacity);
        if (height >= 3) {
            drawTransparentVerticalLine(leftX, topY + 1, height - 2, rgbColour, opacity);
            drawTransparentVerticalLine(leftX + width - 1, topY + 1, height - 2, rgbColour, opacity);
        }
    }

    /**
     * Draws a transparent coloured horizontal line in the drawingArea.
     * @param xPosition The start X-Position of the line.
     * @param yPosition The Y-Position of the line.
     * @param width The width of the line.
     * @param rgbColour The colour of the line.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentHorizontalLine(int xPosition, int yPosition, int width, int rgbColour, int opacity) {
        if (yPosition < yClipStart || yPosition >= yClipEnd) {
            return;
        }
        if (xPosition < xClipStart) {
            width -= xClipStart - xPosition;
            xPosition = xClipStart;
        }
        if (xPosition + width > xClipEnd) {
            width = xClipEnd - xPosition;
        }
        final int transparency = 256 - opacity;
        final int red = (rgbColour >> 16 & 0xff) * opacity;
        final int green = (rgbColour >> 8 & 0xff) * opacity;
        final int blue = (rgbColour & 0xff) * opacity;
        int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
        for (int i = 0; i < width; i++) {
            final int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
            final int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
            final int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
            final int transparentColour = (red + otherRed >> 8 << 16) + (green + otherGreen >> 8 << 8) + (blue + otherBlue >> 8);
            drawAlpha(pixels,pixelIndex ++,transparentColour,opacity);
        }
    }

    /**
     * Draws a transparent coloured vertical line in the drawingArea.
     * @param xPosition The X-Position of the line.
     * @param yPosition The start Y-Position of the line.
     * @param height The height of the line.
     * @param rgbColour The colour of the line.
     * @param opacity The opacity value ranging from 0 to 256.
     */
    public static void drawTransparentVerticalLine(int xPosition, int yPosition, int height, int rgbColour, int opacity) {
        if (xPosition < xClipStart || xPosition >= xClipEnd) {
            return;
        }
        if (yPosition < yClipStart) {
            height -= yClipStart - yPosition;
            yPosition = yClipStart;
        }
        if (yPosition + height > yClipEnd) {
            height = yClipEnd - yPosition;
        }
        final int transparency = 256 - opacity;
        final int red = (rgbColour >> 16 & 0xff) * opacity;
        final int green = (rgbColour >> 8 & 0xff) * opacity;
        final int blue = (rgbColour & 0xff) * opacity;
        int pixelIndex = xPosition + yPosition * width;
        for (int i = 0; i < height; i++) {
            final int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
            final int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
            final int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
            final int transparentColour = (red + otherRed >> 8 << 16) + (green + otherGreen >> 8 << 8) + (blue + otherBlue >> 8);
            drawAlpha(pixels,pixelIndex,transparentColour,opacity);
            pixelIndex += width;
        }
    }

    public static Graphics2D createGraphics(boolean renderingHints) {
        Graphics2D g2d = createGraphics(pixels, width, height);
        if (renderingHints) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        return g2d;
    }

    public static Graphics2D createGraphics(int[] pixels, int width, int height) {
        return new BufferedImage(COLOR_MODEL, Raster.createWritableRaster(COLOR_MODEL.createCompatibleSampleModel(width, height), new DataBufferInt(pixels, width * height), null), false, new Hashtable<Object, Object>()).createGraphics();
    }

    public static Shape createSector(int x, int y, int r, int angle) {
        return new Arc2D.Double(x, y, r, r, 90, -angle, Arc2D.PIE);
    }

    public static Shape createCircle(int x, int y, int r) {
        return new Ellipse2D.Double(x, y, r, r);
    }

    public static Shape createRing(Shape sector, Shape innerCircle) {
        Area ring = new Area(sector);
        ring.subtract(new Area(innerCircle));
        return ring;
    }

    public static void drawFilledCircle(int x, int y, int radius, int color, int alpha) {
        int y1 = y - radius;
        if (y1 < 0) {
            y1 = 0;
        }
        int y2 = y + radius;
        if (y2 >= height) {
            y2 = height - 1;
        }
        int a2 = 256 - alpha;
        int r1 = (color >> 16 & 0xff) * alpha;
        int g1 = (color >> 8 & 0xff) * alpha;
        int b1 = (color & 0xff) * alpha;
        for (int iy = y1; iy <= y2; iy++) {
            int dy = iy - y;
            int dist = (int) Math.sqrt(radius * radius - dy * dy);
            int x1 = x - dist;
            if (x1 < 0) {
                x1 = 0;
            }
            int x2 = x + dist;
            if (x2 >= width) {
                x2 = width - 1;
            }
            int pos = x1 + iy * width;
            for (int ix = x1; ix <= x2; ix++) {
                /*  Tried replacing all pixels[pos] with:
                    Client.instance.gameScreenImageProducer.canvasRaster[pos]
                    AND Rasterizer3D.pixels[pos] */
                int r2 = (pixels[pos] >> 16 & 0xff) * a2;
                int g2 = (pixels[pos] >> 8 & 0xff) * a2;
                int b2 = (pixels[pos] & 0xff) * a2;
                drawAlpha(pixels,pos++,((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8),255);
            }
        }
    }

    public static void drawRectangle(int x, int y, int width, int height, int color, int alpha) {
        drawHorizontalLine(x, y, width, color, alpha);
        drawHorizontalLine(x, y + height - 1, width, color, alpha);
        if(height >= 3) {
            drawAlphaVerticalLine(x, y + 1, height - 2, color, alpha);
            drawAlphaVerticalLine(x + width - 1, y + 1, height - 2, color, alpha);
        }
    }

    public static void drawAlphaVerticalLine(int x, int y, int length, int color, int alpha) {
        if(x < xClipStart || x >= xClipEnd) {
            return;
        }
        if(y < yClipStart) {
            length -= yClipStart - y;
            y = yClipStart;
        }
        if(y + length > yClipEnd) {
            length = yClipEnd - y;
        }
        final int j1 = 256 - alpha;
        final int k1 = (color >> 16 & 0xff) * alpha;
        final int l1 = (color >> 8 & 0xff) * alpha;
        final int i2 = (color & 0xff) * alpha;
        int i3 = x + y * width;
        for(int j3 = 0; j3 < length; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = (k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8);
            drawAlpha(pixels,i3,(k1 + j2 >> 8 << 16) + (l1 + k2 >> 8 << 8) + (i2 + l2 >> 8),255);
            i3 += width;
        }
    }

    public static void fillRectangle(int x, int y, int w, int h, int color) {
        if (x < xClipStart) {
            w -= xClipStart - x;
            x = xClipStart;
        }
        if (y < yClipStart) {
            h -= yClipStart - y;
            y = yClipStart;
        }
        if (x + w > xClipEnd) {
            w = xClipEnd - x;
        }
        if (y + h > yClipEnd) {
            h = yClipEnd - y;
        }
        int k1 = width - w;
        int l1 = x + y * width;
        for (int i2 = -h; i2 < 0; i2++) {
            for (int j2 = -w; j2 < 0; j2++) {
                pixels[l1++] = color;

            }
            l1 += k1;
        }
    }

    public static void fillRectangle(int x, int y, int w, int h, int color, int alpha) {
        if (x < xClipStart) {
            w -= xClipStart - x;
            x = xClipStart;
        }
        if (y < yClipStart) {
            h -= yClipStart - y;
            y = yClipStart;
        }
        if (x + w > xClipEnd) {
            w = xClipEnd - x;
        }
        if (y + h > yClipEnd) {
            h = yClipEnd - y;
        }
        int a2 = 256 - alpha;
        int r1 = (color >> 16 & 0xff) * alpha;
        int g1 = (color >> 8 & 0xff) * alpha;
        int b1 = (color & 0xff) * alpha;
        int k3 = width - w;
        int pixel = x + y * width;
        for (int i4 = 0; i4 < h; i4++) {
            for (int j4 = -w; j4 < 0; j4++) {
                int r2 = (pixels[pixel] >> 16 & 0xff) * a2;
                int g2 = (pixels[pixel] >> 8 & 0xff) * a2;
                int b2 = (pixels[pixel] & 0xff) * a2;
                int rgb = ((r1 + r2 >> 8) << 16) + ((g1 + g2 >> 8) << 8) + (b1 + b2 >> 8);
                drawAlpha(pixels,pixel++,rgb,255);
            }
            pixel += k3;
        }
    }

    public static final void drawAlphaCircle(final int x, int y, int radius, final int color, final int alpha) {
        if (alpha != 0) {
            if (alpha == 256) {
                drawCircle(x, y, radius, color);
            } else {
                if (radius < 0) {
                    radius = -radius;
                }
                final int opacity = 256 - alpha;
                final int source_red = (color >> 16 & 0xff) * alpha;
                final int source_green = (color >> 8 & 0xff) * alpha;
                final int source_blue = (color & 0xff) * alpha;
                int diameter_start = y - radius;
                if (diameter_start < yClipStart) {
                    diameter_start = yClipStart;
                }
                int diameter_end = y + radius + 1;
                if (diameter_end > yClipEnd) {
                    diameter_end = yClipEnd;
                }
                int i_26_ = diameter_start;
                final int i_27_ = radius * radius;
                int i_28_ = 0;
                int i_29_ = y - i_26_;
                int i_30_ = i_29_ * i_29_;
                int i_31_ = i_30_ - i_29_;
                if (y > diameter_end) {
                    y = diameter_end;
                }
                while (i_26_ < y) {
                    for (/**/; i_31_ <= i_27_ || i_30_ <= i_27_; i_31_ += i_28_++ + i_28_) {
                        i_30_ += i_28_ + i_28_;
                    }
                    int i_32_ = x - i_28_ + 1;
                    if (i_32_ < xClipStart) {
                        i_32_ = xClipStart;
                    }
                    int i_33_ = x + i_28_;
                    if (i_33_ > xClipEnd) {
                        i_33_ = xClipEnd;
                    }
                    int coordinates = i_32_ + i_26_ * width;
                    for (int i_35_ = i_32_; i_35_ < i_33_; i_35_++) {
                        final int dest_red = (pixels[coordinates] >> 16 & 0xff) * opacity;
                        final int dest_green = (pixels[coordinates] >> 8 & 0xff) * opacity;
                        final int dest_blue = (pixels[coordinates] & 0xff) * opacity;
                        final int dest_color = (source_red + dest_red >> 8 << 16) + (source_green + dest_green >> 8 << 8) + (source_blue + dest_blue >> 8);
                        pixels[coordinates++] = dest_color;
                    }
                    i_26_++;
                    i_30_ -= i_29_-- + i_29_;
                    i_31_ -= i_29_ + i_29_;
                }
                i_28_ = radius;
                i_29_ = -i_29_;
                i_31_ = i_29_ * i_29_ + i_27_;
                i_30_ = i_31_ - i_28_;
                i_31_ -= i_29_;
                while (i_26_ < diameter_end) {
                    for (/**/; i_31_ > i_27_ && i_30_ > i_27_; i_30_ -= i_28_ + i_28_) {
                        i_31_ -= i_28_-- + i_28_;
                    }
                    int i_40_ = x - i_28_;
                    if (i_40_ < xClipStart) {
                        i_40_ = xClipStart;
                    }
                    int i_41_ = x + i_28_;
                    if (i_41_ > xClipEnd - 1) {
                        i_41_ = xClipEnd - 1;
                    }
                    int coordinates = i_40_ + i_26_ * width;
                    for (int i_43_ = i_40_; i_43_ <= i_41_; i_43_++) {
                        final int i_44_ = (pixels[coordinates] >> 16 & 0xff) * opacity;
                        final int i_45_ = (pixels[coordinates] >> 8 & 0xff) * opacity;
                        final int i_46_ = (pixels[coordinates] & 0xff) * opacity;
                        final int i_47_ = (source_red + i_44_ >> 8 << 16) + (source_green + i_45_ >> 8 << 8) + (source_blue + i_46_ >> 8);
                        pixels[coordinates++] = i_47_;
                    }
                    i_26_++;
                    i_31_ += i_29_ + i_29_;
                    i_30_ += i_29_++ + i_29_;
                }
            }
        }
    }

    private static final void setPixel(final int x, final int y, final int color) {
        if (x >= xClipStart && y >= yClipStart && x < xClipEnd && y < yClipEnd) {
            pixels[x + y * width] = color;
        }
    }

    private static final void drawCircle(final int x, int y, int radius, final int color) {
        if (radius == 0) {
            setPixel(x, y, color);
        } else {
            if (radius < 0) {
                radius = -radius;
            }
            int i_67_ = y - radius;
            if (i_67_ < yClipStart) {
                i_67_ = yClipStart;
            }
            int i_68_ = y + radius + 1;
            if (i_68_ > yClipEnd) {
                i_68_ = yClipEnd;
            }
            int i_69_ = i_67_;
            final int i_70_ = radius * radius;
            int i_71_ = 0;
            int i_72_ = y - i_69_;
            int i_73_ = i_72_ * i_72_;
            int i_74_ = i_73_ - i_72_;
            if (y > i_68_) {
                y = i_68_;
            }
            while (i_69_ < y) {
                for (/**/; i_74_ <= i_70_ || i_73_ <= i_70_; i_74_ += i_71_++ + i_71_) {
                    i_73_ += i_71_ + i_71_;
                }
                int i_75_ = x - i_71_ + 1;
                if (i_75_ < xClipStart) {
                    i_75_ = xClipStart;
                }
                int i_76_ = x + i_71_;
                if (i_76_ > xClipEnd) {
                    i_76_ = xClipEnd;
                }
                int i_77_ = i_75_ + i_69_ * width;
                for (int i_78_ = i_75_; i_78_ < i_76_; i_78_++) {
                    pixels[i_77_++] = color;
                }
                i_69_++;
                i_73_ -= i_72_-- + i_72_;
                i_74_ -= i_72_ + i_72_;
            }
            i_71_ = radius;
            i_72_ = i_69_ - y;
            i_74_ = i_72_ * i_72_ + i_70_;
            i_73_ = i_74_ - i_71_;
            i_74_ -= i_72_;
            while (i_69_ < i_68_) {
                for (/**/; i_74_ > i_70_ && i_73_ > i_70_; i_73_ -= i_71_ + i_71_) {
                    i_74_ -= i_71_-- + i_71_;
                }
                int i_79_ = x - i_71_;
                if (i_79_ < xClipStart) {
                    i_79_ = xClipStart;
                }
                int i_80_ = x + i_71_;
                if (i_80_ > xClipEnd - 1) {
                    i_80_ = xClipEnd - 1;
                }
                int i_81_ = i_79_ + i_69_ * width;
                for (int i_82_ = i_79_; i_82_ <= i_80_; i_82_++) {
                    drawAlpha(pixels,i_81_++,color,255);
                }
                i_69_++;
                i_74_ += i_72_ + i_72_;
                i_73_ += i_72_++ + i_72_;
            }
        }
    }

    public static void draw_rectangle_outline(int x, int y, int line_width, int line_height, int color) {
        draw_vertical_line1(x, y, line_width, color);
        draw_vertical_line1(x, line_height + y - 1, line_width, color);
        draw_horizontal_line1(x, y, line_height, color);
        draw_horizontal_line1(x + line_width - 1, y, line_height, color);
    }

    public static void draw_vertical_line1(int x, int y, int line_width, int color) {
        if (y >= yClipStart && y < yClipEnd) {
            if (x < xClipStart) {
                line_width -= xClipStart - x;
                x = xClipStart;
            }
            if (x + line_width > xClipEnd) {
                line_width = xClipEnd - x;
            }
            int coordinates = x + width * y;
            for (int step = 0; step < line_width; step++) {
                drawAlpha(pixels,coordinates + step,color,255);
            }
        }
    }

    public static void draw_horizontal_line1(int x, int y, int line_height, int color) {
        if (x >= xClipStart && x < xClipEnd) {
            if (y < yClipStart) {
                line_height -= yClipStart - y;
                y = yClipStart;
            }
            if (line_height + y > yClipEnd) {
                line_height = yClipEnd - y;
            }
            int coordinates = x + width * y;
            for (int step = 0; step < line_height; step++) {
                drawAlpha(pixels,coordinates + step * width,color,255);
            }
        }
    }

    public static void draw_line(int x, int y, int line_width, int line_height, int color) {
        line_width -= x;
        line_height -= y;
        if (line_height == 0) {//check for straight lines
            if (line_width >= 0) {
                draw_vertical_line1(x, y, line_width + 1, color);
            } else {
                draw_vertical_line1(x + line_width, y, -line_width + 1, color);
            }

        } else if (line_width == 0) {//check for straight lines
            if (line_height >= 0) {
                draw_horizontal_line1(x, y, line_height + 1, color);
            } else {
                draw_horizontal_line1(x, line_height + y, -line_height + 1, color);
            }
        } else {
            //bresenham algorithm?
            if (line_height + line_width < 0) {
                x += line_width;
                line_width = -line_width;
                y += line_height;
                line_height = -line_height;
            }
            int height_step;
            int width_step;
            if (line_width > line_height) {
                y <<= 16;
                y += 16384;
                line_height <<= 16;
                height_step = (int) Math.floor((double) line_height / (double) line_width + 0.5D);
                line_width += x;
                if (x < xClipStart) {
                    y += height_step * (xClipStart - x);
                    x = xClipStart;
                }
                if (line_width >= xClipEnd) {
                    line_width = xClipEnd - 1;
                }
                while (x <= line_width) {
                    width_step = y >> 16;
                    if (width_step >= yClipStart && width_step < yClipEnd) {
                        drawAlpha(pixels,x + width_step * width,color,255);
                    }
                    y += height_step;
                    x++;
                }
            } else {
                x <<= 16;
                x += 16384;
                line_width <<= 16;
                height_step = (int) Math.floor((double) line_width / (double) line_height + 0.5D);
                line_height += y;
                if (y < yClipStart) {
                    x += (yClipStart - y) * height_step;
                    y = yClipStart;
                }
                if (line_height >= yClipEnd) {
                    line_height = yClipEnd - 1;
                }
                while (y <= line_height) {
                    width_step = x >> 16;
                    if (width_step >= xClipStart && width_step < xClipEnd) {
                        drawAlpha(pixels,width_step * width,color,255);
                    }
                    x += height_step;
                    y++;
                }
            }
        }
    }

    private static final ColorModel COLOR_MODEL = new DirectColorModel(32, 0xff0000, 0xff00, 0xff);

    public static int[] pixels;
    public static int width;
    public static int height;
    public static int yClipStart;
    public static int yClipEnd;
    public static int xClipStart;
    public static int xClipEnd;

    public static float[] depth;

    static {
        yClipStart = 0;
        yClipEnd = 0;
        xClipStart = 0;
        xClipEnd = 0;
    }

    public static void drawBox(int leftX, int topY, int width, int height, int rgbColour) {
        if (leftX < Rasterizer2D.xClipStart) {
            width -= Rasterizer2D.xClipStart - leftX;
            leftX = Rasterizer2D.xClipStart;
        }
        if (topY < Rasterizer2D.yClipStart) {
            height -= Rasterizer2D.yClipStart - topY;
            topY = Rasterizer2D.yClipStart;
        }
        if (leftX + width > xClipEnd)
            width = xClipEnd - leftX;
        if (topY + height > yClipEnd)
            height = yClipEnd - topY;
        int leftOver = Rasterizer2D.width - width;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++)
                drawAlpha(pixels, pixelIndex++, rgbColour, 255);
            pixelIndex += leftOver;
        }
    }


    public static void transparentBox(int i, int j, int k, int l, int i1, int opac) {
        int j3 = 256 - opac;
        if (k < Rasterizer2D.xClipStart) {
            i1 -= Rasterizer2D.xClipStart - k;
            k = Rasterizer2D.xClipStart;
        }

        if (j < yClipStart) {
            i -= yClipStart - j;
            j = yClipStart;
        }

        if (k + i1 > xClipEnd) {
            i1 = xClipEnd - k;
        }

        if (j + i > yClipEnd) {
            i = yClipEnd - j;
        }

        int k1 = width - i1;
        int l1 = k + j * width;

        for(int i2 = -i; i2 < 0; ++i2) {
            for(int j2 = -i1; j2 < 0; ++j2) {
                int i3 = pixels[l1];
                pixels[l1++] = ((l & 16711935) * opac + (i3 & 16711935) * j3 & -16711936) + ((l & '\uff00') * opac + (i3 & '\uff00') * j3 & 16711680) >> 8;
            }

            l1 += k1;
        }
    }

    public static void fillPixels(int i, int j, int k, int l, int i1) {
        method339(i1, l, j, i);
        method339((i1 + k) - 1, l, j, i);
        method341(i1, l, k, i);
        method341(i1, l, k, (i + j) - 1);
    }

    public static void method339(int i, int j, int k, int l) {
        if (i < yClipStart || i >= yClipEnd)
            return;
        if (l < xClipStart) {
            k -= xClipStart - l;
            l = xClipStart;
        }
        if (l + k > xClipEnd)
            k = xClipEnd - l;
        int i1 = l + i * width;
        for (int j1 = 0; j1 < k; j1++)
            drawAlpha(pixels, i1 + j1, j, 255);

    }

    public static void method341(int i, int j, int k, int l) {
        if (l < xClipStart || l >= xClipEnd)
            return;
        if (i < yClipStart) {
            k -= yClipStart - i;
            i = yClipStart;
        }
        if (i + k > yClipEnd)
            k = yClipEnd - i;
        int j1 = l + i * width;
        for (int k1 = 0; k1 < k; k1++)
            drawAlpha(pixels,j1 + k1 * width,j,255);

    }


    public static void drawBox(int x, int y, int width, int height, int border, int borderColor, int color, int alpha) {
        drawHorizontalLine(x + 1, y, width, color, alpha);
        drawHorizontalLine(x, y + height - 2, width, color, alpha);

        drawAlphaVerticalLine(x, y, height - 2, color, alpha);
        drawAlphaVerticalLine(x + width, y + 1, height - 2, color, alpha);

        for (int i = 1; i < border; i++) {
            drawHorizontalLine(x + 1, y + i, width - 1, borderColor, alpha);
            drawHorizontalLine(x + border, y + height - i - 2, width - border * 2 + 1, borderColor, alpha);

            drawAlphaVerticalLine(x + i, y + border, height - border - 2, borderColor, alpha);
            drawAlphaVerticalLine(x + width - border + i, y + border, height - border - 2, borderColor, alpha);
        }
    }

    public static void drawHorizontalLine2(int xPosition, int yPosition, int width, int rgbColour) {
        if (yPosition < yClipStart || yPosition >= yClipEnd)
            return;
        if (xPosition < xClipStart) {
            width -= xClipStart - xPosition;
            xPosition = xClipStart;
        }
        if (xPosition + width > xClipEnd)
            width = xClipEnd - xPosition;
        int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
        for (int i = 0; i < width; i++)
            drawAlpha(pixels, pixelIndex + i, rgbColour, 255);

    }
    public static void drawVerticalLine2(int xPosition, int yPosition, int height, int rgbColour) {
        if (xPosition < xClipStart || xPosition >= xClipEnd)
            return;
        if (yPosition < yClipStart) {
            height -= yClipStart - yPosition;
            yPosition = yClipStart;
        }
        if (yPosition + height > yClipEnd)
            height = yClipEnd - yPosition;
        int pixelIndex = xPosition + yPosition * width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++)
            drawAlpha(pixels, pixelIndex + rowIndex * width, rgbColour, 255);
    }

    public static void drawTransparentBox(int leftX, int topY, int width, int height, int rgbColour, int opacity) {
        if (leftX < Rasterizer2D.xClipStart) {
            width -= Rasterizer2D.xClipStart - leftX;
            leftX = Rasterizer2D.xClipStart;
        }
        if (topY < Rasterizer2D.yClipStart) {
            height -= Rasterizer2D.yClipStart - topY;
            topY = Rasterizer2D.yClipStart;
        }
        if (leftX + width > xClipEnd)
            width = xClipEnd - leftX;
        if (topY + height > yClipEnd)
            height = yClipEnd - topY;
        int transparency = 256 - opacity;
        int red = (rgbColour >> 16 & 0xff) * opacity;
        int green = (rgbColour >> 8 & 0xff) * opacity;
        int blue = (rgbColour & 0xff) * opacity;
        int leftOver = Rasterizer2D.width - width;
        int pixelIndex = leftX + topY * Rasterizer2D.width;
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            for (int columnIndex = 0; columnIndex < width; columnIndex++) {
                int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
                int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
                int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
                int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
                drawAlpha(pixels, pixelIndex++, transparentColour, opacity);
            }
            pixelIndex += leftOver;
        }
    }

    public static void filterGrayscale(int x, int y, int width, int height, double amount) {
        if (amount <= 0) {
            return;
        }


        if(x < Rasterizer2D.xClipStart) {
            x = Rasterizer2D.xClipStart;
        }
        if(y < Rasterizer2D.yClipStart) {
            y = Rasterizer2D.yClipStart;
        }
        if(x + width > xClipEnd)
            width = xClipEnd - x;
        if(y + height > yClipEnd)
            height = yClipEnd - y;


        int pos = x + y * Rasterizer2D.width;
        int offset = Rasterizer2D.width - width;
        if (amount >= 1) {
            while (height-- > 0) {
                for (int i = 0; i < width; i++) {
                    int red = pixels[pos] >> 16 & 0xff;
                    int green = pixels[pos] >> 8 & 0xff;
                    int blue = pixels[pos] & 0xff;
                    int lightness = (red + green + blue) / 3;
                    int color = lightness << 16 | lightness << 8 | lightness;
                    drawAlpha(pixels, pos++, color, 255);
                }
                pos += offset;
            }
        } else {
            double divider = 2 * amount + 1;
            while (height-- > 0) {
                for (int i = 0; i < width; i++) {
                    int red = pixels[pos] >> 16 & 0xff;
                    int green = pixels[pos] >> 8 & 0xff;
                    int blue = pixels[pos] & 0xff;
                    int red2 = (int) (red * amount);
                    int green2 = (int) (green * amount);
                    int blue2 = (int) (blue * amount);
                    red = (int) ((red + green2 + blue2) / divider);
                    green = (int) ((red2 + green + blue2) / divider);
                    blue = (int) ((red2 + green2 + blue) / divider);
                    int color = red << 16 | green << 8 | blue;
                    drawAlpha(pixels, pos++, color, 255);
                }
                pos += offset;
            }
        }
    }

    public static void drawBoxOutline(int leftX, int topY, int width, int height, int rgbColour) {
        drawHorizontalLine2(leftX, topY, width, rgbColour);
        drawHorizontalLine2(leftX, (topY + height) - 1, width, rgbColour);
        drawVerticalLine2(leftX, topY, height, rgbColour);
        drawVerticalLine2((leftX + width) - 1, topY, height, rgbColour);
    }


    public void drawAlphaGradientOnSprite(Sprite sprite, int x, int y, int gradientWidth,
                                          int gradientHeight, int startColor, int endColor, int alpha) {
        int k1 = 0;
        int l1 = 0x10000 / gradientHeight;
        if (x < xClipStart) {
            gradientWidth -= xClipStart - x;
            x = xClipStart;
        }
        if (y < yClipStart) {
            k1 += (yClipStart - y) * l1;
            gradientHeight -= yClipStart - y;
            y = yClipStart;
        }
        if (x + gradientWidth > xClipEnd)
            gradientWidth = xClipEnd - x;
        if (y + gradientHeight > yClipEnd)
            gradientHeight = yClipEnd - y;
        int i2 = width - gradientWidth;
        int result_alpha = 256 - alpha;
        int total_pixels = x + y * width;
        for (int k2 = -gradientHeight; k2 < 0; k2++) {
            int gradient1 = 0x10000 - k1 >> 8;
            int gradient2 = k1 >> 8;
            int gradient_color = ((startColor & 0xff00ff) * gradient1
                    + (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
                    + ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
                    * gradient2 & 0xff0000) >>> 8;
            int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
                    + ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
            for (int k3 = -gradientWidth; k3 < 0; k3++) {
                int colored_pixel = pixels[total_pixels];
                colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
                        + ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
                drawAlpha(pixels, total_pixels++, colored_pixel, alpha);
            }
            total_pixels += i2;
            k1 += l1;
        }
    }

    public static void method338(int i, int j, int k, int l, int i1, int j1) {
        method340(l, i1, i, k, j1);
        method340(l, i1, (i + j) - 1, k, j1);
        if (j >= 3) {
            method342(l, j1, k, i + 1, j - 2);
            method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
        }
    }

    private static void method340(int i, int j, int k, int l, int i1) {
        if (k < yClipStart || k >= yClipEnd)
            return;
        if (i1 < xClipStart) {
            j -= xClipStart - i1;
            i1 = xClipStart;
        }
        if (i1 + j > xClipEnd)
            j = xClipEnd - i1;
        int j1 = 256 - l;
        int k1 = (i >> 16 & 0xff) * l;
        int l1 = (i >> 8 & 0xff) * l;
        int i2 = (i & 0xff) * l;
        int i3 = i1 + k * width;
        for (int j3 = 0; j3 < j; j3++) {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            drawAlpha(pixels, i3++, k3, 255);
        }

    }

    private static void method342(int i, int j, int k, int l, int i1) {
        if (j < xClipStart || j >= xClipEnd)
            return;
        if (l < yClipStart) {
            i1 -= yClipStart - l;
            l = yClipStart;
        }
        if (l + i1 > yClipEnd)
            i1 = yClipEnd - l;
        int j1 = 256 - k;
        int k1 = (i >> 16 & 0xff) * k;
        int l1 = (i >> 8 & 0xff) * k;
        int i2 = (i & 0xff) * k;
        int i3 = j + l * width;
        for (int j3 = 0; j3 < i1; j3++) {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            drawAlpha(pixels, i3, k3, 255);
            i3 += width;
        }
    }

    public static void renderGlow(int drawX, int drawY, int glowColor, int r) {
        // center
        drawX += r / 2;
        drawY += r / 2;

        int startX = drawX - r;
        int endX = drawX + r;
        int startY = drawY - r;
        int endY = drawY + r;

        // clipping
        if (startX < xClipStart) {
            startX = xClipStart;
        }

        if (endX > xClipEnd) {
            endX = xClipEnd;
        }

        if (startY < yClipStart) {
            startY = yClipStart;
        }

        if (endY > yClipEnd) {
            endY = yClipEnd;
        }

        float edge0 = -(r / 2f);
        float edge1 = MathUtils.map((float) Math.sin(Client.loopCycle / 20f), -1, 1, edge0 + (r / 1.35f), r);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) { // what did i have to get working again, texture animation? Yeah uhh, new boxes textures 96>100 not aniamting
                int index = x + y * width;
                float d = MathUtils.dist(x, y, drawX, drawY);
                float dist = MathUtils.smoothstep(edge0, edge1, d);
                int oldColor = pixels[index];
                int newColor = blend(oldColor, glowColor, 1f - dist);
                drawAlpha(pixels, index, newColor, r);
            }
        }
    }

    public static int blend(int rgb1, int rgb2, float factor) {
        if (factor >= 1f) {
            return rgb2;
        }
        if (factor <= 0f) {
            return rgb1;
        }

        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = (rgb1) & 0xff;

        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = (rgb2) & 0xff;

        int r3 = r2 - r1;
        int g3 = g2 - g1;
        int b3 = b2 - b1;

        int r = (int) (r1 + (r3 * factor));
        int g = (int) (g1 + (g3 * factor));
        int b = (int) (b1 + (b3 * factor));

        return (r << 16) + (g << 8) + b;
    }

    public static void drawBorder(int x, int y, int width, int height, int color) {
        Rasterizer2D.drawPixels(1, y, x, color, width);
        Rasterizer2D.drawPixels(height, y, x, color, 1);
        Rasterizer2D.drawPixels(1, y + height, x, color, width + 1);
        Rasterizer2D.drawPixels(height, y, x + width, color, 1);
    }


    public static void fillCircle(int x, int y, int radius, int color) {
        int y1 = y - radius;
        if (y1 < 0) {
            y1 = 0;
        }
        int y2 = y + radius;
        if (y2 >= height) {
            y2 = height - 1;
        }
        for (int iy = y1; iy <= y2; iy++) {
            int dy = iy - y;
            int dist = (int) Math.sqrt(radius * radius - dy * dy);
            int x1 = x - dist;
            if (x1 < 0) {
                x1 = 0;
            }
            int x2 = x + dist;
            if (x2 >= width) {
                x2 = width - 1;
            }
            int pos = x1 + iy * width;
            for (int ix = x1; ix <= x2; ix++) {
                drawAlpha(pixels, pos++, color, 255);
            }
        }
    }
    public static void drawPixelsWithOpacity2(int xPos, int yPos, int pixelWidth, int pixelHeight, int color, int opacityLevel) {
        drawPixelsWithOpacity(color, yPos, pixelWidth, pixelHeight, opacityLevel, xPos);
    }

    public static void drawPixelsWithOpacity(int color, int yPos, int pixelWidth, int pixelHeight, int opacityLevel,
                                             int xPos) {
        if (xPos < xClipStart) {
            pixelWidth -= xClipStart - xPos;
            xPos = xClipStart;
        }
        if (yPos < yClipStart) {
            pixelHeight -= yClipStart - yPos;
            yPos = yClipStart;
        }
        if (xPos + pixelWidth > xClipEnd) {
            pixelWidth = xClipEnd - xPos;
        }
        if (yPos + pixelHeight > yClipEnd) {
            pixelHeight = yClipEnd - yPos;
        }
        int l1 = 256 - opacityLevel;
        int i2 = (color >> 16 & 0xFF) * opacityLevel;
        int j2 = (color >> 8 & 0xFF) * opacityLevel;
        int k2 = (color & 0xFF) * opacityLevel;
        int k3 = width - pixelWidth;
        int l3 = xPos + yPos * width;
        if (l3 > pixels.length - 1) {
            l3 = pixels.length - 1;
        }
        for (int i4 = 0; i4 < pixelHeight; i4++) {
            for (int j4 = -pixelWidth; j4 < 0; j4++) {
                int l2 = (pixels[l3] >> 16 & 0xFF) * l1;
                int i3 = (pixels[l3] >> 8 & 0xFF) * l1;
                int j3 = (pixels[l3] & 0xFF) * l1;
                int k4 = (i2 + l2 >> 8 << 16) + (j2 + i3 >> 8 << 8) + (k2 + j3 >> 8);
                drawAlpha(pixels, l3++, k4, opacityLevel);
            }
            l3 += k3;
        }
    }


}
