package com.client.graphics;

import com.client.Buffer;

public class SpriteData {

    public static int[] spriteWidths;
    public static byte[][] pixels;
    public static int[] spritePalette;
    public static int[] xOffsets;
    public static int[] spriteHeights;
    public static int spriteCount;
    public static int[] yOffsets;
    public static int spriteWidth;
    public static int spriteHeight;

    public static void decode(byte[] var0) {
        Buffer var1 = new Buffer(var0);
        var1.currentPosition = var0.length - 2;
        spriteCount = var1.readUShort();
        xOffsets = new int[spriteCount];
        yOffsets = new int[spriteCount];
        spriteWidths = new int[spriteCount];
        spriteHeights = new int[spriteCount];
        pixels = new byte[spriteCount][];
        var1.currentPosition = var0.length - 7 - spriteCount * 8;
        spriteWidth = var1.readUShort();
        spriteHeight = var1.readUShort();
        int var2 = (var1.readUnsignedByte() & 255) + 1;

        int var3;
        for(var3 = 0; var3 < spriteCount; ++var3) {
            xOffsets[var3] = var1.readUShort();
        }

        for(var3 = 0; var3 < spriteCount; ++var3) {
            yOffsets[var3] = var1.readUShort();
        }

        for(var3 = 0; var3 < spriteCount; ++var3) {
            spriteWidths[var3] = var1.readUShort();
        }

        for(var3 = 0; var3 < spriteCount; ++var3) {
            spriteHeights[var3] = var1.readUShort();
        }

        var1.currentPosition = var0.length - 7 - spriteCount * 8 - (var2 - 1) * 3;
        spritePalette = new int[var2];

        for(var3 = 1; var3 < var2; ++var3) {
            spritePalette[var3] = var1.readMedium();
            if (spritePalette[var3] == 0) {
                spritePalette[var3] = 1;
            }
        }

        var1.currentPosition = 0;

        for(var3 = 0; var3 < spriteCount; ++var3) {
            int var4 = spriteWidths[var3];
            int var5 = spriteHeights[var3];
            int var6 = var5 * var4;
            byte[] var7 = new byte[var6];
            pixels[var3] = var7;
            int var8 = var1.readUnsignedByte();
            int var9;
            if (var8 == 0) {
                for(var9 = 0; var9 < var6; ++var9) {
                    var7[var9] = var1.readSignedByte();
                }
            } else if (var8 == 1) {
                for(var9 = 0; var9 < var4; ++var9) {
                    for(int var10 = 0; var10 < var5; ++var10) {
                        var7[var9 + var4 * var10] = var1.readSignedByte();
                    }
                }
            }
        }

    }


}
