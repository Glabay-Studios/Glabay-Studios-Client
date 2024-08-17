package com.client.graphics.interfaces.impl;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;
import com.client.model.Spell;
import com.google.common.base.Preconditions;

public class Autocast extends RSInterface {

    private static final Autocast SINGLETON = new Autocast();
    public static final int AUTOCAST_MENU_ACTION_ID = 1755;
    public static final int BUTTON_START_INTERFACE_ID = 28_801;
    private static final Set<Spell> autocastable;
    private static final Sprite SPRITE_STANDARD = new Sprite("autocast");
    private static final Sprite SPRITE_DEFENSIVE = new Sprite("autocast_defensive");

    static {
        autocastable = Collections.unmodifiableSet(Arrays.stream(Spell.values()).filter(Spell::isAutocastable)
                .collect(Collectors.toSet()));
    }

    public static Autocast getSingleton() {
        return SINGLETON;
    }

    private boolean loaded = false;
    private List<RSInterface> autocasts = new ArrayList<>();

    private Autocast() {}

    public void load() {
        if (!loaded) {
            loaded = true;
            int interfaceId = BUTTON_START_INTERFACE_ID;
            for (Spell autocast : autocastable) {
                RSInterface spell = get(autocast.getId());
                RSInterface container = get(autocast.getSpellBook().getInterfaceId());
                Preconditions.checkState(container != null);
                Preconditions.checkState(spell != null);
                int spellChildIndex = RSInterface.getIndexOfChild(container, autocast.getId());
                Preconditions.checkState(spellChildIndex != -1);
                int x = container.childX[spellChildIndex];
                int y = container.childY[spellChildIndex];
                RSInterface.expandChildren(3, container);

                // Status sprite
                addSprite(interfaceId, SPRITE_STANDARD);
                Point spriteOffset = getSpriteOffset(autocast);
                container.child(container.children.length - 3, interfaceId, x + spriteOffset.x, y + spriteOffset.y);
                get(interfaceId).sprite1 = null;
                get(interfaceId).active = false;
                interfaceId++;

                for (int i = 2; i > 0; i--) {
                    // Autocast button
                    autocast(interfaceId, autocast.toString(), spell.width, spell.height, autocast.getId(), i == 2, spell);
                    container.child(container.children.length - i, interfaceId, x, y);
                    interfaceId++;
                }
            }
        }
    }

    private void autocast(int interfaceId, String spellName, int width, int height, int spellId, boolean defensive, RSInterface spellInterface) {
        RSInterface autocastChild = addInterface(interfaceId);
        autocastChild.id = interfaceId;
        autocastChild.parentID = interfaceId;
        autocastChild.type = 5;
        autocastChild.atActionType = RSInterface.AT_ACTION_TYPE_AUTOCAST;
        autocastChild.contentType = 0;
        autocastChild.opacity = 0;
        autocastChild.hoverType = 52;
        autocastChild.width = width;
        autocastChild.height = height;
        autocastChild.tooltip = "Autocast " + (defensive ? "defensive " : "") + "@gre@" + spellName;
        autocastChild.autocastSpellId = spellId;
        autocastChild.autocastDefensive = defensive;
        autocasts.add(autocastChild);
        autocastChild.mOverInterToTrigger = spellInterface.mOverInterToTrigger;
    }

    private int getAutocastSpriteInterfaceId(RSInterface rsInterface) {
        return rsInterface.autocastDefensive ? rsInterface.id - 1 : rsInterface.id - 2;
    }

    public void onConfigChanged(int config, int value) {
        if (config == Configs.AUTOCAST_DEFENCE_CONFIG) {
            for (RSInterface autocast : autocasts) {
                get(getAutocastSpriteInterfaceId(autocast)).sprite2 = value == 1 ? SPRITE_DEFENSIVE : SPRITE_STANDARD;
            }
        } else  if (config == Configs.AUTOCAST_SPELL) {
            boolean defensive = Client.instance.variousSettings[Configs.AUTOCAST_DEFENCE_CONFIG] == 1;
            for (RSInterface autocast : autocasts) {
                RSInterface rsInterface = get(getAutocastSpriteInterfaceId(autocast));
                rsInterface.active = autocast.autocastSpellId == value;
            }
        }
    }

    private Point getSpriteOffset(Spell spell) {
        switch (spell) {
            case WIND_STRIKE:
            case WATER_STRIKE:
            case EARTH_STRIKE:
            case FIRE_STRIKE:
            case WIND_BOLT:
            case WATER_BOLT:
            case EARTH_BOLT:
            case FIRE_BOLT:
            case WIND_BLAST:
            case WATER_BAST:
            case EARTH_BLAST:
            case FIRE_BLAST:
            case WIND_WAVE:
            case WATER_WAVE:
            case EARTH_WAVE:
            case FIRE_WAVE:
            case SMOKE_RUSH:
            case SHADOW_RUSH:
            case BLOOD_RUSH:
            case ICE_RUSH:
            case SMOKE_BURST:
            case SHADOW_BURST:
            case BLOOD_BURST:
            case ICE_BURST:
            case SMOKE_BLITZ:
            case SHADOW_BLITZ:
            case BLOOD_BLITZ:
            case ICE_BLITZ:
            case SMOKE_BARRAGE:
            case SHADOW_BARRAGE:
            case BLOOD_BARRAGE:
            case ICE_BARRAGE:
                return new Point(-2, -2);
            case WIND_SURGE:
            case WATER_SURGE:
            case EARTH_SURGE:
            case FIRE_SURGE:
                return new Point(0, 0);

        }
        return new Point(0, 0);
    }

}
