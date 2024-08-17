package com.client.graphics.interfaces.impl;

import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;

public class SlayerRewards extends RSInterface
{

    public static final int UNLOCK_INTERFACE_ID = 50_000;
    public static final int EXTEND_INTERFACE_ID  = 50_100;
    public static final int TASK_INTERFACE_ID  = 50_200;
    public static final int INFO_INTERFACE_ID  = 50_300;

    public static final int CLOSE_BUTTON_1 = 47_902;
    public static final int CLOSE_BUTTON_2 = 47_903;

    private static final int UNLOCK_BUTTON  = 49_998;
    private static final int EXTEND_BUTTON  = 49_997;
    private static final int BUY_BUTTON  = 49_996;
    private static final int TASK_BUTTON  = 49_995;

    private static final int POINTS_STRING_INTERFACE_ID  = 49_999;

    public static void initializeInterfaces(TextDrawingArea[] tda) {
        // Buttons at the top
        addHoverText(UNLOCK_BUTTON, "Unlock", "Unlock", tda, 0, 0xFF9900, true, true, 82, 16);
        addHoverText(EXTEND_BUTTON, "Extend", "Extend", tda, 0, 0xFF9900, true, true, 82, 16);
        addHoverText(BUY_BUTTON, "Buy", "Buy", tda, 0, 0xFF9900, true, true, 82, 16);
        addHoverText(TASK_BUTTON, "Task", "Task", tda, 0, 0xFF9900, true, true, 82, 16);

        // Slayer points string
        addText(POINTS_STRING_INTERFACE_ID, "1300", tda, 0, 0xFF9900, false, true);

        // Close button
        addHoverButton(CLOSE_BUTTON_1, "Slayer interface/CLOSE", 1, 21, 21,
                "Close", -1, CLOSE_BUTTON_1, 3);
        addHoveredButton2(CLOSE_BUTTON_2, "Slayer interface/CLOSE", 2, 21, 21, CLOSE_BUTTON_2 + 1);

        unlockInterface(tda);
        extendInterface(tda);
        taskInterface(tda);
        infoInterface(tda);
    }

    public static void infoInterface(TextDrawingArea[] tda) {

        RSInterface rsinterface = addInterface(INFO_INTERFACE_ID);
        addSprite(INFO_INTERFACE_ID + 1, 0, "Slayer interface/Info");
        addHoverText(INFO_INTERFACE_ID + 2, "Back", "Back", tda, 0, 0xFF9900, true, true, 83, 18);
        addHoverText(INFO_INTERFACE_ID + 3, "Confirm", "Confirm", tda, 0, 0xFF9900, true, true, 83, 18);

        rsinterface.totalChildren(15);

        int y = 104;

        int startInterfaceId = INFO_INTERFACE_ID + 4;
        int startChild = 5;

        for (int i = 0; i < 10; i++) {
            addText(startInterfaceId + i, "asdfas", tda, i == 0? 1 : 0, 0xFF9900, true, true);

            rsinterface.child(i + startChild, startInterfaceId + i, 246, y);
            y += 15;
        }

        rsinterface.child(0, INFO_INTERFACE_ID + 1, 12, 20);
        rsinterface.child(1, CLOSE_BUTTON_1, 472, 27);
        rsinterface.child(2, CLOSE_BUTTON_2, 472, 27);
        rsinterface.child(3, INFO_INTERFACE_ID + 2, 161 - 13, 265);
        rsinterface.child(4, INFO_INTERFACE_ID + 3, 279 - 13, 265);

    }

    // We don't use this interface so I'm commenting it out
    /*public static void buyInterface(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(47700);
        addSprite(47701, 0, "Slayer interface/Buy/buy");

        addToItemGroup(47706, 5, 4, 60, 40, true, "Info","Buy 1","Buy 5","Buy 10");

        rsinterface.totalChildren(9);
        rsinterface.child(0, 47701, 12, 20);
        rsinterface.child(1, 47902, 472, 27);
        rsinterface.child(2, 47903, 472, 27);
        rsinterface.child(3, POINTS_STRING_INTERFACE_ID, 455, 61);
        rsinterface.child(4, 47706, 58, 81);

        rsinterface.child(5, UNLOCK_BUTTON, 52-15-12, 61);
        rsinterface.child(6, EXTEND_BUTTON, 134-12-12, 61);
        rsinterface.child(7, BUY_BUTTON, 228-19-12, 61);
        rsinterface.child(8, TASK_BUTTON, 308-14-12, 61);
    }*/

