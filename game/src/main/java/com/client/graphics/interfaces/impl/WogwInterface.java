package com.client.graphics.interfaces.impl;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;

public class WogwInterface extends RSInterface {

    public static final int INTERFACE_ID = 22931;
    private static final WogwInterface INSTANCE = new WogwInterface();
    private static final Sprite BG = new Sprite("wogw_interface/BACKGROUND");
    private static final Sprite BUTTON = new Sprite("wogw_interface/BUTTON");
    private static final Sprite BUTTON_HIGHLIGHT = new Sprite("wogw_interface/BUTTON_HIGHLIGHTED");
    private static final Sprite CONTRIBUTE_BUTTON = new Sprite("wogw_interface/CONTRIBUTE_BUTTON");

    public static WogwInterface get() {
        return INSTANCE;
    }

    private WogwInterface() {
    }

    private enum Button {
        EXPERIENCE("Experience (x1.5)", new Sprite("wogw_interface/STATBARS")),
        PEST("Pest Control (+5)", new Sprite("wogw_interface/VOID")),
        DOUBLE_DROPS("+20% Drop Rate", new Sprite("wogw_interface/COINS")),
        ;

        private final String name;
        private final Sprite icon;

        Button(String name, Sprite icon) {
            this.name = name;
            this.icon = icon;
        }
    }

    public void load() {
        int interfaceId = INTERFACE_ID;
        int childId = 0;
        RSInterface inter = addInterface(interfaceId++);
        inter.totalChildren(12 + (Button.values().length * 4));

        addSprite(interfaceId, BG);
        inter.child(childId++, interfaceId++, 104, 54);

        addText(interfaceId, 2, RSInterface.DEFAULT_TEXT_COLOR, true, "Well of Goodwill");
        inter.child(childId++, interfaceId++, 264, 64);

        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL, 394, 64);
        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL_HOVER, 394, 64);

        int leftTextX = 176;

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Top Contributor");
        inter.child(childId++, interfaceId++, leftTextX, 118);

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Top Contributor Name");
        inter.child(childId++, interfaceId++, leftTextX, 136);

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Recent Contributors");
        inter.child(childId++, interfaceId++, leftTextX, 154);

        addStringContainer(interfaceId, 0, Client.instance.newSmallFont, true, RSInterface.DEFAULT_TEXT_COLOR, true, 16, "Player Username");
        inter.child(childId++, interfaceId++, leftTextX, 184);

        // Headers
        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Contributors");
        inter.child(childId++, interfaceId++, leftTextX, 96);

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Bonus Selection");
        inter.child(childId++, interfaceId++, 327, 96);

        // Contribute button
        int contributeX = 219;
        int contributeY = 252;
        addButton(interfaceId, CONTRIBUTE_BUTTON, "Contribute", 1);
        inter.child(childId++, interfaceId++, contributeX, contributeY);

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Contribute");
        inter.child(childId++, interfaceId++, contributeX + 44, contributeY + 6);

        int x = 244;
        int y = 116;
        for (int index = 0; index < Button.values().length; index++) {
            Button button = Button.values()[index];

            addConfigButton(interfaceId, BUTTON, BUTTON_HIGHLIGHT, "Select bonus", index, Configs.WOGW_BONUS_BUTTON_SELECTION, 4);
            get(interfaceId).ignoreConfigClicking = true;
            inter.child(childId++, interfaceId++, x, y);

            addSprite(interfaceId, button.icon);
            inter.child(childId++, interfaceId++, x + 140 - (button.icon.myWidth / 2), y + 18 - (button.icon.myHeight / 2));

            addText(interfaceId, 0, 0xFFFFFFFF, true, button.name);
            inter.child(childId++, interfaceId++, x + 70, y + 7);

            addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "0/50000000m");
            inter.child(childId++, interfaceId++, x + 70, y + 18);
            y += 42;
        }

        inter.setNewButtonClicking();
    }
}
