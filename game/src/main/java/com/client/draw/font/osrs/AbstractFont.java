package com.client.draw.font.osrs;


import com.client.Bounds;
import com.client.IndexedImage;
import com.client.Rasterizer2D;
import com.client.Sprite;
import com.client.draw.ImageCache;

import java.awt.*;
import java.util.Random;

public abstract class AbstractFont extends Rasterizer2D {

    public static IndexedImage[] AbstractFont_modIconSprites;

    static int strike = -1;

    static int underline = -1;

    static int previousShadow = -1;

    static int shadow = -1;

    static int previousColor = 0;

    static int color = 0;

    static int alpha = 256;

    static int justificationTotal = 0;

    static int justificationCurrent = 0;

    static Random AbstractFont_random = new Random();

    static String[] AbstractFont_lines = new String[100];

    byte[][] pixels = new byte[256][];

    int[] advances;

    int[] widths;

    int[] heights;

    int[] leftBearings;

    int[] topBearings;

    public int ascent = 0;

    public int maxAscent;

    public int maxDescent;

    byte[] kerning;

    AbstractFont(byte[] fontData, int[] leftBearings, int[] topBearings, int[] widths, int[] heights, int[] var6, byte[][] pixels) {
        this.leftBearings = leftBearings;
        this.topBearings = topBearings;
        this.widths = widths;
        this.heights = heights;
        this.readMetrics(fontData);
        this.pixels = pixels;
        int minTopBearing = Integer.MAX_VALUE;
        int maxBottomBearing = Integer.MIN_VALUE;

        for(int index = 0; index < 256; ++index) {
            if (this.topBearings[index] < minTopBearing && this.heights[index] != 0) {
                minTopBearing = this.topBearings[index];
            }

            if (this.topBearings[index] + this.heights[index] > maxBottomBearing) {
                maxBottomBearing = this.topBearings[index] + this.heights[index];
            }
        }

        this.maxAscent = this.ascent - minTopBearing;
        this.maxDescent = maxBottomBearing - this.ascent;
    }

    AbstractFont(byte[] var1) {
        this.readMetrics(var1);
    }


    abstract void drawGlyph(byte[] var1, int var2, int var3, int var4, int var5, int var6);


    abstract void drawGlyphAlpha(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);


    void readMetrics(byte[] data) {
        advances = new int[256];
        int length = data.length;
        int index = 0;
        if (length == 257) {
            for (int i = 0; i < advances.length; i++) {
                advances[i] = data[i] & 0xff;
            }
            ascent = data[256] & 0xff;
        } else {
            for (int i = 0; i < 256; i++) {
                advances[i] = data[index++] & 0xff;
            }
            int[] charWidth = new int[256];
            int[] charHeights = new int[256];
            for (int i = 0; i < 256; i++) {
                charWidth[i] = data[index++] & 0xff;
            }
            for (int i = 0; i < 256; i++) {
                charHeights[i] = data[index++] & 0xff;
            }
            byte[][] charBitmapWidth = new byte[256][];
            for (int i = 0; i < 256; i++) {
                charBitmapWidth[i] = new byte[charWidth[i]];
                int sum = 0;
                for (int j = 0; j < charBitmapWidth[i].length; j++) {
                    sum += data[index++];
                    charBitmapWidth[i][j] = (byte) sum;
                }
            }
            byte[][] charBitmapHeights = new byte[256][];
            for (int i = 0; i < 256; i++) {
                charBitmapHeights[i] = new byte[charWidth[i]];
                byte sum = 0;
                for (int j = 0; j < charBitmapHeights[i].length; j++) {
                    sum += data[index++];
                    charBitmapHeights[i][j] = sum;
                }
            }
            byte[] kerning = new byte[65536];
            for (int i = 0; i < 256; i++) {
                if (i != 32 && i != 160) {
                    for (int j = 0; j < 256; j++) {
                        if (j != 32 && j != 160) {
                            kerning[j + (i << 8)] = (byte) computeKerning(charBitmapWidth, charBitmapHeights, charHeights, advances, charWidth, i, j);
                        }
                    }
                }
            }
            ascent = charHeights[32] + charWidth[32];
        }
    }


    public int charWidth(char currentChar) {
        if (currentChar == 160) {
            currentChar = ' ';
        }

        return this.advances[charToByteCp1252(currentChar) & 255];
    }

    public static byte charToByteCp1252(char c) {
        switch (c) {
            case 8364:
                return -128;
            case 8218:
                return -126;
            case 402:
                return -125;
            case 8222:
                return -124;
            case 8230:
                return -123;
            case 8224:
                return -122;
            case 8225:
                return -121;
            case 710:
                return -120;
            case 8240:
                return -119;
            case 352:
                return -118;
            case 8249:
                return -117;
            case 338:
                return -116;
            case 381:
                return -114;
            case 8216:
                return -111;
            case 8217:
                return -110;
            case 8220:
                return -109;
            case 8221:
                return -108;
            case 8226:
                return -107;
            case 8211:
                return -106;
            case 8212:
                return -105;
            case 732:
                return -104;
            case 8482:
                return -103;
            case 353:
                return -102;
            case 8250:
                return -101;
            case 339:
                return -100;
            case 382:
                return -98;
            case 376:
                return -97;
            default:
                if (c > 0 && c < 128 || c >= 160 && c <= 255) {
                    return (byte) c;
                } else {
                    return 63;
                }
        }
    }

    public int getTextWidth(String text) {
        return stringWidthNoFormatting(text);
    }