    public static void taskInterface(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(TASK_INTERFACE_ID);
        addSprite(TASK_INTERFACE_ID + 1, 0, "Slayer interface/Task/task");

        addText(TASK_INTERFACE_ID + 2, "Black Demons x230", tda, 1, 0xFFFFFF, true, true);

        addHoverText(TASK_INTERFACE_ID + 3, "Cancel task", "Cancel", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 4, "Block task", "Block", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 5, "Unblock task", "Unblock task", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 6, "Unblock task", "Unblock task", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 7, "Unblock task", "Unblock task", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 8, "Unblock task", "Unblock task", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 9, "Unblock task", "Unblock task", tda, 0, 0xFF9900, true, true, 57);
        addHoverText(TASK_INTERFACE_ID + 10, "Unblock task", "Unblock task", tda, 0, 0xFF9900, true, true, 57);

        addText(TASK_INTERFACE_ID + 11, "asdf", tda, 1, 0xFFFFFF, true, true);
        addText(TASK_INTERFACE_ID + 12, "asdf", tda, 1, 0xFFFFFF, true, true);
        addText(TASK_INTERFACE_ID + 13, "asdfasdf", tda, 1, 0xFFFFFF, true, true);
        addText(TASK_INTERFACE_ID + 14, "asdf", tda, 1, 0xFFFFFF, true, true);
        addText(TASK_INTERFACE_ID + 15, "asdf", tda, 1, 0xFFFFFF, true, true);
        addText(TASK_INTERFACE_ID + 16, "asdf", tda, 1, 0xFFFFFF, true, true);


        rsinterface.totalChildren(23);
        rsinterface.child(0, TASK_INTERFACE_ID + 1, 12, 20);
        rsinterface.child(1, CLOSE_BUTTON_1, 472, 27);
        rsinterface.child(2, CLOSE_BUTTON_2, 472, 27);
        rsinterface.child(3, POINTS_STRING_INTERFACE_ID, 455, 61);

        rsinterface.child(4, UNLOCK_BUTTON, 52-15-12, 61);
        rsinterface.child(5, EXTEND_BUTTON, 134-12-12, 61);
        rsinterface.child(6, BUY_BUTTON, 228-19-12, 61);
        rsinterface.child(7, TASK_BUTTON, 308-14-12, 61);

        rsinterface.child(8, TASK_INTERFACE_ID + 2, 149, 144);
        rsinterface.child(9, TASK_INTERFACE_ID + 3, 310, 144);
        rsinterface.child(10, TASK_INTERFACE_ID + 4, 410, 144);
        rsinterface.child(11, TASK_INTERFACE_ID + 5, 384, 186);
        rsinterface.child(12, TASK_INTERFACE_ID + 6, 384, 208);
        rsinterface.child(13, TASK_INTERFACE_ID + 7, 384, 230);
        rsinterface.child(14, TASK_INTERFACE_ID + 8, 384, 252);
        rsinterface.child(15, TASK_INTERFACE_ID + 9, 384, 274);
        rsinterface.child(16, TASK_INTERFACE_ID + 10, 384, 296);
        rsinterface.child(17, TASK_INTERFACE_ID + 11, 254, 186);
        rsinterface.child(18, TASK_INTERFACE_ID + 12, 254, 208);
        rsinterface.child(19, TASK_INTERFACE_ID + 13, 254, 230);
        rsinterface.child(20, TASK_INTERFACE_ID + 14, 254, 252);
        rsinterface.child(21, TASK_INTERFACE_ID + 15, 254, 274);
        rsinterface.child(22, TASK_INTERFACE_ID + 16, 254, 296);
    }

