package com.client.definitions;

import com.client.Buffer;
import com.client.Client;
import com.client.RSFont;
import com.client.Sprite;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.draw.ImageCache;
import com.client.draw.font.osrs.FontLoader;
import com.client.draw.font.osrs.RSFontOSRS;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import net.runelite.api.HitsplatComposition;
import net.runelite.api.events.PostHealthBar;
import net.runelite.api.events.PostHitSplat;
import net.runelite.rs.api.RSHitSplatDefinition;

public class HitSplatDefinition extends DualNode implements RSHitSplatDefinition {

    public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);

    public static EvictingDualNodeHashTable spritesCached = new EvictingDualNodeHashTable(64);

    public static EvictingDualNodeHashTable fontsCached = new EvictingDualNodeHashTable(20);

    private int fontId;

    public int textColor;

    public int animationDuration;

    public int classicGraphic;

    private int middleGraphic;

    private int leftGraphic;

    private  int rightGraphic;

    public int animationEndX;

    public int animationEndY;

    public int fadeat;

    private String damageFormat;

    public int usedDamage;

    public int damageYOfset;

    public int[] multimark;

    private int multivarbit;

    private int multivarp;

    private HitSplatDefinition() {
        this.fontId = -1;
        this.textColor = 16777215;
        this.animationDuration = 70;
        this.classicGraphic = -1;
        this.middleGraphic = -1;
        this.leftGraphic = -1;
        this.rightGraphic = -1;
        this.animationEndX = 0;
        this.animationEndY = 0;
        this.fadeat = -1;
        this.damageFormat = "";
        this.usedDamage = -1;
        this.damageYOfset = 0;
        this.multivarbit = -1;
        this.multivarp = -1;
    }

    public static HitSplatDefinition lookup(int id) {
        HitSplatDefinition definition = (HitSplatDefinition)cached.get(id);
        if (definition == null) {
            byte[] payload = Js5List.configs.takeFile(Js5ConfigType.HITSPLAT, id);
            definition = new HitSplatDefinition();
            if (payload != null) {
                definition.decode(new Buffer(payload));
            }

            PostHitSplat postHealthBar = new PostHitSplat();
            postHealthBar.setHealthBar(definition);
            Client.instance.getCallbacks().post(postHealthBar);

            cached.put(definition, id);
        }
        return definition;
    }

    void decode(Buffer buffer) {
        while (true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }

            this.decodeNext(buffer, opcode);
        }
    }


    void decodeNext(Buffer var1, int var2) {
        if (var2 == 1) {
            this.fontId = var1.readNullableLargeSmart();
        } else if (var2 == 2) {
            this.textColor = var1.readMedium();
        } else if (var2 == 3) {
            this.classicGraphic = var1.readNullableLargeSmart();
        } else if (var2 == 4) {
            this.leftGraphic = var1.readNullableLargeSmart();
        } else if (var2 == 5) {
            this.middleGraphic = var1.readNullableLargeSmart();
        } else if (var2 == 6) {
            this.rightGraphic = var1.readNullableLargeSmart();
        } else if (var2 == 7) {
            this.animationEndX = var1.readShort();
        } else if (var2 == 8) {
            this.damageFormat = var1.readStringCp1252NullCircumfixed();
        } else if (var2 == 9) {
            this.animationDuration = var1.readUnsignedShort();
        } else if (var2 == 10) {
            this.animationEndY = var1.readShort();
        } else if (var2 == 11) {
            this.fadeat = 0;
        } else if (var2 == 12) {
            this.usedDamage = var1.readUnsignedByte();
        } else if (var2 == 13) {
            this.damageYOfset = var1.readShort();
        } else if (var2 == 14) {
            var1.readUnsignedShort();
        } else if (var2 == 17 || var2 == 18) {
            this.multivarbit = var1.readUnsignedShort();
            if (this.multivarbit == 65535) {
                this.multivarbit = -1;
            }

            this.multivarp = var1.readUnsignedShort();
            if (this.multivarp == 65535) {
                this.multivarp = -1;
            }

            int var3 = -1;
            if (var2 == 18) {
                var3 = var1.readUnsignedShort();
                if (var3 == 65535) {
                    var3 = -1;
                }
            }

            int var4 = var1.readUnsignedByte();
            this.multimark = new int[var4 + 2];

            for (int var5 = 0; var5 <= var4; ++var5) {
                this.multimark[var5] = var1.readUnsignedShort();
                if (this.multimark[var5] == 65535) {
                    this.multimark[var5] = -1;
                }
            }

            this.multimark[var4 + 1] = var3;
        }

    }

    public final HitSplatDefinition transform() {
        int var1 = -1;
        if (this.multivarbit != -1) {
            VariableBits varBit = VariableBits.lookup(multivarbit);
            int j = varBit.baseVar;
            int k = varBit.startBit;
            int l = varBit.endBit;
            int i1 = Client.anIntArray1232[l - k];
            var1 = Client.instance.variousSettings[j] >> k & i1;
        } else if (this.multivarp != -1) {
            var1 = Client.instance.settings[this.multivarp];
        }

        int var2;
        if (var1 >= 0 && var1 < this.multimark.length - 1) {
            var2 = this.multimark[var1];
        } else {
            var2 = this.multimark[this.multimark.length - 1];
        }

        return var2 != -1 ? lookup(var2) : null;
    }


    public String formatDamage(int var1) {
        String damageTemplate = this.damageFormat;
        while (true) {
            int index = damageTemplate.indexOf("%1");
            if (index < 0) {
                return damageTemplate;
            }
            damageTemplate = damageTemplate.substring(0, index) + intToString(var1, false) + damageTemplate.substring(index + 2);
        }
    }

    public static String intToString(int value, boolean useCustomFormat) {
        return useCustomFormat && value >= 0 ? convertIntToCustomString(value, 10, useCustomFormat) : Integer.toString(value);
    }

    static String convertIntToCustomString(int number, int base, boolean usePlusPrefix) {
        if (base < 2 || base > 36) {
            throw new IllegalArgumentException("Invalid base: " + base);
        }

        if (usePlusPrefix && number >= 0) {
            int numberOfDigits = 2;

            for (int temp = number / base; temp != 0; numberOfDigits++) {
                temp /= base;
            }

            char[] formattedNumber = new char[numberOfDigits];
            formattedNumber[0] = '+';

            for (int index = numberOfDigits - 1; index > 0; index--) {
                int currentNumber = number;
                number /= base;
                int remainder = currentNumber - number * base;

                if (remainder >= 10) {
                    formattedNumber[index] = (char) (remainder + 87); // Convert numbers 10-35 to 'a'-'z'
                } else {
                    formattedNumber[index] = (char) (remainder + 48); // Convert numbers 0-9 to '0'-'9'
                }
            }

            return new String(formattedNumber);
        } else {
            return Integer.toString(number, base);
        }
    }



    public Sprite getClassicGraphic() {
        if (this.classicGraphic == -1) {
            return null;
        }
        return ImageCache.get(this.classicGraphic);
    }

    public Sprite getMiddleGraphic() {
        if (this.middleGraphic < 0) {
            return null;
        } else {
            Sprite middleSprite = (Sprite) spritesCached.get(this.middleGraphic);
            if (middleSprite == null) {
                middleSprite = Sprite.getSprite(this.middleGraphic, 0);
                if (middleSprite != null) {
                    spritesCached.put(middleSprite, this.middleGraphic);
                }

            }
            return middleSprite;
        }
    }

    public Sprite getLeftGraphic() {
        if (this.leftGraphic < 0) {
            return null;
        } else {
            Sprite leftSprite = (Sprite) spritesCached.get((long)this.leftGraphic);
            if (leftSprite == null) {
                leftSprite = Sprite.getSprite(this.leftGraphic, 0);
                if (leftSprite != null) {
                    spritesCached.put(leftSprite, (long) this.leftGraphic);
                }

            }
            return leftSprite;
        }
    }

    public Sprite getRightGraphic() {
        if (this.rightGraphic < 0) {
            return null;
        } else {
            Sprite rightGraphic = (Sprite) spritesCached.get(this.rightGraphic);
            if (rightGraphic == null) {
                rightGraphic = Sprite.getSprite(this.rightGraphic, 0);
                if (rightGraphic != null) {
                    spritesCached.put(rightGraphic, this.rightGraphic);
                }

            }
            return rightGraphic;
        }
    }


    public RSFontOSRS getFont() {
        if (this.fontId == -1) {
            return null;
        } else {
            RSFontOSRS font = (RSFontOSRS) fontsCached.get(this.fontId);
            if (font == null) {
                font = FontLoader.loadFont(Js5List.sprites, Js5List.fonts, this.fontId, 0);
                if (font != null) {
                    fontsCached.put(font, this.fontId);
                }

            }
            return font;
        }
    }


    @Override
    public void setFadeat(int fadeat) {
        this.fadeat = fadeat;
    }

    @Override
    public void setAnimationEndY(int animationEndY) {
        this.animationEndY = animationEndY;
    }

    @Override
    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }
}