    public int stringWidthNoFormatting(String var1) {
        if (var1 == null) {
            return 0;
        }
        return stringWidth(var1.replaceAll("\\@(.*?)\\@",""));
    }

    public int stringWidth(String text) {
        if (text == null) {
            return 0;
        }

        int tagStartIndex = -1;
        int previousChar = -1;
        int width = 0;

        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (currentChar == '<') {
                tagStartIndex = i;
            } else {
                if (currentChar == '>' && tagStartIndex != -1) {
                    String tag = text.substring(tagStartIndex + 1, i);
                    tagStartIndex = -1;
                    if (tag.equals("lt")) {
                        currentChar = '<';
                    } else if (tag.equals("gt")) {
                        currentChar = '>';
                    } else if (tag.startsWith("img=")) {
                        try {
                            int imageIndex = parseInt(tag.substring(4));
                            width += AbstractFont_modIconSprites[imageIndex].width;
                            previousChar = -1;
                        } catch (Exception e) {
                            // ignore error
                        }
                        continue;
                    } else if (tag.startsWith("cimg=")) {
                        try {
                            int imageIndex = parseInt(tag.substring(5));
                            width += ImageCache.get(imageIndex).myWidth;
                            previousChar = -1;
                        } catch (Exception e) {
                            // ignore error
                        }
                        continue;
                    }
                }

                if (currentChar == 160) {
                    currentChar = ' ';
                }

                if (tagStartIndex == -1) {
                    width += this.advances[charToByteCp1252(currentChar) & 255];
                    if (this.kerning != null && previousChar != -1) {
                        width += this.kerning[currentChar + (previousChar << 8)];
                    }
                    previousChar = currentChar;
                }
            }
        }

