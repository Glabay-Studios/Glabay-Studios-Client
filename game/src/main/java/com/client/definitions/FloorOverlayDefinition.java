package com.client.definitions;

import com.client.Buffer;
import com.client.collection.node.DualNode;
import com.client.collection.table.EvictingDualNodeHashTable;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import lombok.Data;
import net.runelite.rs.api.RSFloorOverlayDefinition;

@Data
public class FloorOverlayDefinition extends DualNode implements RSFloorOverlayDefinition {

    public static EvictingDualNodeHashTable floorCache = new EvictingDualNodeHashTable(64);

    public int primaryRgb = 0;

    public int id;
    public int texture = -1;

    public boolean hideUnderlay = true;

    public int secondaryRgb = -1;

    public int hue;

    public int saturation;

    public int lightness;

    public int secondaryHue;

    public int secondarySaturation;

    public int secondaryLightness;

    public static FloorOverlayDefinition lookup(int floorID) {
        FloorOverlayDefinition floorDef = (FloorOverlayDefinition)FloorOverlayDefinition.floorCache.get(floorID);
        if (floorDef == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.OVERLAY, floorID);
            floorDef = new FloorOverlayDefinition();
            if (data != null) {
                floorDef.decode(new Buffer(data));
            }
            floorDef.postDecode();
            floorDef.id = floorID;
            floorCache.put(floorDef, floorID);
        }
        return floorDef;
    }

    public void postDecode() {
        if (this.secondaryRgb != -1) {
            this.setHsl(this.secondaryRgb);
            this.secondaryHue = this.hue;
            this.secondarySaturation = this.saturation;
            this.secondaryLightness = this.lightness;
        }

        this.setHsl(this.primaryRgb);
    }

    public void decode(Buffer buffer) {
        while(true) {
            int opcodes = buffer.readUnsignedByte();
            if (opcodes == 0) {
                return;
            }

            this.decodeNext(buffer, opcodes);
        }
    }


    void decodeNext(Buffer buffer, int opcode) {
        if (opcode == 1) {
            this.primaryRgb = buffer.readMedium();
        } else if (opcode == 2) {
            this.texture = buffer.readUnsignedByte();
        } else if (opcode == 5) {
            this.hideUnderlay = false;
        } else if (opcode == 7) {
            this.secondaryRgb = buffer.readMedium();
        }
    }

    void setHsl(int rgbValue) {
        double red = (double)(rgbValue >> 16 & 255) / 256.0;
        double green = (double)(rgbValue >> 8 & 255) / 256.0;
        double blue = (double)(rgbValue & 255) / 256.0;
        double minColorValue = red;
        if (green < red) {
            minColorValue = green;
        }

        if (blue < minColorValue) {
            minColorValue = blue;
        }

        double maxColorValue = red;
        if (green > red) {
            maxColorValue = green;
        }

        if (blue > maxColorValue) {
            maxColorValue = blue;
        }

        double hueValue = 0.0;
        double saturationValue = 0.0;
        double lightnessValue = (minColorValue + maxColorValue) / 2.0;
        if (minColorValue != maxColorValue) {
            if (lightnessValue < 0.5) {
                saturationValue = (maxColorValue - minColorValue) / (minColorValue + maxColorValue);
            }

            if (lightnessValue >= 0.5) {
                saturationValue = (maxColorValue - minColorValue) / (2.0 - maxColorValue - minColorValue);
            }

            if (red == maxColorValue) {
                hueValue = (green - blue) / (maxColorValue - minColorValue);
            } else if (maxColorValue == green) {
                hueValue = 2.0 + (blue - red) / (maxColorValue - minColorValue);
            } else if (blue == maxColorValue) {
                hueValue = (red - green) / (maxColorValue - minColorValue) + 4.0;
            }
        }

        hueValue /= 6.0;
        this.hue = (int)(hueValue * 256.0);
        this.saturation = (int)(saturationValue * 256.0);
        this.lightness = (int)(lightnessValue * 256.0);
        if (this.saturation < 0) {
            this.saturation = 0;
        } else if (this.saturation > 255) {
            this.saturation = 255;
        }

        if (this.lightness < 0) {
            this.lightness = 0;
        } else if (this.lightness > 255) {
            this.lightness = 255;
        }

    }

}
