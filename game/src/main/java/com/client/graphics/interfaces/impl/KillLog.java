package com.client.graphics.interfaces.impl;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;

public class KillLog extends RSInterface {

    private static final KillLog INSTANCE = new KillLog();

    public static KillLog get() {
        return INSTANCE;
    }

    private static final int INTERFACE_ID = 24430;
    private static final Sprite BG = new Sprite("kill_log/bg");
    private static final Sprite DIVIDER = new Sprite("kill_log/divider");
    private static final Sprite TOP_DIVIDER = new Sprite("kill_log/top_divider");
    private static final Sprite RESTART = new Sprite("kill_log/restart");
    private static final Sprite ROW = new Sprite("kill_log/row");

    private KillLog() {}

    private static int KILL_X;
    private static int BEST_TIME_X;

    public void load() {
        KILL_X = 220;
        BEST_TIME_X = 320;
        int interfaceId = INTERFACE_ID;
        int childId = 0;

        RSInterface inter = addInterface(interfaceId++);
        inter.totalChildren(7);

        addSprite(interfaceId, BG);
        inter.child(childId++, interfaceId++, 16, 18);

        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL, 462, 27);
        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL_HOVER, 462, 27);

        addText(interfaceId, "Monster Kill Log", Interfaces.defaultTextDrawingAreas, 2, RSInterface.DEFAULT_TEXT_COLOR, true, true);
        inter.child(childId++, interfaceId++, 250, 26);

        int container = interfaceId;
        inter.child(childId++, container, 26, 54);
        interfaceId = container(interfaceId);

        addSprite(interfaceId, DIVIDER);
        inter.child(childId++, interfaceId, KILL_X + 20, 58);

        addSprite(interfaceId, DIVIDER);
        inter.child(childId++, interfaceId, BEST_TIME_X + 20, 58);

        inter.offsetChildrenPositions(8, 8);
    }

    private int container(int interfaceId) {
        RSInterface container = addInterface(interfaceId++);
        container.width = 436;
        container.height = 248;
        container.scrollMax = 6000;
        int lines = 100;
        container.totalChildren(7 + (lines / 2));
        int childId = 0;

        // Top divider
        addSprite(interfaceId, TOP_DIVIDER);
        container.child(childId++, interfaceId++, 0, 16);

        // Monster names
        addText(interfaceId, "Monster", RSInterface.defaultTextDrawingAreas, 2, RSInterface.DEFAULT_TEXT_COLOR, false, true);
        container.child(childId++, interfaceId++, 0, 0);

        addStringContainer(interfaceId, container.id, Client.instance.newRegularFont, false, RSInterface.DEFAULT_TEXT_COLOR, true, 18, "Commander Zilyana");
        get(interfaceId).invAutoScrollHeightOffset = 18;
        container.child(childId++, interfaceId++, 0, 32);

        // Kills
        addText(interfaceId, "Kills", RSInterface.defaultTextDrawingAreas, 2, RSInterface.DEFAULT_TEXT_COLOR, false, true);
        container.child(childId++, interfaceId++, KILL_X, 0);

        addStringContainer(interfaceId, container.id, Client.instance.newRegularFont, false, RSInterface.DEFAULT_TEXT_COLOR, true, 18, "100");
        get(interfaceId).invAutoScrollHeightOffset = 18;
        container.child(childId++, interfaceId++, KILL_X, 32);

        // Best kill time
        addText(interfaceId, "Best Kill Time", RSInterface.defaultTextDrawingAreas, 2, RSInterface.DEFAULT_TEXT_COLOR, false, true);
        container.child(childId++, interfaceId++, BEST_TIME_X, 0);

        addStringContainer(interfaceId, container.id, Client.instance.newRegularFont, false, RSInterface.DEFAULT_TEXT_COLOR, true, 18, "15:00");
        get(interfaceId).invAutoScrollHeightOffset = 18;
        container.child(childId++, interfaceId++, BEST_TIME_X, 32);

        addSpriteTransparent(interfaceId, ROW, 16);
        for (int i = 0; i < 100; i += 2) {
            container.child(childId++, interfaceId, 0, 18 + (i * 18));
        }
        interfaceId++;

        return interfaceId;
    }

}