    public static void unlockInterface(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(UNLOCK_INTERFACE_ID);
        addSprite(UNLOCK_INTERFACE_ID + 1, 0, "Slayer interface/Unlock/1/unlock");

        rsinterface.totalChildren(9);
        rsinterface.child(0, UNLOCK_INTERFACE_ID + 1, 12, 20);
        rsinterface.child(1, CLOSE_BUTTON_1, 472, 27);
        rsinterface.child(2, CLOSE_BUTTON_2, 472, 27);
        rsinterface.child(3, POINTS_STRING_INTERFACE_ID, 455, 61);
        rsinterface.child(4, UNLOCK_INTERFACE_ID + 2, 16, 78);

        rsinterface.child(5, UNLOCK_BUTTON, 52-15-12, 61);
        rsinterface.child(6, EXTEND_BUTTON, 134-12-12, 61);
        rsinterface.child(7, BUY_BUTTON, 228-19-12, 61);
        rsinterface.child(8, TASK_BUTTON, 308-14-12, 61);

        Object[][] unlocks = {
                {20, false}, // imbue slayer helmet
                {6, true}, // malevolent masquerade
                {17, true}, // bigger and badder
                {5, true}, // broad bolt fletching
        };

        RSInterface scrollInterface = addTabInterface(UNLOCK_INTERFACE_ID + 2);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 280 + 182;
        scrollInterface.height = 220;
        int scrollExtension = unlocks.length >= 6 ? (((int) Math.ceil((unlocks.length - 6) / 2d)) * 64) : 0;
        scrollInterface.scrollMax = scrollInterface.height + 1 + scrollExtension;
        int x, y;
        int childrenCount = 0;
        for (Object[] unlock : unlocks)
            childrenCount += ((boolean) unlock[1]) ? 3 : 2;
        scrollInterface.totalChildren(childrenCount);

        int child = 0;
        int interfaceId = UNLOCK_INTERFACE_ID + 3;

        for (int index = 0; index < unlocks.length; index++) {
            x = index % 2 * 227;
            y = index / 2 * 88;

            scrollInterface.child(child++, interfaceId, x + 6, y);
            addHoverButton(interfaceId++, "Slayer interface/Unlock/1/unlock", (int)unlocks[index][0], 224, 64, "Unlock", 0, interfaceId, 1);

            scrollInterface.child(child++, interfaceId, x + 6, y);
            addHoveredButton2(interfaceId++, "Slayer interface/Unlock/2/unlock", (int)unlocks[index][0], 224, 64, interfaceId++);

            if ((boolean) unlocks[index][1]) {
                scrollInterface.child(child++, interfaceId, x + 42 + 6, y + 15);
                addConfigSprite(interfaceId++, 1, "Slayer interface/check", 2, "Slayer interface/check", 1, 880 + index);
            }
        }
    }

    public static void extendInterface(TextDrawingArea[] tda) {
        RSInterface rsinterface = addInterface(EXTEND_INTERFACE_ID);
        addSprite(EXTEND_INTERFACE_ID + 1, 0, "Slayer interface/Extend/1/extend");

        //addHoverButton(47602, "Slayer interface/CLOSE", 1, 21, 21, "Close", -1, 47603, 3);
        //addHoveredButton2(47603, "Slayer interface/CLOSE", 2, 21, 21, 47604);


        rsinterface.totalChildren(9);
        rsinterface.child(0, EXTEND_INTERFACE_ID + 1, 12, 20);
        rsinterface.child(1, CLOSE_BUTTON_1, 472, 27);
        rsinterface.child(2, CLOSE_BUTTON_2, 472, 27);
        rsinterface.child(3, POINTS_STRING_INTERFACE_ID, 455, 61);
        rsinterface.child(4, EXTEND_INTERFACE_ID + 2, 16, 78);

        rsinterface.child(5, UNLOCK_BUTTON, 52-15-12, 61);
        rsinterface.child(6, EXTEND_BUTTON, 134-12-12, 61);
        rsinterface.child(7, BUY_BUTTON, 228-19-12, 61);
        rsinterface.child(8, TASK_BUTTON, 308-14-12, 61);

        int[] extensions = {
                11, // blood veld
                14, // dust devil
                16, // gargoyle
                17, // nech
                19, //kraken
                9, // greater demon
                10, // black demon
        };

        RSInterface scrollInterface = addTabInterface(EXTEND_INTERFACE_ID + 2);
        scrollInterface.scrollPosition = 0;
        scrollInterface.contentType = 0;
        scrollInterface.width = 280 + 182;
        scrollInterface.height = 220;
        scrollInterface.scrollMax = scrollInterface.height + 1 + (((int) Math.ceil((extensions.length - 6) / 2d)) * 64);
        int x, y;
        scrollInterface.totalChildren(extensions.length * 3);
        int interfaceId = EXTEND_INTERFACE_ID + 3;
        int child = 0;

       for (int index = 0; index < extensions.length; index++) {
           x = index % 2 * 227;
           y = index / 2 * 67;
           addHoverButton(interfaceId++, "Slayer interface/Extend/1/extend", extensions[index], 224, 64, "Extend", 0, interfaceId, 1);
           addHoveredButton2(interfaceId++, "Slayer interface/Extend/2/extend", extensions[index], 224, 64, interfaceId++);
           addConfigSprite(interfaceId++, 1, "Slayer interface/check", 2, "Slayer interface/check", 1, 899 + index);
           scrollInterface.child(child++, interfaceId-4, x + 6, y);
           scrollInterface.child(child++, interfaceId-3, x + 6, y);
           scrollInterface.child(child++, interfaceId-1, x + 42 + 6, y + 15);
       }
    }

}
