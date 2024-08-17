package com.client.graphics.interfaces.impl;

import com.client.Configuration;
import com.client.Sprite;
import com.client.StringUtils;
import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;

public class QuestTab extends RSInterface {

    public static final int INTERFACE_ID = 50414; // 586 free ids past this

    public static final int CONFIG_ID = 1355;
    public static final int DEFAULT_SCROLL_MAX = 219;

    public static final int TAB_1 = 10280; // Information
    public static final int TAB_2 = 19050; // Coin tab
    public static final int TAB_3 = 24_500;//29475; // Diaries
    public static final int TAB_4 = 50614; // Quests

    public static final int[] INTERFACE_PLACEHOLDERS = {TAB_1, TAB_2, TAB_3, TAB_4};

    private static final String SPRITE_FOLDER = "/Info Tab/";
    private static final String TAB = SPRITE_FOLDER + "TAB";
    private static final Sprite BIG_BOX = new Sprite(SPRITE_FOLDER + "BIG BOX");
    private static final Sprite SMALL_BOX = new Sprite(SPRITE_FOLDER + "SMALL BOX");
    private static final Sprite TAB_0 = new Sprite(SPRITE_FOLDER + "TAB 0");

    public static final int DEFAULT_CONTAINER_WIDTH = 152;

    private enum Button {
        INFORMATION(1, DEFAULT_SCROLL_MAX + 200),
        COIN(2, DEFAULT_SCROLL_MAX + 120),
        DIARIES(3, 0),
        QUEST_LIST(5, DEFAULT_SCROLL_MAX)
        ;

        private final int spriteId;
        private final int scrollMax;

        Button(int spriteId, int scrollMax) {
            this.spriteId = spriteId;
            this.scrollMax = scrollMax;
        }

        public Sprite getSprite() {
            return new Sprite(SPRITE_FOLDER + "SYMBOL " + spriteId);
        }

        public int getPlaceholderInterface() {
            return INTERFACE_PLACEHOLDERS[ordinal()];
        }
    }

    private static int containerInterfaceId = 0;
    private static int containerChildIndex = 0;
    private static int containerDefaultX = 0;
    private static int containerDefaultY = 0;
    private static int containerDefaultWidth = 0;
    private static int containerDefaultHeight = 0;

    private static int smallBoxInterfaceId = 0;
    private static int smallBoxChildId = 0;

    private static RSInterface master;

    public static void onConfigChanged(int config, int value) {
        if (config == CONFIG_ID) {
            for (Button button : Button.values()) {
                if (value == button.ordinal()) {
                    setContainer(button);
                    return;
                }
            }

            throw new IllegalStateException("No button for value: " + value);
        }
    }

    private static void setContainer(Button button) {
        RSInterface placeholder = RSInterface.interfaceCache[button.getPlaceholderInterface()];
        RSInterface container = RSInterface.interfaceCache[containerInterfaceId];
        container.children = placeholder.children;
        container.childX = placeholder.childX;
        container.childY = placeholder.childY;
        container.scrollMax = button.scrollMax;
        container.scrollPosition = 0;
        container.width = button.scrollMax != 0 ? DEFAULT_CONTAINER_WIDTH : DEFAULT_CONTAINER_WIDTH + 25;

        RSInterface containerChild = master.getChild(containerChildIndex);
        if (button == Button.DIARIES) { // Gotta do some stuff here that doesn't jive with the others
            master.childX[containerChildIndex] = 0;
            master.childY[containerChildIndex] = 25;
            containerChild.width = 255;
            containerChild.height = 255;
            master.children[smallBoxChildId] = RSInterface.emptyInterface;
        } else {
            containerChild.width = containerDefaultWidth;
            containerChild.height = containerDefaultHeight;
            master.childX[containerChildIndex] = containerDefaultX;
            master.childY[containerChildIndex] = containerDefaultY;
            master.children[smallBoxChildId] = smallBoxInterfaceId;
        }
    }

    public void load(TextDrawingArea[] tda) {
        questTab(tda);
        infoTab(tda);
        tab(tda);
        setContainer(Button.INFORMATION);
        questList();
        get(INTERFACE_ID).setNewButtonClicking();
    }

