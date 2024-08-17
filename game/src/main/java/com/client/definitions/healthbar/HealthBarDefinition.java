package com.client.definitions.healthbar;

import com.client.Buffer;
import com.client.Client;
import com.client.Sprite;
import com.client.collection.EvictingDualNodeHashTable;
import com.client.collection.node.DualNode;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;
import net.runelite.api.events.PostHealthBar;
import net.runelite.rs.api.RSHealthBarDefinition;
import net.runelite.rs.api.RSSpritePixels;

public class HealthBarDefinition extends DualNode implements RSHealthBarDefinition {

    public static EvictingDualNodeHashTable cached = new EvictingDualNodeHashTable(64);

    public static EvictingDualNodeHashTable cachedSprites = new EvictingDualNodeHashTable(64);

    public int field1994;

    public int int1;

    public int int2;

    public int int3;

    public int field1998;

    public int int5;

    int frontSpriteID;

    int backSpriteID;

    public int width;

    public int widthPadding;

    public HealthBarDefinition() {
        this.int1 = 255;
        this.int2 = 255;
        this.int3 = -1;
        this.field1998 = 1;
        this.int5 = 70;
        this.frontSpriteID = -1;
        this.backSpriteID = -1;
        this.width = 30;
        this.widthPadding = 0;
    }

    public static HealthBarDefinition lookup(int barId) {
        HealthBarDefinition definition = (HealthBarDefinition) cached.get((long) barId);

        if (definition != null) {
            return definition;
        }

        byte[] payload = Js5List.configs.takeFile(Js5ConfigType.HEALTHBAR, barId);
        HealthBarDefinition newBarDef = new HealthBarDefinition();
        newBarDef.field1994 = barId;

        if (payload != null) {
            newBarDef.decode(new Buffer(payload));
        }

        PostHealthBar postHealthBar = new PostHealthBar();
        postHealthBar.setHealthBar(newBarDef);
        Client.instance.getCallbacks().post(postHealthBar);

        cached.put(newBarDef, barId);
        return newBarDef;
    }

    public void decode(Buffer buffer) {
        while (true) {
            int opcpde = buffer.readUnsignedByte();
            if (opcpde == 0) {
                return;
            }

            this.decodeNext(buffer, opcpde);
        }
    }


    void decodeNext(Buffer payload, int opcode) {
        if (opcode == 1) {
            payload.readUnsignedShort();
        } else if (opcode == 2) {
            this.int1 = payload.readUnsignedByte();
        } else if (opcode == 3) {
            this.int2 = payload.readUnsignedByte();
        } else if (opcode == 4) {
            this.int3 = 0;
        } else if (opcode == 5) {
            this.int5 = payload.readUnsignedShort();
        } else if (opcode == 6) {
            payload.readUnsignedByte();
        } else if (opcode == 7) {
            this.frontSpriteID = payload.readNullableLargeSmart();
        } else if (opcode == 8) {
            this.backSpriteID = payload.readNullableLargeSmart();
        } else if (opcode == 11) {
            this.int3 = payload.readUnsignedShort();
        } else if (opcode == 14) {
            this.width = payload.readUnsignedByte();
        } else if (opcode == 15) {
            this.widthPadding = payload.readUnsignedByte();
        }
    }


    public Sprite getFrontSprite() {
        if (this.frontSpriteID < 0) {
            return null;
        } else {
            Sprite frontSprite = (Sprite) cachedSprites.get(this.frontSpriteID);
            if (frontSprite == null) {
                frontSprite = Sprite.getSprite(this.frontSpriteID, 0);
                if (frontSprite != null) {
                    cachedSprites.put(frontSprite, this.frontSpriteID);
                }

            }
            return frontSprite;
        }
    }

    public Sprite getBackSprite() {
        if (this.backSpriteID < 0) {
            return null;
        } else {
            Sprite backSprite = (Sprite) cachedSprites.get(this.backSpriteID);
            if (backSprite == null) {
                backSprite = Sprite.getSprite(this.backSpriteID, 0);
                if (backSprite != null) {
                    cachedSprites.put(backSprite, this.backSpriteID);
                }
            }
            return backSprite;
        }
    }


    @Override
    public int getHealthScale() {
        return width;
    }

    @Override
    public int getHealthBarFrontSpriteId() {
        return frontSpriteID;
    }

    @Override
    public RSSpritePixels getHealthBarFrontSprite() {
        return getFrontSprite();
    }

    @Override
    public RSSpritePixels getHealthBarBackSprite() {
        return getBackSprite();
    }

    @Override
    public void setPadding(int padding) {
        this.widthPadding = padding;
    }
}
