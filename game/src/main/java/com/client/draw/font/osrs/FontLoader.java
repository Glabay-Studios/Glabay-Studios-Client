package com.client.draw.font.osrs;


import com.client.graphics.SpriteData;
import com.client.js5.Js5List;
import com.client.js5.disk.AbstractArchive;

import java.util.HashMap;

public class FontLoader {

    public HashMap<FontInfo, RSFontOSRS> fonts;


    public FontLoader() {
        this.fonts = new HashMap<>();
    }

    public void createMap() {
        for (FontInfo fontInfo : FontInfo.initialFonts()) {
            RSFontOSRS font = loadFont(Js5List.sprites, Js5List.fonts, fontInfo.getId(), 0);
            if (font != null) {
                fonts.put(fontInfo, font);
            }
        }
    }

    public static RSFontOSRS loadFont(AbstractArchive spritesArchive, AbstractArchive fontsArchive, int fontID, int var3) {
        byte[] spriteData = spritesArchive.takeFile(fontID, var3);
        boolean spriteEncoded;
        if (spriteData == null) {
            spriteEncoded = false;
        } else {
            SpriteData.decode(spriteData);
            spriteEncoded = true;
        }

        if (!spriteEncoded) {
            return null;
        } else {
            byte[] data = fontsArchive.takeFile(fontID, var3);
            RSFontOSRS drawFont;
            if (data == null) {
                drawFont = null;
            } else {
                RSFontOSRS font = new RSFontOSRS(data, SpriteData.xOffsets, SpriteData.yOffsets, SpriteData.spriteWidths, SpriteData.spriteHeights, SpriteData.spritePalette, SpriteData.pixels);
                SpriteData.xOffsets = null;
                SpriteData.yOffsets = null;
                SpriteData.spriteWidths = null;
                SpriteData.spriteHeights = null;
                SpriteData.spritePalette = null;
                SpriteData.pixels = null;
                drawFont = font;
            }

            return drawFont;
        }
    }

}