    public void tab(TextDrawingArea[] tda) {
        master = addInterface(INTERFACE_ID);
        setChildren(3 + (Button.values().length * 2), master);
        int childIndex = 0;
        int interfaceId = INTERFACE_ID + 1;

        int x = 5;
        int y = 24;

        addSprite(interfaceId, BIG_BOX);
        master.child(childIndex++, interfaceId++, x, y);

        smallBoxInterfaceId = interfaceId;
        smallBoxChildId = childIndex;

        addSprite(interfaceId, SMALL_BOX);
        master.child(childIndex++, interfaceId++, x + 7, y + 6);

        for (Button button : Button.values()) {
            int tabX = x + (button.ordinal() * TAB_0.myWidth) - button.ordinal();
            int tabY = y + 1 - TAB_0.myHeight;
            addConfigButton(interfaceId, INTERFACE_ID, 0, 1, TAB, TAB_0.myWidth, TAB_0.myHeight, StringUtils.fixName(button.toString().toLowerCase()),
                    button.ordinal(),4, CONFIG_ID);
            RSInterface.interfaceCache[interfaceId].ignoreConfigClicking = true;
            master.child(childIndex++, interfaceId++, tabX, tabY);

            Sprite icon = button.getSprite();
            int iconX = tabX + (TAB_0.myWidth / 2) - (icon.myWidth / 2);
            int iconY = tabY + (TAB_0.myHeight / 2) - (icon.myHeight / 2);
            addSprite(interfaceId, icon);
            master.child(childIndex++, interfaceId++, iconX, iconY);
        }

        containerInterfaceId = interfaceId;
        containerChildIndex = childIndex;
        containerDefaultX = x + 7;
        containerDefaultY = y + 7;
        containerDefaultWidth = 152;
        containerDefaultHeight = 218;

        RSInterface container = addInterface(interfaceId);
        master.child(childIndex++, interfaceId++, containerDefaultX, containerDefaultY);
        container.width = containerDefaultWidth;
        container.height = containerDefaultHeight;
        container.scrollMax = DEFAULT_SCROLL_MAX;
    }

    private void questList() {
        int lines = 20;
        RSInterface inter = addInterface(TAB_4);
        setChildren(lines, inter);

        int y = 8;
        for (int line = 0; line < lines; line++) {
            inter.child(line, TAB_4 + 1 + line, 8, y);
            RSInterface.addClickableText(TAB_4 + 1 + line, "","Open quest journal", RSInterface.defaultTextDrawingAreas, 0, 0xff981f, false,true,140, 16);
            y += 14;
        }

        inter.setNewButtonClicking();
    }

