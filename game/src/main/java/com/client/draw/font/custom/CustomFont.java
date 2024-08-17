package com.client.draw.font.custom;

import com.client.Client;
import com.client.Rasterizer2D;
import com.client.engine.impl.MouseHandler;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.zip.*;

import static com.client.Rasterizer2D.drawAlpha;


public final class CustomFont {

    public CustomFont(Component context, String name, int style, int size, boolean smooth) {
        this(context, new Font(name, style, size), smooth);
    }




    public CustomFont(Component context, Font font, boolean smooth) {
        if (context == null || font == null)
            throw new IllegalArgumentException();

        this.font = font;
        fontMetrics = context.getFontMetrics(font);
        if (fontMetrics == null)
            throw new RuntimeException();

        ascent = fontMetrics.getAscent();
        Rectangle bounds = font.getMaxCharBounds(fontMetrics.getFontRenderContext()).getBounds();
        bufferWidth = bounds.width;
        bufferHeight = bounds.height;
        if (bufferWidth < 1 || bufferHeight < 1)
            throw new RuntimeException();

        bufferSize = bufferWidth * bufferHeight;
        bufferPixels = new int[bufferSize];
        buffer = wrapPixels(bufferPixels, bufferWidth, bufferHeight);
        bufferGraphics = buffer.createGraphics();
        if (bufferGraphics == null)
            throw new RuntimeException();

        bufferGraphics.setFont(font);
        if (smooth) {
            bufferGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,smooth ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        bufferGraphics.setColor(new Color(FOREGROUND, true));
        charCache = new AdvancedFontChar[256][];
    }

    public void drawString1(String str, int x, int y, int color, boolean validAlpha, boolean shaded) {
        if (shaded) {
            drawString(str, x - (stringWidth(str) / 2) - 1, y + 1, 0x000000, validAlpha);
            drawString(str, x - (stringWidth(str) / 2) + 1, y + 1, 0x000000, validAlpha);
            drawString(str, x - (stringWidth(str) / 2) + 1, y - 1, 0x000000, validAlpha);
            drawString(str, x - (stringWidth(str) / 2) - 1, y - 1, 0x000000, validAlpha);
        }
        drawString(str, x, y, color, validAlpha);
    }

    public Font getFont() {
        return this.font;
    }

    public CustomFont(URL url) throws IOException {
        this(url.openStream());
    }

    public CustomFont(String file) throws IOException {
        this(new File(file));
    }

    public CustomFont(File file) throws IOException {
        this.font = null;
        FileInputStream in = new FileInputStream(file);
        try {
            load(in);
        } finally {
            in.close();
        }
    }

    public CustomFont(byte[] data) throws IOException {
        this(new ByteArrayInputStream(data, 0, data.length));
    }

    public CustomFont(byte[] data, int offset, int length) throws IOException {
        this(new ByteArrayInputStream(data, offset, length));
    }

    public CustomFont(InputStream in) throws IOException {
        this.font = null;
        load(in);
    }

    private void load(InputStream in0) throws IOException {
        DataInputStream in = new DataInputStream(new GZIPInputStream(in0));
        int bufferWidth = readInt(in);
        int bufferHeight = readInt(in);
        int ascent = readInt(in);
        if (bufferWidth == 0 || bufferHeight == 0)
            bufferWidth = bufferHeight = 0;

        this.bufferWidth = bufferWidth;
        this.bufferHeight = bufferHeight;
        this.ascent = ascent;
        charCache = new AdvancedFontChar[256][];
        for (int i = 0; i != 0x10000; ++i)
            provideFontChar((char) i, new AdvancedFontChar(in));

    }

    public void writeTo(OutputStream out0) throws IOException {
        GZIPOutputStream gzipOut = new GZIPOutputStream(out0);
        DataOutputStream out = new DataOutputStream(gzipOut);
        writeInt(out, bufferWidth);
        writeInt(out, bufferHeight);
        writeInt(out, ascent);
        for (int i = 0; i != 0x10000; ++i) {
            AdvancedFontChar chr = getFontChar((char) i, false);
            if (chr == null) {
                out.writeByte(0);
                continue;
            }
            chr.writeTo(out);
        }

        gzipOut.finish();
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(out);
        return out.toByteArray();
    }

    public void writeTo(String file) throws IOException {
        writeTo(new File(file));
    }

    public void writeTo(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        try {
            writeTo(out);
        } finally {
            out.close();
        }
    }

    public void drawHoverString(String str, int x, int y, int color, int hoverColor, boolean center, boolean shaded,
                                boolean underline) {
        int colorToUse = color;
        if (Client.instance.newmouseInRegion(x,y,center ? stringWidth(str) / 2 : 0,stringHeight(str))) {
            colorToUse = hoverColor;
            if (underline) {
                Rasterizer2D.drawHorizontalLine(x - 1 - (center ? stringWidth(str) / 2 : 0) + 1, y + 2 + 1, stringWidth(str) + 2, 0);
                Rasterizer2D.drawHorizontalLine(x - 1 - (center ? stringWidth(str) / 2 : 0), y + 2, stringWidth(str) + 2, hoverColor);
            }
        }
        if (center) {
            drawStringCenter(str, false, x, y, colorToUse, shaded);
        } else {
            drawString(str, x, y, colorToUse, false, shaded);
        }
    }

    public void drawStringCenter(String str, boolean underline, int x, int y, int color, boolean shaded) {
        drawStringCenter(str, x, y, color, false, shaded);
        if (underline) {
            Rasterizer2D.drawHorizontalLine(x - 1 - stringWidth(str) / 2 + 1, y + 2 + 1, stringWidth(str) + 2, 0);
            Rasterizer2D.drawHorizontalLine(x - 1 - stringWidth(str) / 2, y + 2, stringWidth(str) + 2, color);
        }
    }

    public void drawStringCenter(String str, int x, int y, int color, boolean shaded) {
        drawStringCenter(str, x, y, color, false, shaded);
    }

    public void drawStringCenter(String str, int x, int y, int color, boolean validAlpha, boolean shaded) {
        if (shaded) {
            drawString(str, x - (stringWidth(str) / 2) + 1, y + 1, 0x000000, validAlpha);
        }
        drawString(str, x - stringWidth(str) / 2, y, color, validAlpha);
    }

    public void drawStringCenter(String str, int x, int y, int color,int color1, boolean validAlpha, boolean shaded) {
        if (shaded) {
            drawString(str, x - (stringWidth(str) / 2) + 1, y + 1, 0x000000, validAlpha);
        }
        if(Client.instance.newmouseInRegion(x - (stringWidth(str) / 2) - 1, y - 10, stringWidth(str), 10)) {
            drawString(str, x - stringWidth(str) / 2, y, color1, validAlpha);
        } else {
            drawString(str, x - stringWidth(str) / 2, y, color, validAlpha);
        }

    }

    public void drawStringCenter(String str, int x, int y, int color,int color1, boolean validAlpha, boolean shaded, String url) {
        if (shaded) {
            drawString(str, x - (stringWidth(str) / 2) + 1, y + 1, 0x000000, validAlpha);
        }
        if(Client.instance.newmouseInRegion(x - (stringWidth(str) / 2) - 1, y - 10, stringWidth(str), 10)) {
            drawString(str, x - stringWidth(str) / 2, y, color1, validAlpha);
        } else {
            drawString(str, x - stringWidth(str) / 2, y, color, validAlpha);
        }
        if(MouseHandler.clickMode3 == 1 && Client.instance.newclickInRegion(x - (stringWidth(str) / 2) - 1, y - 10, stringWidth(str), 10)) {
            //Client.instance.openURL(url);
        }

    }


    public void drawStringCenter(String str, int x, int y, int color,int color1, boolean validAlpha, boolean shaded, String url,int fade) {

        if(Client.instance.newmouseInRegion(x - (stringWidth(str) / 2) - 1, y - 10, stringWidth(str), 10)) {
            drawString(str, x - stringWidth(str) / 2, y, color1,fade);
        } else {
            drawString(str, x - stringWidth(str) / 2, y, color,fade);
        }
        if(MouseHandler.clickMode3 == 1  && Client.instance.newclickInRegion(x - (stringWidth(str) / 2) - 1, y - 10, stringWidth(str), 10)) {
            //Client.instance.openURL(url);
        }

    }

    public void drawStringLeft(String str, int x, int y, int color, boolean shaded) {
        drawStringLeft(str, x, y, color, false, shaded);
    }

    public void drawStringRight(String str, int x, int y, int color, boolean shaded) {
        drawStringRight(str, x, y, color, false, shaded);
    }

    public void drawStringRight(String str, int x, int y, int color, boolean validAlpha, boolean shaded) {
        if (shaded) {
            drawString(str, x + 1, y + 1, 0x000000, validAlpha);
        }
        drawString(str, x, y, color, validAlpha);
    }

    public void drawStringLeft(String str, int x, int y, int color, boolean validAlpha, boolean shaded) {
        if (shaded) {
            drawString(str, x - stringWidth(str) + 1, y + 1, 0x000000, validAlpha);
        }
        drawString(str, x - stringWidth(str), y, color, validAlpha);
    }

    public void drawString(String str, int x, int y, int color, boolean validAlpha, boolean shaded) {
        if (shaded) {
            drawString(str, x + 1, y + 1, 0x000000, false);
        }
        drawString(str, x, y, color, false);
    }

    public void drawString(String str, int x, int y, int normalColor, boolean validAlpha, boolean shaded, int colorHover) {
        int color;
        if (Client.instance.newmouseInRegion(x,y - stringHeight(str),stringWidth(str),stringHeight(str))) {
            color = colorHover;
        } else {
            color = normalColor;
        }
        if (shaded) {
            drawString(str, x + 1, y + 1, 0x000000, false);
        }
        drawString(str, x, y, color, false);
    }

    public int drawString(String str, int x, int y, int color,boolean validalpha, int alpha) {
        if (str == null)
            return 0;

        if (alpha > 255)
            alpha = 255;

        color = (alpha << 24) + 0x00FFFFFF;

        int width = 0;
        for (int i = 0; i != str.length(); ++i) {
            width += drawChar(str.charAt(i), width + x, y, color, validalpha);
        }

        return width;
    }

    public int drawString(String str, int x, int y, int color, int alpha) {
        if (str == null)
            return 0;

        if (alpha > 255)
            alpha = 255;

        color = (alpha << 24) + 0x00FFFFFF;

        int width = 0;
        for (int i = 0; i != str.length(); ++i) {
            width += drawChar(str.charAt(i), width + x, y, color, true);
        }

        return width;
    }




    public int drawString(String str, int x, int y, int color, boolean validAlpha) {
        if (str == null)
            return 0;

        if (!validAlpha)
            color |= 0xff000000;

        int width = 0;
        for (int i = 0; i != str.length(); i++) {
            width += drawChar(str.charAt(i), width + x, y, color, true);
        }

        return width;
    }

    public int drawChar(char chr, int x, int y, int color) {
        return drawChar(chr, x, y, color, false);
    }

    public int drawChar(char chr, int x, int y, int color, boolean validAlpha) {
        AdvancedFontChar fontChar = getFontChar(chr);
        drawPixels(fontChar.pixels, fontChar.width, fontChar.height, fontChar.xOffset + x,
                fontChar.yOffset + y - ascent, validAlpha ? color : color | 0xff000000);
        return fontChar.widthOffset;
    }

    public int stringWidth(String str) {
        if (str == null)
            return 0;

        int width = 0;
        int length = str.length();
        for (int i = 0; i != length; ++i)
            width += charWidth(str.charAt(i));

        return width;
    }

    public int stringHeight(String str) {
        if (str == null)
            return 0;

        int height = 0;
        int length = str.length();
        for (int i = 0; i != length; ++i) {
            int h = charHeight(str.charAt(i));
            if (height < h)
                height = h;

        }

        return height;
    }

    public int stringX(String str) {
        if (str == null || str.length() == 0)
            return 0;

        return charX(str.charAt(0));
    }

    public int stringY(String str) {
        if (str == null || str.length() == 0)
            return 0;

        int y = 0;
        int length = str.length();
        for (int i = 0; i != length; ++i) {
            int y0 = charY(str.charAt(i));
            if (y > y0)
                y = y0;

        }

        return y;
    }

    public Rectangle stringBounds(String str) {
        return new Rectangle(stringX(str), stringY(str), stringWidth(str), stringHeight(str));
    }

    public int charX(char chr) {
        return getFontChar(chr).xOffset;
    }

    public int charY(char chr) {
        return ascent + getFontChar(chr).yOffset;
    }

    public int charWidth(char chr) {
        return getFontChar(chr).widthOffset;
    }

    public int charHeight(char chr) {
        return getFontChar(chr).heightOffset;
    }

    public Rectangle charBounds(char chr) {
        return getFontChar(chr).getBounds();
    }

    public int getMaxCharWidth() {
        return bufferWidth;
    }

    public int getMaxCharHeight() {
        return bufferHeight;
    }

    public int getDescent() {
        return ascent;
    }

    public void provideFontChar(char chr, AdvancedFontChar fontChar) {
        if (disposed)
            return;

        if (fontChar == null)
            fontChar = DUMMY_FONT_CHAR;

        int sector = chr >>> 8;
        int index = chr & 0xff;
        AdvancedFontChar[][] charCache = this.charCache;
        AdvancedFontChar[] cacheSector = charCache[sector];
        if (cacheSector != null) {
            cacheSector[index] = fontChar;
            return;
        }
        cacheSector = charCache[sector] = new AdvancedFontChar[256];
        cacheSector[index] = fontChar;
    }

    public AdvancedFontChar getFontChar(char chr) {
        return getFontChar(chr, true);
    }

    public AdvancedFontChar getFontChar(char chr, boolean giveDummy) {
        if (disposed)
            return giveDummy ? DUMMY_FONT_CHAR : null;

        int sector = chr >>> 8;
        int index = chr & 0xff;
        AdvancedFontChar[][] charCache = this.charCache;
        AdvancedFontChar[] cacheSector = charCache[sector];
        if (cacheSector != null) {
            AdvancedFontChar fontChar = cacheSector[index];
            if (fontChar != null)
                return fontChar;

        }
        AdvancedFontChar fontChar = getFontChar0(chr);
        if (fontChar == null) {
            if (!giveDummy)
                return null;

            fontChar = DUMMY_FONT_CHAR;
        }
        if (cacheSector == null)
            cacheSector = charCache[sector] = new AdvancedFontChar[256];

        cacheSector[index] = fontChar;
        return fontChar;
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void dispose() {
        if (disposed)
            return;

        disposed = true;
        charCache = null;
        bufferPixels = null;
        fontMetrics = null;
        Graphics2D gfx = bufferGraphics;
        if (gfx != null) {
            bufferGraphics = null;
            try {
                gfx.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        BufferedImage img = buffer;
        if (img != null) {
            buffer = null;
            try {
                img.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static BufferedImage wrapPixels(int[] pixels, int width, int height) {
        DirectColorModel model = new DirectColorModel(32, 0xff0000, 0xff00, 0xff, 0xff000000);
        return new BufferedImage(model, Raster.createWritableRaster(model.createCompatibleSampleModel(width, height),
                new DataBufferInt(pixels, width * height), null), false, new Hashtable<Object, Object>());
    }

    private static int bind(int a, int b) {
        return (a * (b + 1)) >>> 8;
    }

    private void drawPixels(final byte[] pixels, final int width, final int height, int xPos, int yPos,
                            final int color) {
        if (width < 1 || height < 1)
            return;

        int x = 0;
        int y = 0;
        int w = width;
        int h = height;
        int tmp = Rasterizer2D.xClipStart;
        if (xPos < tmp) {
            x += tmp - xPos;
            w -= tmp - xPos;
            xPos = tmp;
        }
        tmp = Rasterizer2D.yClipStart;
        if (yPos < tmp) {
            y += tmp - yPos;
            h -= tmp - yPos;
            yPos = tmp;
        }
        tmp = Rasterizer2D.xClipEnd;
        if (xPos + w > tmp)
            w = tmp - xPos;

        tmp = Rasterizer2D.yClipEnd;
        if (yPos + h > tmp)
            h = tmp - yPos;

        if (w < 1 || h < 1)
            return;

        tmp = Rasterizer2D.width;
        int localIndex = x + y * width;
        int globalIndex = xPos + yPos * tmp;
        int[] dst = Rasterizer2D.pixels;
        if (globalIndex > dst.length - 1) {
            globalIndex = dst.length - 1;
        }
        for (int i = 0; i != h; ++i) {
            for (int i1 = 0; i1 != w; ++i1)
                drawAlpha(dst,globalIndex + i1,blend(dst[globalIndex + i1], color, bind(pixels[localIndex + i1] & 0xff, color >>> 24)) & 0xffffff,255);

            localIndex += width;
            globalIndex += tmp;
        }

    }



    private AdvancedFontChar getFontChar0(char chr) {
        if (!font.canDisplay(chr))
            return null;

        int bufferSize = this.bufferSize;
        int bufferWidth = this.bufferWidth;
        int bufferHeight = this.bufferHeight;
        int[] bufferPixels = this.bufferPixels;
        for (int i = 0; i != bufferSize; ++i)
            bufferPixels[i] = BACKGROUND;

        bufferGraphics.drawString(String.valueOf(chr), 0, fontMetrics.getMaxAscent());
        int startX = bufferWidth;
        int startY = 0;
        int endX = 0;
        int endY = 0;
        int offset = 0;
        for (int i = 0; i != bufferHeight; ++i) {
            int startX0 = bufferWidth;
            for (int i1 = 0; i1 != startX0; ++i1)
                if ((bufferPixels[offset + i1] & 0xff000000) != 0) {
                    startX0 = i1;
                    break;
                }

            offset += bufferWidth;
            if (startX0 == bufferWidth) {
                if (startY == i)
                    startY = 1 + i;

                continue;
            }
            endY = 1 + i;
            int endX0 = bufferWidth - startX0 - 1;
            if (endX0 < endX)
                endX0 = endX;

            for (int i1 = 0; i1 != endX0; ++i1)
                if ((bufferPixels[offset - i1 - 1] & 0xff000000) != 0) {
                    endX0 = bufferWidth - i1;
                    break;
                }

            if (startX > startX0)
                startX = startX0;

            if (endX < endX0)
                endX = endX0;

        }

        if (startX < 0) {
            endX += startX;
            startX = 0;
        }
        if (startY < 0) {
            endY += startY;
            startY = 0;
        }
        if (startX > endX)
            endX = startX;

        if (startY > endY)
            endY = startY;

        int x = startX;
        int y = startY;
        int width = endX - x;
        int height = endY - y;
        byte[] fontPixels = new byte[width * height];
        int localIndex = 0;
        int globalIndex = x + y * bufferWidth;
        for (int i = 0; i != height; ++i) {
            for (int i1 = 0; i1 != width; ++i1)
                fontPixels[localIndex + i1] = (byte) (bufferPixels[globalIndex + i1] >>> 24);

            localIndex += width;
            globalIndex += bufferWidth;
        }

        return new AdvancedFontChar(fontPixels, width, height, x, y, fontMetrics.charWidth(chr), bufferHeight);
    }

    private static int blend(int dst, int src, int alpha) {
        if (alpha == 0)
            return dst;

        if (alpha == 255)
            return src;

        int delta = 255 - alpha;
        return (src & 0xff000000 | ((src & 0xff00ff) * alpha + (dst & 0xff00ff) * delta & 0xff00ff00
                | (src & 0xff00) * alpha + (dst & 0xff00) * delta & 0xff0000) >>> 8);
    }



    protected void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    private static int readInt(DataInputStream in) throws IOException {
        int b = in.readByte() & 0xff;
        if ((b & 0x80) == 0)
            return b;

        return b | ((in.readByte() & 0xff) << 7) | ((in.readByte() & 0xff) << 15) | ((in.readByte() & 0xff) << 23);
    }

    private static void writeInt(DataOutputStream out, int val) throws IOException {
        if ((val & 0x7f) == val) {
            out.writeByte((byte) val);
            return;
        }
        out.writeByte((byte) (val | 0x80));
        out.writeByte((byte) (val >>> 7));
        out.writeByte((byte) (val >>> 15));
        out.writeByte((byte) (val >>> 23));
    }

    public static class AdvancedFontChar {

        public AdvancedFontChar(byte[] pixels, int width, int height, int xOffset, int yOffset, int widthOffset,
                                int heightOffset) {
            if (width < 0 || height < 0 || pixels == null || width * height > pixels.length)
                width = height = 0;

            if (pixels == null)
                pixels = new byte[0];

            if (xOffset < 0)
                xOffset = 0;

            if (yOffset < 0)
                yOffset = 0;

            if (widthOffset < 0)
                widthOffset = 0;

            if (heightOffset < 0)
                heightOffset = 0;

            this.pixels = pixels;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.widthOffset = widthOffset;
            this.heightOffset = heightOffset;
        }

        public AdvancedFontChar(InputStream in0) throws IOException {
            DataInputStream in = in0 instanceof DataInputStream ? (DataInputStream) in0 : new DataInputStream(in0);
            int flags = in.readByte() & 0xff;
            int width;
            int height;
            if ((flags & 0x1) != 0) {
                width = readInt(in);
                height = readInt(in);
                if (width == 0 || height == 0)
                    width = height = 0;

            } else
                width = height = 0;

            int count = width * height;
            byte[] pixels = new byte[count];
            for (int i = 0; i != count; ++i)
                pixels[i] = in.readByte();

            int xOffset = (flags & 0x3) == 0x3 ? readInt(in) : 0;
            int yOffset = (flags & 0x5) == 0x5 ? readInt(in) : 0;
            int widthOffset = (flags & 0x8) != 0 ? readInt(in) : 0;
            int heightOffset = (flags & 0x10) != 0 ? readInt(in) : 0;
            this.width = width;
            this.height = height;
            this.pixels = pixels;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.widthOffset = widthOffset;
            this.heightOffset = heightOffset;
        }

        public void writeTo(OutputStream out0) throws IOException {
            DataOutputStream out = out0 instanceof DataOutputStream ? (DataOutputStream) out0
                    : new DataOutputStream(out0);
            int flags = 0;
            int width = this.width;
            int height = this.height;
            int xOffset = this.xOffset;
            int yOffset = this.yOffset;
            int widthOffset = this.widthOffset;
            int heightOffset = this.heightOffset;
            if (width > 0 && height > 0) {
                flags |= 0x1;
                if (xOffset != 0)
                    flags |= 0x2;

                if (yOffset != 0)
                    flags |= 0x4;

            }
            if (widthOffset != 0)
                flags |= 0x8;

            if (heightOffset != 0)
                flags |= 0x10;

            out.writeByte(flags);
            if ((flags & 0x1) != 0) {
                byte[] pixels = this.pixels;
                writeInt(out, width);
                writeInt(out, height);
                int count = width * height;
                for (int i = 0; i != count; ++i)
                    out.writeByte(pixels[i]);

            }
            if ((flags & 0x3) == 0x3)
                writeInt(out, xOffset);

            if ((flags & 0x5) == 0x5)
                writeInt(out, yOffset);

            if ((flags & 0x8) != 0)
                writeInt(out, widthOffset);

            if ((flags & 0x10) != 0)
                writeInt(out, heightOffset);

        }

        public Rectangle getBounds() {
            return new Rectangle(xOffset, yOffset, widthOffset, heightOffset);
        }

        public final byte[] pixels;
        public final int width;
        public final int height;
        public final int xOffset;
        public final int yOffset;
        public final int widthOffset;
        public final int heightOffset;
    }

    private static final AdvancedFontChar DUMMY_FONT_CHAR = new AdvancedFontChar(new byte[0], 0, 0, 0, 0, 0, 0);
    private static final int FOREGROUND = 0xffffffff;
    private static final int BACKGROUND = 0x00000000;
    private boolean disposed;
    private AdvancedFontChar[][] charCache;
    private int bufferWidth;
    private int bufferHeight;
    private int[] bufferPixels;
    private int bufferSize;
    private int ascent;
    private Graphics2D bufferGraphics;
    private BufferedImage buffer;
    private FontMetrics fontMetrics;
    public final Font font;

    public static int getColorByName(String s) {
        if (s.equals("369"))
            return 0x336699;
        if (s.equals("mon"))
            return 0x00ff80;
        if (s.equals("red"))
            return 0xff0000;
        if (s.equals("gre"))
            return 65280;
        if (s.equals("blu"))
            return 255;
        if (s.equals("yel"))
            return 0xffff00;
        if (s.equals("cya"))
            return 65535;
        if (s.equals("mag"))
            return 0xff00ff;
        if (s.equals("whi"))
            return 0xffffff;
        if (s.equals("bla"))
            return 0;
        if (s.equals("lre"))
            return 0xff9040;
        if (s.equals("dre"))
            return 0x800000;
        if (s.equals("dbl"))
            return 128;
        if (s.equals("or0"))
            return 0xFF9040;
        if (s.equals("or1"))
            return 0xffb000;
        if (s.equals("or2"))
            return 0xff7000;
        if (s.equals("or3"))
            return 0xff3000;
        if (s.equals("gr1"))
            return 0xc0ff00;
        if (s.equals("gr2"))
            return 0x80ff00;
        if (s.equals("gr3"))
            return 0x40ff00;
        /** Loyalty Colors **/
        if (s.equals("bas"))
            return 0xC86400;
        if (s.equals("mbl"))
            return 0xAF64A0;
        if (s.equals("vth"))
            return 0xC12006;
        return -1;
    }
}