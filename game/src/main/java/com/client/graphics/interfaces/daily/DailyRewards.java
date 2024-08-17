package com.client.graphics.interfaces.daily;

import com.client.Configuration;
import com.client.Sprite;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.impl.Interfaces;

public class DailyRewards extends RSInterface {

    private static final DailyRewards SINGLETON = new DailyRewards();

    private static final String SPRITE_DIR = "daily_login/";
    private static final Sprite BG = new Sprite(SPRITE_DIR + "Interface");
    private static final Sprite ITEM_BOX = new Sprite(SPRITE_DIR + "Item Box");
    private static final Sprite ITEM_BOX_SELECTED = new Sprite(SPRITE_DIR + "Item Box Selected");
    private static final Sprite CLAIM_BUTTON = new Sprite(SPRITE_DIR + "Claim Button");
    private static final Sprite CLAIM_BUTTON_HOVER = new Sprite(SPRITE_DIR + "Claim Button Hover");

    private static final int ID = 23_680;
    private static final int COLOR = 0xff9933;
    private static final String COLOR_TEXT = "<col=ff9933>";
    private static final String GREEN = "@gre@";
    private static final int ITEM_BOXES_START_INTERFACE_ID = 23686;
    private static final int ITEM_BOXES_END_INTERFACE_ID = 23717;

    public static DailyRewards get() {
        return SINGLETON;
    }

    private int textInterfaceId;
    private int headerTextInterfaceId;

    private DailyRewards() {}

    private String getParagraph() {
        return COLOR_TEXT + "The " + GREEN + getDayCount() + " days of " + Configuration.CLIENT_TITLE + COLOR_TEXT + " contains a bunch of rewards for\\n"
                + "you to claim! Login every day and claim your reward.\\n\\n\\n"
                + "Note that this is limited to one per user, this means\\n"
                + "you @red@cannot" + COLOR_TEXT + " make multiple accounts to claim rewards.\\n\\n\\n"
                + "Every month has a new reward list, make sure\\n"
                + "to stick around!";
    }

    public void load() {
        int interfaceId = ID;
        int childId = 0;
        RSInterface rsInterface = addInterface(interfaceId++);
        rsInterface.totalChildren(11);

        addSprite(interfaceId, BG);
        rsInterface.child(childId++, interfaceId++, 10, 16);
        rsInterface.child(childId++, Interfaces.CLOSE_BUTTON_SMALL, 482, 26);
        rsInterface.child(childId++, Interfaces.CLOSE_BUTTON_SMALL, 482, 26);

        addText(interfaceId, 2, COLOR, true, Configuration.CLIENT_TITLE + " Daily Rewards");
        rsInterface.child(childId++, interfaceId++, 266, 28);

        // Right panel title
        headerTextInterfaceId = interfaceId;
        addText(interfaceId, 2, COLOR, false, "31 Days of " + Configuration.CLIENT_TITLE);
        rsInterface.child(childId++, interfaceId++, 232, 54);

        // Right panel text
        textInterfaceId = interfaceId;
        addText(interfaceId, 0, COLOR, false, "");
        rsInterface.child(childId++, interfaceId++, 220, 78);

        // Scrollable rewards
        rsInterface.child(childId++, interfaceId, 20, 72);
        interfaceId = itemScrollable(interfaceId);

        addItemContainer(interfaceId, 1, 1, 0, 0, true);
        rsInterface.child(childId++, interfaceId++, 284, 234);

        addItemContainer(interfaceId, 1, 1, 0, 0, true);
        rsInterface.child(childId++, interfaceId++, 394, 234);

        addButton(interfaceId, CLAIM_BUTTON, "Claim prize", 1);
        rsInterface.child(childId++, interfaceId++, 311, 290);

        addText(interfaceId, 0, COLOR, true, "Claim Reward");
        rsInterface.child(childId++, interfaceId++, 311 + 44, 290 + 6);

        get(ID).setNewButtonClicking();
    }

    public void onConfigReceived(int config, int value) {
        if (config == Configs.DAILY_REWARD_STREAK_CONFIG) {
            get(textInterfaceId).message = getParagraph();
            get(headerTextInterfaceId).message = getDayCount() + " Days of " + Configuration.CLIENT_TITLE;
        }
    }

    private int itemScrollable(int interfaceId) {
        RSInterface rsInterface = addInterface(interfaceId++);
        int childId = 0;
        rsInterface.width = 174;
        rsInterface.height = 252;
        rsInterface.scrollMax = 5000;
        rsInterface.invAutoScrollHeightOffset = 6;
        rsInterface.totalChildren(33);

        for (int i = 0; i < 32; i++) {
            int x = 4 + ((i % 4) * 42);
            int y = 4 + ((i / 4) * 42);
            addConfigSprite(interfaceId, ITEM_BOX, ITEM_BOX_SELECTED, i, Configs.DAILY_REWARD_STREAK_CONFIG);
            rsInterface.child(childId++, interfaceId++, x, y);
        }

        addItemContainerAutoScrollable(interfaceId, 4, 8, 10, 10, true, rsInterface.id);
        rsInterface.child(childId++, interfaceId++, 8, 8);

        return interfaceId;
    }

    private int getDayCount() {
        int days = 0;
        for (int interfaceId = ITEM_BOXES_START_INTERFACE_ID; interfaceId <= ITEM_BOXES_END_INTERFACE_ID; interfaceId++) {
            if (!get(interfaceId).interfaceHidden) {
                days++;
            }
        }
        return days;
    }

}
