package com.client.graphics.interfaces.impl;

import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;

public class MonsterDropViewer extends RSInterface {

    public static final int CONFIG_ID = 1356;
    private static final int CONTAINER_ID = 34000;
    private static final int EMPTY = 21405;

    public static void onConfigChanged(int config, int value) {
        if (config == CONFIG_ID) {
            RSInterface container = RSInterface.interfaceCache[CONTAINER_ID];
            for (int index = 0; index < 80; index++) {
               // main.child(240 + i, 34003, 175, 2 + yy);
               // main.child(320 + i, 34004, 234, 2 + yy);
               // main.child(400 + i, 34005, 293, 2 + yy);
                if (index >= value) {
                    container.children[240 + index] = EMPTY;
                    container.children[320 + index] = EMPTY;
                    container.children[400 + index] = EMPTY;
                } else {
                    container.children[240 + index] = 34003;
                    container.children[320 + index] = 34004;
                    container.children[400 + index] = 34005;
                }
            }
        }
    }

    public void OsDropViewer(TextDrawingArea[] tda) {
        addText(EMPTY, "", tda, 1, 0xff9040, false, true);

        RSInterface tab = addInterface(39500);
        String dir = "Interfaces/DropViewer/SPRITE";
        addSprite(39501, 0, dir);
        addHoverButton(39502, dir, 1, 21, 21, "Close", 0, 39503, 1);
        addHoveredButton(39503, dir, 2, 21, 21, 39504);
        addText(43005, "Monster Drop Viewer", tda, 2, 0xFFA500, true, true);
        addText(43110, "Health: @whi@0", tda, 1, 0xff9040, false, true);// overrides
        addText(43111, "Combat Level: @whi@0", tda, 1, 0xff9040, false, true);
        addText(43112, "Max Hit: @whi@0", tda, 1, 0xff9040, false, true);
        addText(43113, "Aggressive: @whi@false", tda, 1, 0xff9040, false, true);

        addInputField(39806, 30, 0xFF981F, "NPC/Item Name..", 130, 28, false, false, "[A-Za-z0-9 .,]");
        //addText(42522, "Find npc/item drops", drawingArea, 0, 0xFF981F, true, false);
        int x = 7, y = 7;
        tab.totalChildren(11);
        tab.child(0, 39501, 0 + x, 0 + y);
        tab.child(1, 39502, 472 + x, 7 + y);
        tab.child(2, 39503, 472 + x, 7 + y);
        tab.child(3, 43005, 250 + x, 11 + y);
        tab.child(4, 39806, 8+x, 37+y);
        tab.child(5, 39507, 6 + x, 66 + y);
        tab.child(6, CONTAINER_ID, 150 + x, 86 + y);
        tab.child(7, 43110, 250 + x, 40 + y);
        tab.child(8, 43111, 250 + x, 60 + y);
        tab.child(9, 43112, 360 + x, 40 + y);
        tab.child(10, 43113, 360 + x, 60 + y);

        RSInterface results = addInterface(39507);
        results.width = 122;
        results.height = 250;
        results.scrollMax = 251;
        results.totalChildren(200);
        for (int j = 0; j < 200; j++) {
            addClickableText(33008 + j, "", "View Drops", tda, 0, 0xff0000, false, true, 110);
            results.child(j, 33008 + j, 6, 8 + (j * 14));
        }

        RSInterface main = addInterface(CONTAINER_ID);
        main.totalChildren(720);
        main.width = 328;
        main.height = 230;
        main.scrollMax = 2560;
        addSprite(34001, 3, dir);
        addSprite(34002, 4, dir);
        for (int i = 0; i < 40; i++) {
            main.child(i, 34001, 0, (i * 64));
            main.child(i + 40, 34002, 0, 32 + (i * 64));
        }
        addText(34003, "Amount:", tda, 0, 0xff9040, true, true);
        addText(34004, "Rarity:", tda, 0, 0xff9040, true, true);
        addText(34005, "Chance:", tda, 0, 0xff9040, true, true);
        for (int i = 0; i < 80; i++) {
            itemGroup(34010 + i, 1, 1, 1, 1, false, false);
            interfaceCache[34010 + i].inventoryItemId[0] = 14485;
            interfaceCache[34010 + i].inventoryAmounts[0] = 1;
            addText(34100 + i, "Item Name", tda, 1, 0xFFA500, false, true);
            addText(34200 + i, "1-50", tda, 0, 0xffffff, true, true);
            addText(34300 + i, "Common", tda, 0, 0xffffff, true, true);
            addText(34400 + i, "1/200", tda, 0, 0xffffff, true, true);
            int yy = (i * 32);
            main.child(80 + i, 34010 + i, 1, 0 + yy);
            main.child(160 + i, 34100 + i, 39, 6 + yy);
            main.child(240 + i, 34003, 175, 2 + yy);
            main.child(320 + i, 34004, 234, 2 + yy);
            main.child(400 + i, 34005, 293, 2 + yy);
            main.child(480 + i, 34200 + i, 175, 14 + yy);
            main.child(560 + i, 34300 + i, 234, 14 + yy);
            main.child(640 + i, 34400 + i, 293, 14 + yy);
        }

    }

}