        return width;
    }

    public String breakString(String text, int maxLineWidth) {
        return this.breakLiness(text, new int[]{maxLineWidth}, AbstractFont_lines);
    }

    public int breakLines(String text, int[] widths, String[] lines) {
        if (text == null) {
            return 0;
        } else {
            int charCount = 0;
            int lineStart = 0;
            StringBuilder lineBuilder = new StringBuilder(100);
            int spacePos = -1;
            int spaceWidth = 0;
            byte addSpace = 0;
            int tagStart = -1;
            char prevChar = 0;
            int lineIndex = 0;
            int textLength = text.length();

            for(int index = 0; index < textLength; ++index) {
                char var15 = text.charAt(index);
                if (var15 == '<') {
                    tagStart = index;
                } else {
                    if (var15 == '>' && tagStart != -1) {
                        String tag = text.substring(tagStart + 1, index);
                        tagStart = -1;
                        lineBuilder.append('<');
                        lineBuilder.append(tag);
                        lineBuilder.append('>');
                        if (tag.equals("br")) {
                            lines[lineIndex] = lineBuilder.toString().substring(lineStart, lineBuilder.length());
                            ++lineIndex;
                            lineStart = lineBuilder.length();
                            charCount = 0;
                            spacePos = -1;
                            prevChar = 0;
                        } else if (tag.equals("lt")) {
                            charCount += this.charWidth('<');
                            if (this.kerning != null && prevChar != -1) {
                                charCount += this.kerning[(prevChar << 8) + 60];
                            }

                            prevChar = '<';
                        } else if (tag.equals("gt")) {
                            charCount += this.charWidth('>');
                            if (this.kerning != null && prevChar != -1) {
                                charCount += this.kerning[(prevChar << 8) + 62];
                            }

                            prevChar = '>';
                        } else if (tag.startsWith("img=")) {
                            try {
                                int var17 = parseInt(tag.substring(4));
                                charCount += AbstractFont_modIconSprites[var17].width;
                                prevChar = 0;
                            } catch (Exception var20) {
                            }
                        } else if (tag.startsWith("cimg=")) {
                            try {
                                int var17 = parseInt(tag.substring(5));
                                charCount += ImageCache.get(var17).myWidth;
                                prevChar = 0;
                            } catch (Exception var20) {
                            }
                        }

                        var15 = 0;
                    }

                    if (tagStart == -1) {
                        if (var15 != 0) {
                            lineBuilder.append(var15);
                            charCount += this.charWidth(var15);
                            if (this.kerning != null && prevChar != -1) {
                                charCount += this.kerning[var15 + (prevChar << 8)];
                            }

                            prevChar = var15;
                        }

                        if (var15 == ' ') {
                            spacePos = lineBuilder.length();
                            spaceWidth = charCount;
                            addSpace = 1;
                        }

                        if (widths != null && charCount > widths[lineIndex < widths.length ? lineIndex : widths.length - 1] && spacePos >= 0) {
                            lines[lineIndex] = lineBuilder.toString().substring(lineStart, spacePos - addSpace);
                            ++lineIndex;
                            lineStart = spacePos;
                            spacePos = -1;
                            charCount -= spaceWidth;
                            prevChar = 0;
                        }

                        if (var15 == '-') {
                            spacePos = lineBuilder.length();
                            spaceWidth = charCount;
                            addSpace = 0;
                        }
                    }
                }
            }

            String finalText = lineBuilder.toString();
            if (finalText.length() > lineStart) {
                lines[lineIndex++] = finalText.substring(lineStart, finalText.length());
            }

            return lineIndex;
        }
    }



    public String breakLiness(String text, int[] widths, String[] lines) {
        if (text == null) {
            return "";
        } else {
            int charCount = 0;
            int lineStart = 0;
            StringBuilder lineBuilder = new StringBuilder(100);
            int spacePos = -1;
            int spaceWidth = 0;
            byte addSpace = 0;
            int tagStart = -1;
            char prevChar = 0;
            int lineIndex = 0;
            int textLength = text.length();

            for(int index = 0; index < textLength; ++index) {
                char var15 = text.charAt(index);
                if (var15 == '<') {
                    tagStart = index;
                } else {
                    if (var15 == '>' && tagStart != -1) {
                        String tag = text.substring(tagStart + 1, index);
                        tagStart = -1;
                        lineBuilder.append('<');
                        lineBuilder.append(tag);
                        lineBuilder.append('>');
                        if (tag.equals("br")) {
                            lines[lineIndex] = lineBuilder.toString().substring(lineStart, lineBuilder.length());
                            ++lineIndex;
                            lineStart = lineBuilder.length();
                            charCount = 0;
                            spacePos = -1;
                            prevChar = 0;
                        } else if (tag.equals("lt")) {
                            charCount += this.charWidth('<');
                            if (this.kerning != null && prevChar != -1) {
                                charCount += this.kerning[(prevChar << 8) + 60];
                            }

                            prevChar = '<';
                        } else if (tag.equals("gt")) {
                            charCount += this.charWidth('>');
                            if (this.kerning != null && prevChar != -1) {
                                charCount += this.kerning[(prevChar << 8) + 62];
                            }

                            prevChar = '>';
                        } else if (tag.startsWith("img=")) {
                            try {
                                int var17 = parseInt(tag.substring(4));
                                charCount += AbstractFont_modIconSprites[var17].width;
                                prevChar = 0;
                            } catch (Exception var20) {
                            }
                        } else if (tag.startsWith("cimg=")) {
                            try {
                                int var17 = parseInt(tag.substring(5));
                                charCount += ImageCache.get(var17).myWidth;
                                prevChar = 0;
                            } catch (Exception var20) {
                            }
                        }

                        var15 = 0;
                    }

                    if (tagStart == -1) {
                        if (var15 != 0) {
                            lineBuilder.append(var15);
                            charCount += this.charWidth(var15);
                            if (this.kerning != null && prevChar != -1) {
                                charCount += this.kerning[var15 + (prevChar << 8)];
                            }

                            prevChar = var15;
                        }

                        if (var15 == ' ') {
                            spacePos = lineBuilder.length();
                            spaceWidth = charCount;
                            addSpace = 1;
                        }

                        if (widths != null && charCount > widths[lineIndex < widths.length ? lineIndex : widths.length - 1] && spacePos >= 0) {
                            lines[lineIndex] = lineBuilder.toString().substring(lineStart, spacePos - addSpace);
                            ++lineIndex;
                            lineStart = spacePos;
                            spacePos = -1;
                            charCount -= spaceWidth;
                            prevChar = 0;
                        }

                        if (var15 == '-') {
                            spacePos = lineBuilder.length();
                            spaceWidth = charCount;
                            addSpace = 0;
                        }
                    }
                }
            }

            return lineBuilder.toString();
        }
    }

    public static int parseInt(CharSequence var0) {
        return parseInt(String.valueOf(var0), 10);
    }

    public int lineWidth(String text, int maxLineWidth) {
        int numLines = breakLines(text, new int[]{maxLineWidth}, AbstractFont_lines);
        int maxWidth = 0;
        for (int i = 0; i < numLines; i++) {
            int lineWidth = stringWidth(AbstractFont_lines[i]);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }
        return maxWidth;
    }


    public int lineCount(String text, int maxLineWidth) {
        return this.breakLines(text, new int[]{maxLineWidth}, AbstractFont_lines);
    }

    public Bounds getBounds(int start, int end, String text, int x, int y) {
        if (text != null && text.length() >= end + start) {
            int centerX = x - stringWidth(text) / 2;
            centerX += stringWidth(text.substring(0, start));
            int baselineY = y - maxAscent;
            int width = stringWidth(text.substring(start, end + start));
            int height = maxAscent + maxDescent;
            return new Bounds(centerX, baselineY, width, height);
        } else {
            return new Bounds(x, y, 0, 0);
        }
    }

    public void draw(String string, int drawX, int drawY, int color, int shadow) {
        if (string != null) {
            this.reset(color, shadow);
            this.draw0(string, drawX, drawY);
        }
    }

    public void drawAlpha(String string, int drawX, int drawY, int color, int shadow, int alpha) {
        if (string != null) {
            this.reset(color, shadow);
            AbstractFont.alpha = alpha;
            this.draw0(string, drawX, drawY);
        }
    }


    public void drawRightAligned(String string, int drawX, int drawY, int color, int shadow) {
        if (string != null) {
            this.reset(color, shadow);
            this.draw0(string, drawX - this.stringWidth(string), drawY);
        }
    }

    public void drawCentered(String string, int drawX, int drawY, int color, int shadow) {
        if (string != null) {
            this.reset(color, shadow);
            this.draw0(string, drawX - this.stringWidth(string) / 2, drawY);
        }
    }


    public int drawLines(String text, int x, int y, int width, int height, int color, int fontSize, int alignment, int lineSpacing, int fontType) {
        if (text == null) {
            return 0;
        } else {
            this.reset(color, fontSize);
            if (fontType == 0) {
                fontType = this.ascent;
            }

            int[] maxLineWidth = new int[]{width};
            if (height < fontType + this.maxAscent + this.maxDescent && height < fontType + fontType) {
                maxLineWidth = null;
            }

            int lineCount = this.breakLines(text, maxLineWidth, AbstractFont_lines);
            if (lineSpacing == 3 && lineCount == 1) {
                lineSpacing = 1;
            }

            int lineY;
            int lineIndex;
            if (lineSpacing == 0) {
                lineY = y + this.maxAscent;
            } else if (lineSpacing == 1) {
                lineY = y + (height - this.maxAscent - this.maxDescent - fontType * (lineCount - 1)) / 2 + this.maxAscent;
            } else if (lineSpacing == 2) {
                lineY = y + height - this.maxDescent - fontType * (lineCount - 1);
            } else {
                lineIndex = (height - this.maxAscent - this.maxDescent - fontType * (lineCount - 1)) / (lineCount + 1);
                if (lineIndex < 0) {
                    lineIndex = 0;
                }

                lineY = y + lineIndex + this.maxAscent;
                fontType += lineIndex;
            }

            for(lineIndex = 0; lineIndex < lineCount; ++lineIndex) {
                if (alignment == 0) {
                    this.draw0(AbstractFont_lines[lineIndex], x, lineY);
                } else if (alignment == 1) {
                    this.draw0(AbstractFont_lines[lineIndex], x + (width - this.stringWidth(AbstractFont_lines[lineIndex])) / 2, lineY);
                } else if (alignment == 2) {
                    this.draw0(AbstractFont_lines[lineIndex], x + width - this.stringWidth(AbstractFont_lines[lineIndex]), lineY);
                } else if (lineIndex == lineCount - 1) {
                    this.draw0(AbstractFont_lines[lineIndex], x, lineY);
                } else {
                    this.calculateLineJustification(AbstractFont_lines[lineIndex], width);
                    this.draw0(AbstractFont_lines[lineIndex], x, lineY);
                    justificationTotal = 0;
                }

                lineY += fontType;
            }

            return lineCount;
        }
    }

    public void drawCenteredWave(String string, int drawX, int drawY, int color, int shadow, int ran) {
        if (string != null) {
            this.reset(color, shadow);
            int[] offsets = new int[string.length()];

            for(int index = 0; index < string.length(); ++index) {
                offsets[index] = (int)(Math.sin((double)index / 2.0 + (double)ran / 5.0) * 5.0);
            }

            this.drawWithOffsets0(string, drawX - this.stringWidth(string) / 2, drawY, null, offsets);
        }
    }

    public void drawCenteredWaveWithShaking(String string, int x, int y, int width, int height, int waveSeed) {
        if (string != null) {
            this.reset(width, height);
            int[] wave1 = new int[string.length()];
            int[] wave2 = new int[string.length()];

            for (int i = 0; i < string.length(); i++) {
                wave1[i] = (int)(Math.sin((double)i / 5.0 + (double)waveSeed / 5.0) * 5.0);
                wave2[i] = (int)(Math.sin((double)i / 3.0 + (double)waveSeed / 5.0) * 5.0);
            }

            this.drawWithOffsets0(string, x - this.stringWidth(string) / 2, y, wave1, wave2);
        }
    }

    public void drawCenteredShakeWithVariance(String string, int x, int y, int width, int height, int phase, int variance) {
        if (string != null) {
            reset(width, height);
            double varianceFactor = 7.0 - (double)variance / 8.0;
            if (varianceFactor < 0.0) {
                varianceFactor = 0.0;
            }

            int[] offsets = new int[string.length()];

            for(int i = 0; i < string.length(); ++i) {
                offsets[i] = (int)(Math.sin((double)i / 1.5 + (double)phase / 1.0) * varianceFactor);
            }

            drawWithOffsets0(string, x - stringWidth(string) / 2, y, (int[])null, offsets);
        }
    }

    public void method7441(String var1, int var2, int var3, int var4, int var5, int[] var6) {
        if (var1 != null) {
            this.reset(var4, var5);
            int[] var7 = null;
            if (var6 != null) {
                var7 = this.method7443(var6, var1.length());
            }

            this.drawWithOffsets0(var1, var2 - this.stringWidth(var1) / 2, var3, var7, (int[])null);
        }
    }

    int[] method7443(int[] var1, int var2) {
        if (var2 == 0) {
            return null;
        } else {
            int[] var3 = new int[var2];
            float var4 = (float)var1.length / (float)var2;

            for (int var5 = 0; var5 < var2; ++var5) {
                var3[var5] = var1[(int)((float)var5 * var4)];
            }

            return var3;
        }
    }


    public void drawRandomAlphaAndSpacing(String string, int x, int y, int width, int height, int seed) {
        if (string != null) {
            reset(width, height);
            AbstractFont_random.setSeed(seed);
            alpha = 192 + (AbstractFont_random.nextInt() & 31);
            int[] offsets = new int[string.length()];
            int offset = 0;

            for (int i = 0; i < string.length(); i++) {
                offsets[i] = offset;
                if ((AbstractFont_random.nextInt() & 3) == 0) {
                    offset++;
                }
            }

            drawWithOffsets0(string, x, y, offsets, null);
        }
    }

    void reset(int col, int shadowCol) {
        strike = -1;
        underline = -1;
        previousShadow = shadowCol;
        shadow = shadowCol;
        previousColor = col;
        color = col;
        alpha = 256;
        justificationTotal = 0;
        justificationCurrent = 0;
    }


    void decodeTag(String text) {
        try {
            if (text.startsWith("col=")) {
                String color = text.substring(4);
                AbstractFont.color = color.length() < 6 ? Color.decode(color).getRGB() : Integer.parseInt(color, 16);
            } else if (text.equals("/col")) {
                color = previousColor;
            } else if (text.startsWith("str=")) {
                strike = parseInt(text.substring(4), 16);
            } else if (text.equals("str")) {
                strike = 8388608;
            } else if (text.equals("/str")) {
                strike = -1;
            } else if (text.startsWith("u=")) {
                underline = parseInt(text.substring(2), 16);
            } else if (text.equals("u")) {
                underline = 0;
            } else if (text.equals("/u")) {
                underline = -1;
            } else if (text.startsWith("shad=")) {
                shadow = parseInt(text.substring(5), 16);
            } else if (text.equals("shad")) {
                shadow = 0;
            } else if (text.equals("/shad")) {
                shadow = previousShadow;
            } else if (text.equals("br")) {
                this.reset(previousColor, previousShadow);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    static int parseInt(String str, int radix) {
        if (radix >= 2 && radix <= 36) {
            boolean isNegative = false;
            boolean hasValue = false;
            int result = 0;
            int length = str.length();

            for (int i = 0; i < length; i++) {
                int charValue = str.charAt(i);
                if (i == 0) {
                    if (charValue == '-') {
                        isNegative = true;
                        continue;
                    }
                    if (charValue == '+') {
                        continue;
                    }
                }
                if (charValue >= 48 && charValue <= 57) {
                    charValue -= 48;
                } else if (charValue >= 65 && charValue <= 90) {
                    charValue -= 55;
                } else if (charValue >= 97 && charValue <= 122) {
                    charValue -= 87;
                } else {
                    throw new NumberFormatException();
                }
                if (charValue >= radix) {
                    throw new NumberFormatException();
                }
                if (isNegative) {
                    charValue = -charValue;
                }
                int newResult = result * radix + charValue;
                if (newResult / radix != result) {
                    throw new NumberFormatException();
                }
                result = newResult;
                hasValue = true;
            }

            if (!hasValue) {
                throw new NumberFormatException();
            }
            return result;
        } else {
            throw new IllegalArgumentException("Radix must be between 2 and 36, inclusive. Found: " + radix);
        }
    }


    public void calculateLineJustification(String text, int lineWidth) {
        int spaces = 0;
        boolean insideTag = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '<') {
                insideTag = true;
            } else if (c == '>') {
                insideTag = false;
            } else if (!insideTag && c == ' ') {
                spaces++;
            }
        }

        if (spaces > 0) {
            justificationTotal = (lineWidth - this.stringWidth(text) << 8) / spaces;
        }
    }

    public void drawBasicString(String string, int drawX, int drawY, int color) {
        draw(string,drawX,drawY,color,-1);
    }

    public void drawBasicString(String string, int drawX, int drawY, int color, int shadow) {
        draw(string,drawX,drawY,color,shadow);
    }

    public void drawCenteredString(String string, int drawX, int drawY, int color, int shadow) {
        if (string != null) {
            this.reset(color, shadow);
            drawBasicString(string,drawX - stringWidth(string) / 2,drawY,color,shadow);
        }
    }

    public void drawCenteredText(String text, int x, int y, int color, boolean shadowed) {
        if (text != null) {
            this.reset(color, shadowed ? 0 : -1);
            drawBasicString(text,x - stringWidth(text) / 2,y,color,shadowed ? 0 : -1);
        }
    }


    public void drawText(int color, String s, int drawY, int drawX) {
        drawCenteredString(s,drawX,drawY,color,-1);
    }

    public void drawTextWithPotentialShadow(boolean shadow, int drawX, int color, String string, int drawY) {
        drawBasicString(string,drawX,drawY,color,shadow ? 0 : -1);
    }

    public void drawCenteredTextWithPotentialShadow(int color, int drawX, String string, int drawY, boolean shadow) {
        drawTextWithPotentialShadow(shadow, drawX - getTextWidth(string) / 2, color, string, drawY);
    }

    public void drawCenteredString(String string, int drawX, int drawY) {
        if (string != null) {
            this.reset(0xFFFFFF, -1);
            drawBasicString(string,drawX - stringWidth(string) / 2,drawY);
        }
    }

    public void drawBasicString(String string, int drawX, int drawY) {
        draw0(string,drawX,drawY);
    }


    public void render(int color, String string, int drawY, int drawX) {
        draw(string,drawX,drawY,color,-1);
    }

    void draw0(String string, int drawX, int drawY) {
        drawY -= this.ascent;
        int var4 = -1;
        int var5 = -1;

        string = handleOldSyntax(string);

        for(int var6 = 0; var6 < string.length(); ++var6) {
            if (string.charAt(var6) != 0) {
                char var7 = (char)(charToByteCp1252(string.charAt(var6)) & 255);
                if (var7 == '<') {
                    var4 = var6;
                } else {
                    int var9;
                    if (var7 == '>' && var4 != -1) {
                        String var8 = string.substring(var4 + 1, var6);
                        var4 = -1;
                        if (var8.equals("lt")) {
                            var7 = '<';
                        } else {
                            if (!var8.equals("gt")) {
                                if (var8.startsWith("img=")) {
                                    try {
                                        var9 = parseInt(var8.substring(4));
                                        IndexedImage var10 = AbstractFont_modIconSprites[var9];
                                        var10.draw(drawX, drawY + this.ascent - var10.height);
                                        drawX += var10.width;
                                        var5 = -1;
                                    } catch (Exception var14) {
                                    }
                                } else {
                                    this.decodeTag(var8);
                                }
                                if (var8.startsWith("cimg=")) {
                                    try {
                                        var9 = parseInt(var8.substring(5));
                                        Sprite var10 = ImageCache.get(var9);
                                        var10.drawAdvancedSprite(drawX, drawY + this.ascent - var10.myHeight + 2);
                                        drawX += var10.myWidth;
                                        var5 = -1;
                                    } catch (Exception var14) {
                                    }
                                } else {
                                    this.decodeTag(var8);
                                }
                                continue;
                            }

                            var7 = '>';
                        }
                    }

                    if (var7 == 160) {
                        var7 = ' ';
                    }

                    if (var4 == -1) {
                        if (this.kerning != null && var5 != -1) {
                            drawX += this.kerning[var7 + (var5 << 8)];
                        }

                        int var12 = this.widths[var7];
                        var9 = this.heights[var7];
                        if (var7 != ' ') {
                            if (alpha == 256) {
                                if (shadow != -1) {
                                    drawGlyph2(this.pixels[var7], drawX + this.leftBearings[var7] + 1, drawY + this.topBearings[var7] + 1, var12, var9, shadow);
                                }

                                this.drawGlyph(this.pixels[var7], drawX + this.leftBearings[var7], drawY + this.topBearings[var7], var12, var9, color);
                            } else {
                                if (shadow != -1) {
                                    drawGlyphAlpha2(this.pixels[var7], drawX + this.leftBearings[var7] + 1, drawY + this.topBearings[var7] + 1, var12, var9, shadow, alpha);
                                }

                                this.drawGlyphAlpha(this.pixels[var7], drawX + this.leftBearings[var7], drawY + this.topBearings[var7], var12, var9, color, alpha);
                            }
                        } else if (justificationTotal > 0) {
                            justificationCurrent += justificationTotal;
                            drawX += justificationCurrent >> 8;
                            justificationCurrent &= 255;
                        }

                        int var13 = this.advances[var7];
                        if (strike != -1) {
                            Rasterizer2D.drawHorizontalLine(drawX, drawY + (int)((double)this.ascent * 0.7), var13, strike);
                        }

                        if (underline != -1) {
                            Rasterizer2D.drawHorizontalLine(drawX, drawY + this.ascent + 1, var13, underline);
                        }

                        drawX += var13;
                        var5 = var7;
                    }
                }
            }
        }

    }


    void drawWithOffsets0(String var1, int var2, int var3, int[] var4, int[] var5) {
        var3 -= this.ascent;
        int var6 = -1;
        int var7 = -1;
        int var8 = 0;

        for(int var9 = 0; var9 < var1.length(); ++var9) {
            if (var1.charAt(var9) != 0) {
                char var10 = (char)(charToByteCp1252(var1.charAt(var9)) & 255);
                if (var10 == '<') {
                    var6 = var9;
                } else {
                    int var12;
                    int var13;
                    int var14;
                    if (var10 == '>' && var6 != -1) {
                        String var11 = var1.substring(var6 + 1, var9);
                        var6 = -1;
                        if (var11.equals("lt")) {
                            var10 = '<';
                        } else {
                            if (!var11.equals("gt")) {
                                if (var11.startsWith("img=")) {
                                    try {
                                        if (var4 != null) {
                                            var12 = var4[var8];
                                        } else {
                                            var12 = 0;
                                        }

                                        if (var5 != null) {
                                            var13 = var5[var8];
                                        } else {
                                            var13 = 0;
                                        }


                                        ++var8;
                                        var14 = parseInt(var11.substring(4));
                                        IndexedImage var15 = AbstractFont_modIconSprites[var14];
                                        var15.draw(var12 + var2, var13 + (var3 + this.ascent - var15.height));
                                        var2 += var15.width;
                                        var7 = -1;
                                    } catch (Exception var19) {
                                    }
                                } else {
                                    this.decodeTag(var11);
                                }
                                if (var11.startsWith("cimg=")) {
                                    try {
                                        if (var4 != null) {
                                            var12 = var4[var8];
                                        } else {
                                            var12 = 0;
                                        }

                                        if (var5 != null) {
                                            var13 = var5[var8];
                                        } else {
                                            var13 = 0;
                                        }

                                        ++var8;
                                        var14 = parseInt(var11.substring(5));
                                        Sprite var15 = ImageCache.get(var14);
                                        var15.drawSprite(var12 + var2, var13 + (var3 + this.ascent - var15.myHeight) + 2);
                                        var2 += var15.myWidth;
                                        var7 = -1;
                                    } catch (Exception var19) {
                                    }
                                } else {
                                    this.decodeTag(var11);
                                }
                                continue;
                            }

                            var10 = '>';
                        }
                    }

                    if (var10 == 160) {
                        var10 = ' ';
                    }

                    if (var6 == -1) {
                        if (this.kerning != null && var7 != -1) {
                            var2 += this.kerning[var10 + (var7 << 8)];
                        }

                        int var17 = this.widths[var10];
                        var12 = this.heights[var10];
                        if (var4 != null) {
                            var13 = var4[var8];
                        } else {
                            var13 = 0;
                        }

                        if (var5 != null) {
                            var14 = var5[var8];
                        } else {
                            var14 = 0;
                        }

                        ++var8;
                        if (var10 != ' ') {
                            if (alpha == 256) {
                                if (shadow != -1) {
                                    drawGlyph2(this.pixels[var10], var13 + var2 + this.leftBearings[var10] + 1, var3 + var14 + this.topBearings[var10] + 1, var17, var12, shadow);
                                }

                                this.drawGlyph(this.pixels[var10], var13 + var2 + this.leftBearings[var10], var3 + var14 + this.topBearings[var10], var17, var12, color);
                            } else {
                                if (shadow != -1) {
                                    drawGlyphAlpha2(this.pixels[var10], var13 + var2 + this.leftBearings[var10] + 1, var3 + var14 + this.topBearings[var10] + 1, var17, var12, shadow, alpha);
                                }

                                this.drawGlyphAlpha(this.pixels[var10], var13 + var2 + this.leftBearings[var10], var3 + var14 + this.topBearings[var10], var17, var12, color, alpha);
                            }
                        } else if (justificationTotal > 0) {
                            justificationCurrent += justificationTotal;
                            var2 += justificationCurrent >> 8;
                            justificationCurrent &= 255;
                        }

                        int var18 = this.advances[var10];
                        if (strike != -1) {
                            Rasterizer2D.drawHorizontalLine(var2, var3 + (int)((double)this.ascent * 0.7), var18, strike);
                        }

                        if (underline != -1) {
                            Rasterizer2D.drawHorizontalLine(var2, var3 + this.ascent, var18, underline);
                        }

                        var2 += var18;
                        var7 = var10;
                    }
                }
            }
        }

    }

    static int computeKerning(byte[][] firstGlyphs, byte[][] secondGlyphs, int[] firstWidths, int[] secondWidths, int[] secondHeights, int firstIndex, int secondIndex) {
        int firstStart = firstWidths[firstIndex];
        int firstEnd = firstStart + secondHeights[firstIndex];
        int secondStart = secondWidths[secondIndex];
        int secondEnd = secondStart + secondHeights[secondIndex];
        int commonStart = firstStart;
        if (secondStart > firstStart) {
            commonStart = secondStart;
        }

        int commonEnd = firstEnd;
        if (secondEnd < firstEnd) {
            commonEnd = secondEnd;
        }

        int minimum = firstWidths[firstIndex];
        if (secondWidths[secondIndex] < minimum) {
            minimum = secondWidths[secondIndex];
        }

        byte[] firstArray = firstGlyphs[firstIndex];
        byte[] secondArray = secondGlyphs[secondIndex];
        int firstArrayIndex = commonStart - firstStart;
        int secondArrayIndex = commonStart - secondStart;

        for (int i = commonStart; i < commonEnd; i++) {
            int combinedValue = firstArray[firstArrayIndex++] + secondArray[secondArrayIndex++];
            if (combinedValue < minimum) {
                minimum = combinedValue;
            }
        }

        return -minimum;
    }

    public static String escapeBrackets(String input) {
        int length = input.length();
        int replacementCount = 0;

        int charValue;
        for (int i = 0; i < length; i++) {
            charValue = input.charAt(i);
            if (charValue == 60 || charValue == 62) {
                replacementCount += 3;
            }
        }

        StringBuilder result = new StringBuilder(length + replacementCount);

        for (int i = 0; i < length; i++) {
            char character = input.charAt(i);
            if (character == '<') {
                result.append("<lt>");
            } else if (character == '>') {
                result.append("<gt>");
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    static void drawGlyph2(byte[] var0, int x, int y, int width, int height, int color) {
        int destinationIndex = x + y * Rasterizer2D.width;
        int destinationStep = Rasterizer2D.width - width;
        int sourceIndex = 0;
        int sourceSkip = 0;
        int clip;
        if (y < Rasterizer2D.yClipStart) {
            clip = Rasterizer2D.yClipStart - y;
            height -= clip;
            y = Rasterizer2D.yClipStart;
            sourceSkip += width * clip;
            destinationIndex += clip * Rasterizer2D.width;
        }

        if (y + height > Rasterizer2D.yClipEnd) {
            height -= y + height - Rasterizer2D.yClipEnd;
        }

        if (x < Rasterizer2D.xClipStart) {
            clip = Rasterizer2D.xClipStart - x;
            width -= clip;
            x = Rasterizer2D.xClipStart;
            sourceSkip += clip;
            destinationIndex += clip;
            sourceIndex += clip;
            destinationStep += clip;
        }

        if (width + x > Rasterizer2D.xClipEnd) {
            clip = width + x - Rasterizer2D.xClipEnd;
            width -= clip;
            sourceIndex += clip;
            destinationStep += clip;
        }

        if (width > 0 && height > 0) {
            placeGlyph(Rasterizer2D.pixels, var0, color, sourceSkip, destinationIndex, width, height, destinationStep, sourceIndex);
        }
    }

    static void placeGlyph(int[] pixels, byte[] glyphData, int color, int glyphDataPointer, int pixelPointer, int width, int height, int pixelStride, int glyphStride) {
        int remainingPixels = -(width >> 2);
        width = -(width & 3);

        for (int i = -height; i < 0; i++) {
            int j;
            for (j = remainingPixels; j < 0; j++) {
                if (glyphData[glyphDataPointer++] != 0) {
                    drawAlpha(pixels, pixelPointer++, color, 255);
                } else {
                    pixelPointer++;
                }

                if (glyphData[glyphDataPointer++] != 0) {
                    drawAlpha(pixels, pixelPointer++, color, 255);
                } else {
                    pixelPointer++;
                }

                if (glyphData[glyphDataPointer++] != 0) {
                    drawAlpha(pixels, pixelPointer++, color, 255);
                } else {
                    pixelPointer++;
                }

                if (glyphData[glyphDataPointer++] != 0) {
                    drawAlpha(pixels, pixelPointer++, color, 255);
                } else {
                    pixelPointer++;
                }
            }

            for (j = width; j < 0; j++) {
                if (glyphData[glyphDataPointer++] != 0) {
                    drawAlpha(pixels, pixelPointer++, color, 255);
                } else {
                    pixelPointer++;
                }
            }

            pixelPointer += pixelStride;
            glyphDataPointer += glyphStride;
        }
    }

    static void drawGlyphAlpha2(byte[] glyph, int x, int y, int width, int height, int color, int shadow) {
        int destinationIndex = x + y * Rasterizer2D.width;
        int destinationStep = Rasterizer2D.width - width;
        int sourceIndex = 0;
        int sourceSkip = 0;
        int clip;

        if (y < Rasterizer2D.yClipStart) {
            clip = Rasterizer2D.yClipStart - y;
            height -= clip;
            y = Rasterizer2D.yClipStart;
            sourceSkip += width * clip;
            destinationIndex += clip * Rasterizer2D.width;
        }

        if (y + height > Rasterizer2D.yClipEnd) {
            height -= y + height - Rasterizer2D.yClipEnd;
        }

        if (x < Rasterizer2D.xClipStart) {
            clip = Rasterizer2D.xClipStart - x;
            width -= clip;
            x = Rasterizer2D.xClipStart;
            sourceSkip += clip;
            destinationIndex += clip;
            sourceIndex += clip;
            destinationStep += clip;
        }

        if (width + x > Rasterizer2D.xClipEnd) {
            clip = width + x - Rasterizer2D.xClipEnd;
            width -= clip;
            sourceIndex += clip;
            destinationStep += clip;
        }

        if (width > 0 && height > 0) {
            placeGlyphAlpha(Rasterizer2D.pixels, glyph, color, sourceSkip, destinationIndex, width, height, destinationStep, sourceIndex, shadow);
        }
    }

    static void placeGlyphAlpha(int[] pixels, byte[] data, int color, int dataIndex, int pixelsIndex, int width, int height, int pixelsDelta, int dataDelta, int alpha) {
        int blendedColor = ((color & 0xff00) * alpha & 0xff0000) + (alpha * (color & 0xff00ff) & -0xff00ff) >> 8;
        int inverseAlpha = 256 - alpha;

        for (int y = -height; y < 0; ++y) {
            for (int x = -width; x < 0; ++x) {
                if (data[dataIndex++] != 0) {
                    int pixel = pixels[pixelsIndex];
                    Rasterizer2D.drawAlpha(pixels,pixelsIndex++,(((pixel & 0xff00) * inverseAlpha & 0xff0000) + ((pixel & 0xff00ff) * inverseAlpha & -0xff00ff) >> 8) + blendedColor,255);
                } else {
                    ++pixelsIndex;
                }
            }
            pixelsIndex += pixelsDelta;
            dataIndex += dataDelta;
        }
    }

    public static String handleOldSyntax(String text) {
        text = text.replace("@purp@", "<col=9400D3>");
        text = text.replace("@red@", "<col=ff0000>");
        text = text.replace("@red1@", "<col=800020>");
        text = text.replace("@gre@", "<col=65280>");
        text = text.replace("@blu@", "<col=255>");
        text = text.replace("@yel@", "<col=ffff00>");
        text = text.replace("@cya@", "<col=65535>");
        text = text.replace("@mag@", "<col=ff00ff>");
        text = text.replace("@whi@", "<col=ffffff>");
        text = text.replace("@lre@", "<col=ff9040>");
        text = text.replace("@dre@", "<col=800000>");
        text = text.replace("@bla@", "<col=0>");
        text = text.replace("@or1@", "<col=ffb000>");
        text = text.replace("@or2@", "<col=ff7000>");
        text = text.replace("@or3@", "<col=ff3000>");
        text = text.replace("@or4@", "<col=EE9021>");
        text = text.replace("@gr1@", "<col=c0ff00>");
        text = text.replace("@blu5@", "<col=871F78>");
        text = text.replace("@gr2@", "<col=80ff00>");
        text = text.replace("@gr3@", "<col=40ff00>");
        text = text.replace("@gr4@", "<col=006000>");
        text = text.replace("@gr7@", "<col=768F7B>");
        text = text.replace("@RED@", "<col=ffff00>");
        text = text.replace("@GRE@", "<col=65280>");
        text = text.replace("@BLU@", "<col=255>");
        text = text.replace("@YEL@", "<col=ff0000>");
        text = text.replace("@CYA@", "<col=65535>");
        text = text.replace("@MAG@", "<col=ff00ff>");
        text = text.replace("@WHI@", "<col=ffffff>");
        text = text.replace("@LRE@", "<col=ff9040>");
        text = text.replace("@DRE@", "<col=800000>");
        text = text.replace("@BLA@", "<col=0>");
        text = text.replace("@OR1@", "<col=ffb000>");
        text = text.replace("@OR2@", "<col=ff7000>");
        text = text.replace("@pi1@", "<col=AB91AE>");
        text = text.replace("@OR3@", "<col=ff3000>");
        text = text.replace("@GR1@", "<col=c0ff00>");
        text = text.replace("@GR2@", "<col=80ff00>");
        text = text.replace("@GR3@", "<col=40ff00>");
        text = text.replace("@cr1@", "<img=0>");
        text = text.replace("@cr2@", "<img=2>");
        text = text.replace("@cr3@", "<img=3>");
        text = text.replace("@cr4@", "<img=4>");
        text = text.replace("@cr5@", "<img=5>");
        text = text.replace("@cr6@", "<img=6>");
        text = text.replace("@cr8@", "<img=8>");
        text = text.replace("@cr11@", "<img=11>");
        text = text.replace("@cr13@", "<img=12>");
        return text;
    }

}
