package com.client.definitions;

import com.client.Buffer;
import com.client.Sprite;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.util.EnumUtils;
import com.client.util.HorizontalAlignment;
import com.client.util.VerticalAlignment;
import net.runelite.rs.api.RSSpritePixels;
import net.runelite.rs.api.RSWorldMapElement;

public final class AreaDefinition extends DualNode implements RSWorldMapElement {

    public static AreaDefinition[] definitions;

    public static EvictingDualNodeHashTable cachedSprites = new EvictingDualNodeHashTable(256);

    public final int objectId;

    public int sprite1 = -1;

    int sprite2 = -1;

    public String name;

    public int fontColor;

    public int textSize = 0;

    public boolean field1936 = true;

    public boolean field1940 = false;

    public String[] options = new String[5];

    public String menuTargetName;

    int[] field1933;

    int field1941 = Integer.MAX_VALUE;

    int field1942 = Integer.MAX_VALUE;

    int field1943 = Integer.MIN_VALUE;

    int field1937 = Integer.MIN_VALUE;

    public HorizontalAlignment horizontalAlignment;

    public VerticalAlignment verticalAlignment;

    int[] field1930;

    byte[] field1948;

    public int category;

    public AreaDefinition(int objectID) {
        this.horizontalAlignment = HorizontalAlignment.HorizontalAlignment_centered;
        this.verticalAlignment = VerticalAlignment.VerticalAlignment_centered;
        this.category = -1;
        this.objectId = objectID;
    }

    public static AreaDefinition lookup(int objectID) {
        return objectID >= 0 && objectID < AreaDefinition.definitions.length && AreaDefinition.definitions[objectID] != null ? AreaDefinition.definitions[objectID] : new AreaDefinition(objectID);
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
            sprite1 = buffer.readNullableLargeSmart();
        } else if (opcode == 2) {
            sprite2 = buffer.readNullableLargeSmart();
        } else if (opcode == 3) {
            name = buffer.readNewString();
        } else if (opcode == 4) {
            fontColor = buffer.readMedium();
        } else if (opcode == 5) {
            buffer.readMedium();
        } else if (opcode == 6) {
            textSize = buffer.readUnsignedByte();
        } else {
            int size;
            if (opcode == 7) {
                size = buffer.readUnsignedByte();
                if ((size & 1) == 0) {
                    field1936 = false;
                }

                if ((size & 2) == 2) {
                    field1940 = true;
                }
            } else if (opcode == 8) {
                buffer.readUnsignedByte();
            } else if (opcode >= 10 && opcode <= 14) {
                options[opcode - 10] = buffer.readStringCp1252NullTerminated();
            } else if (opcode == 15) {
                size = buffer.readUnsignedByte();
                field1933 = new int[size * 2];

                int index;
                for(index = 0; index < size * 2; ++index) {
                    field1933[index] = buffer.readShort();
                }

                buffer.readInt();
                index = buffer.readUnsignedByte();
                field1930 = new int[index];

                int var5;
                for(var5 = 0; var5 < field1930.length; ++var5) {
                    field1930[var5] = buffer.readInt();
                }

                field1948 = new byte[size];

                for(var5 = 0; var5 < size; ++var5) {
                    field1948[var5] = buffer.readSignedByte();
                }
            } else if (opcode != 16) {
                if (opcode == 17) {
                    menuTargetName = buffer.readStringCp1252NullTerminated();
                } else if (opcode == 18) {
                    buffer.readNullableLargeSmart();
                } else if (opcode == 19) {
                    category = buffer.readUShort();
                } else if (opcode == 21) {
                    buffer.readInt();
                } else if (opcode == 22) {
                    buffer.readInt();
                } else if (opcode == 23) {
                    buffer.readUnsignedByte();
                    buffer.readUnsignedByte();
                    buffer.readUnsignedByte();
                } else if (opcode == 24) {
                    buffer.readShort();
                    buffer.readShort();
                } else if (opcode == 25) {
                    buffer.readNullableLargeSmart();
                } else if (opcode == 28) {
                    buffer.readUnsignedByte();
                } else if (opcode == 29) {
                    HorizontalAlignment[] horizontalAlignment = new HorizontalAlignment[]{HorizontalAlignment.field2010, HorizontalAlignment.HorizontalAlignment_centered, HorizontalAlignment.field2008};
                    this.horizontalAlignment = (HorizontalAlignment) EnumUtils.findEnumerated(horizontalAlignment, buffer.readUnsignedByte());
                } else if (opcode == 30) {
                    VerticalAlignment[] verticalAlignment = new VerticalAlignment[]{VerticalAlignment.field2073, VerticalAlignment.field2072, VerticalAlignment.VerticalAlignment_centered};
                    this.verticalAlignment = (VerticalAlignment) EnumUtils.findEnumerated(verticalAlignment, buffer.readUnsignedByte());
                }
            }
        }

    }


    public void init() {
        if (this.field1933 != null) {
            for(int var1 = 0; var1 < this.field1933.length; var1 += 2) {
                if (this.field1933[var1] < this.field1941) {
                    this.field1941 = this.field1933[var1];
                } else if (this.field1933[var1] > this.field1943) {
                    this.field1943 = this.field1933[var1];
                }

                if (this.field1933[var1 + 1] < this.field1942) {
                    this.field1942 = this.field1933[var1 + 1];
                } else if (this.field1933[var1 + 1] > this.field1937) {
                    this.field1937 = this.field1933[var1 + 1];
                }
            }
        }

    }

    public Sprite getIconSprite() {
        int spriteID = this.sprite1;
        return this.getIconSprite(spriteID);
    }


    public Sprite getIconSprite(int spriteID) {
        if (spriteID < 0) {
            return null;
        }

        Sprite sprite = (Sprite) cachedSprites.get(spriteID);
        if (sprite != null) {
            return sprite;
        }

        sprite = Sprite.getSprite(spriteID, 0);
        if (sprite != null) {
            cachedSprites.put(sprite, spriteID);
        }
        return sprite;
    }

    public int getObjectId() {
        return this.objectId;
    }


    @Override
    public RSSpritePixels getMapIcon(boolean id) {
        return getIconSprite();
    }
}