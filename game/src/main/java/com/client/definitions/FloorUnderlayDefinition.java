package com.client.definitions;

import com.client.Buffer;
import com.client.collection.node.DualNode;
import com.client.collection.table.EvictingDualNodeHashTable;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import lombok.Data;
import net.runelite.rs.api.RSFloorUnderlayDefinition;

@Data
public class FloorUnderlayDefinition extends DualNode implements RSFloorUnderlayDefinition {

    public static EvictingDualNodeHashTable floorCache = new EvictingDualNodeHashTable(64);

    int rgb = 0;
    public int id;
    public int hue;

    public int saturation;

    public int lightness;

    public int hueMultiplier;

    void postDecode() {
        this.setHsl(this.rgb);
    }


    void decode(Buffer buffer) {
        while(true) {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }

            this.decodeNext(buffer, opcode);
        }
    }


    void decodeNext(Buffer buffer, int opcode) {
        if (opcode == 1) {
            this.rgb = buffer.readMedium();
        }
    }

    public static FloorUnderlayDefinition lookup(int floorID) {
        FloorUnderlayDefinition floorDef = (FloorUnderlayDefinition)FloorUnderlayDefinition.floorCache.get(floorID);
        if (floorDef == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.UNDERLAY, floorID);
            floorDef = new FloorUnderlayDefinition();
            if (data != null) {
                floorDef.decode(new Buffer(data));
            }

            floorDef.postDecode();
            floorDef.id = floorID;
            floorCache.put(floorDef, floorID);
        }
        return floorDef;
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
        double lightnessValue = (maxColorValue + minColorValue) / 2.0;
        if (minColorValue != maxColorValue) {
            if (lightnessValue < 0.5) {
                saturationValue = (maxColorValue - minColorValue) / (minColorValue + maxColorValue);
            }

            if (lightnessValue >= 0.5) {
                saturationValue = (maxColorValue - minColorValue) / (2.0 - maxColorValue - minColorValue);
            }

            if (red == maxColorValue) {
                hueValue = (green - blue) / (maxColorValue - minColorValue);
            } else if (green == maxColorValue) {
                hueValue = 2.0 + (blue - red) / (maxColorValue - minColorValue);
            } else if (blue == maxColorValue) {
                hueValue = 4.0 + (red - green) / (maxColorValue - minColorValue);
            }
        }

        hueValue /= 6.0;
        this.saturation = (int)(256.0 * saturationValue);
        this.lightness = (int)(256.0 * lightnessValue);
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

        if (lightnessValue > 0.5) {
            this.hueMultiplier = (int)(512.0 * saturationValue * (1.0 - lightnessValue));
        } else {
            this.hueMultiplier = (int)(512.0 * lightnessValue * saturationValue);
        }

        if (this.hueMultiplier < 1) {
            this.hueMultiplier = 1;
        }

        this.hue = (int)((double)this.hueMultiplier * hueValue);
    }

}