    public static void questTab(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(10220);
        addText(10221, "@or1@Control Panel", tda, 2, 16750899, false, true);
        addText(10222, "@or1@" + Configuration.WEBSITE, tda, 2, 16750899, true, true);
        addSprite(10224, 0, "Interfaces/infoTab/SPRITE");
        addButton(10403, 2, "Interfaces/infoTab/TAB", "Refresh Tab");
        addButton(10404, 4, "Interfaces/infoTab/TAB", "View Diaries");
        addSprite(10223, 1, "Interfaces/infoTab/SPRITE");
        addText(10405, "@cr10@", tda, 2, 16750899, false, true);
        tab.totalChildren(9);
        tab.child(0, 10221, 20, 7);
        tab.child(1, 10222, 95, 240);
        tab.child(2, 10224, 0, 35);
        tab.child(3, 10223, 0, 230);
        tab.child(4, 10403, 172, 15);
        tab.child(5, 10223, 0, 32);
        tab.child(6, 10280, 2, 34);
        tab.child(7, 10404, 154, 15);
        tab.child(8, 10405, 3, 10);
        RSInterface infoList = addTabInterface(10280);
        infoList.height = 196;
        infoList.width = 172;
        //infoList.scrollMax = 700;
        infoList.newScroller = false;

        int size = 35;
        infoList.totalChildren(size);
        for (int id = 0; id < size; id++) {
            int interfaceId = 51901 + id;
            addHoverText(interfaceId, "", "View Details", tda, 0, 1022259, false, true, 150);
            infoList.child(id, interfaceId, 2, 5 + (id * 16));
        }

//        addHoverText(10406, "", "View Details", tda, 0, 1022259, false, true, 150);
//        addHoverText(10407, "", "View Details", tda, 0, 1022259, false, true, 150);
//        addHoverText(10408, "", "View Details", tda, 0, 1022259, false, true, 150);
//        addHoverText(10409, "", "View Details", tda, 0, 1022259, false, true, 150);
//        addHoverText(10410, "", "View Details", tda, 0, 1022259, false, true, 150);
//        addHoverText(10411, "", "View Details", tda, 0, 1022259, false, true, 150);
//        addHoverText(10412, "", "View Details", tda, 0, 1022259, false, true, 150);
//
//        infoList.totalChildren(58);
//        infoList.child(0, 10406, 2, 3);
//        infoList.child(1, 10407, 2, 25);
//        infoList.child(2, 10408, 2, 41);
//        infoList.child(3, 10409, 2, 57);
//        infoList.child(4, 10410, 2, 73);
//        infoList.child(5, 10411, 2, 89);
//        infoList.child(6, 10412, 2, 105);
//
//        int Ypos = 125;
//        int frameID = 7;
//        for (int iD = 10225; iD <= 10275; iD++) {
//            addHoverText(iD, "", "View", tda,0, 16711680, false, true, 150);
//            infoList.child(frameID, iD, 2, Ypos);
//            frameID++;
//            Ypos += 13;
//            Ypos++;
//        }


//        RSInterface aDiary = addTabInterface(29465);
//        try
//        {
//            addText(29466, "@cr17@@or1@ Diaries", tda, 2, 16750899, false, true);
//            addSprite(29467, 0, "Interfaces/infoTab/SPRITE");
//            addButton(29468, 0, "Interfaces/infoTab/TAB", "View Main Information");
//            addButton(29469, 1, "Interfaces/infoTab/TAB", "Quick Load Preset");//updatepreset
//            //addButton(29470, 2, "Interfaces/infoTab/TAB", "Refresh");
//            addButton(29471, 4, "Interfaces/infoTab/TAB", "Main tab");
//            addButton(29472, 2, "Interfaces/infoTab/TAB", "Refresh");
//            addSprite(29473, 1, "Interfaces/infoTab/SPRITE");
//            addText(29474, "@or1@Diaries Completed: @gre@0", tda, 2, 16750899, true, true);
//
//            RSInterface diaryList = addTabInterface(29475);
//            diaryList.height = 196;
//            diaryList.width = 172;
//            diaryList.scrollMax = 222;
//            diaryList.newScroller = false;
//
//            aDiary.totalChildren(8);
//            aDiary.child(0, 29466, 10, 10);
//            aDiary.child(1, 29467, 0, 35);
//            aDiary.child(2, 29473, 0, 230);
//            //aDiary.child(3, 29468, 95, 15);
//            //aDiary.child(4, 29469, 114, 15);
//            //aDiary.child(5, 29470, 133, 15);
//            aDiary.child(3, 29471, 154, 15);
//            aDiary.child(4, 29472, 172, 15);
//            aDiary.child(5, 29473, 0, 32);
//            aDiary.child(6, 29474, 95, 240);
//            aDiary.child(7, 29475, 2, 34);
//
//            diaryList.totalChildren(16);
//            int Ypos2 = 8;
//            int frameID2 = 0;
//            for (int iD2 = 29480; iD2 <= 29495; iD2++) {
//                addHoverText(iD2, "", "View", tda,
//                        0, 16711680, false, true, 150);
//                diaryList.child(frameID2, iD2, 8, Ypos2);
//                frameID2++;
//                Ypos2 += 13;
//                Ypos2++;
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private static class CoinTab {
        private final String string;
        private final int icon;
        private final int x;
        private final int y;

        public CoinTab(String string, int icon, int x, int y) {
            this.string = string;
            this.icon = icon;
            this.x = x;
            this.y = y;
        }
    }

    /**
     * @author Grant_ | www.rune-server.ee/members/grant_ | 10/9/19
     * @param tda
     */
    public static void infoTab(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(47500);
        addText(47501, Configuration.CLIENT_TITLE, tda, 2, 0xff9933, true, true);

        addHoverText(47502, "Call for help", "Call help",tda, 2, 0xff9933, true, true,100);
        addSprite(47503, 0, "Interfaces/infoTab/SPRITE");
        addSprite(47504, 1, "Interfaces/infoTab/SPRITE");

        int childId = 19050;
        RSInterface scroll = addInterface(childId++);
        int scrollFrame = 0;
        scroll.height = 196;
        scroll.width = 174;

        CoinTab[] tabs = {
                new CoinTab("Collection Log", 4, 52, 7),
                new CoinTab("Monster Kill Log", 5, 62, 7),
                new CoinTab("Drop Table", 5, 42, 7),
                new CoinTab("Loot Tables", 6, 46, 4),
                new CoinTab("World Events", 2, 50, 7),
                new CoinTab("Presets", 7, 32, 5),
                new CoinTab("Donator Benefits", 3, 62, 7),
                new CoinTab("Titles", 1, 22, 10),
                new CoinTab("Community Guides", 2, 65, 7),
                new CoinTab("Vote Page", 2, 40, 7),
                new CoinTab("Online Store", 2, 46, 7),
                new CoinTab("Forums", 2, 30, 7),
                new CoinTab(Configuration.CLIENT_TITLE + " Rules", 2, 50, 7),
                new CoinTab("Call For Help", 2, 50, 7),
        };

        scroll.scrollMax = tabs.length * 28;
        scroll.totalChildren(tabs.length * 4);

        int startY = 0;

        for(int i = 0; i < tabs.length; i++) {
            CoinTab coinTab = tabs[i];
            addHoverButton(childId++, "Interfaces/HelpTab/BUTTON", 1, 151, 28, coinTab.string, -1, childId, 1);
            addHoveredButton(childId++, "Interfaces/HelpTab/BUTTON", 0, 151, 28, childId++);

            childId -= 3;

            scroll.child(scrollFrame++, childId++, 2, startY);
            scroll.child(scrollFrame++, childId++, 2, startY);
            childId++;

            addText(childId, coinTab.string, tda, 2, 0xff9933, true, true);
            scroll.child(scrollFrame++, childId++, 68, startY + 5);

            addSprite(childId, coinTab.icon, "Interfaces/HelpTab/ICON");
            scroll.child(scrollFrame++, childId++, 66 + coinTab.x, startY + coinTab.y);

            startY += 28;
        }

        tab.totalChildren(5);
        tab.child(0, 47501, 92, 8);
        tab.child(1, 47502, 43, 240);
        tab.child(2, 47503, 1, 35);
        tab.child(3, 47504, 0, 230);
        tab.child(4, 19050, 0, 34);
    }

}
