package com.client.draw;


import com.client.Buffer;
import com.client.Sprite;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;

public final class ImageCache extends DualNode {

    public int id;
    public int width = -1;
    public int height = -1;
    public int offsetX = 0;
    public int offsetY = 0;
    public String name = "null";
    public byte[] pixels;
    public Sprite sprite;

    public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(100);
    public static EvictingDualNodeHashTable cachedSizes = new EvictingDualNodeHashTable(100);

    public static Sprite lookup(int id) {
        ImageCache image = (ImageCache) ImageCache.cached.get(id);
        if (image == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.CUSTOM_SPRITES, id);
            image = new ImageCache();
            image.id = id;
            if (data != null) {
                image.decode(new Buffer(data),true);
            } else {
                System.out.println("Missing Sprite: " + id);
                return Sprite.EMPTY_SPRITE;
            }
            cached.put(image, id);
        }
        return image.sprite;
    }

    public static ImageCache metaData(int id) {
        ImageCache image = (ImageCache) ImageCache.cached.get(id);
        if (image == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.CUSTOM_SPRITES, id);
            image = new ImageCache();
            image.id = id;
            if (data != null) {
                image.decode(new Buffer(data),true);
            }
            cached.put(image, id);
        }
        return image;
    }

    public static ImageCache lookupMetaData(int id) {
        ImageCache image = (ImageCache) ImageCache.cachedSizes.get(id);
        if (image == null) {
            byte[] data = Js5List.configs.takeFile(Js5ConfigType.CUSTOM_SPRITES, id);
            image = new ImageCache();
            image.id = id;
            if (data != null) {
                image.decode(new Buffer(data),false);
            }
            cachedSizes.put(image, id);
        }
        return image;
    }

    void decode(Buffer var1, boolean full) {
        while(true) {
            int var2 = var1.readUnsignedByte();
            if (var2 == 0) {
                return;
            }

            this.decodeNext(var1, var2, full);
        }
    }

    void decodeNext(Buffer buffer, int opcode, boolean full) {
        if (opcode == 1) {
            width = buffer.readShort();
        } else if (opcode == 2) {
            height = buffer.readShort();
        } else if (opcode == 3) {
            name = buffer.readStringCp1252NullTerminated();
        } else if (opcode == 4) {
            offsetX = buffer.readShort();
        } else if (opcode == 5) {
            offsetY = buffer.readShort();
        } else if (opcode == 6 && full) {
            int size = buffer.readInt();
            pixels = new byte[size];
            for (int index = 0; index < size; index++) {
                pixels[index] = buffer.readByte();
            }
            sprite = new Sprite(width,height,offsetX,offsetY,pixels);
        }
    }

    public static Sprite get(int id) {
        return lookup(id);
    }

    public static Sprite getLazy(int id) {
        return lookup(2240 + id);
    }

    public static int getWidth(int id) {
        return lookupMetaData(id).width;
    }

    public static int getHeight(int id) {
        return lookupMetaData(id).height;
    }

}