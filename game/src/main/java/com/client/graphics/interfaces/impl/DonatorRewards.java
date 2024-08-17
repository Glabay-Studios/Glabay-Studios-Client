package com.client.graphics.interfaces.impl;

import java.util.List;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;
import com.google.common.collect.Lists;

public class DonatorRewards extends RSInterface {

    public static final int INTERFACE_ID = 22693;

    private static final Sprite BG = new Sprite("donator_rewards/BACKGROUND");
    private static final Sprite BAR = new Sprite("donator_rewards/BAR");
    private static final Sprite BAR_SHADOW = new Sprite("donator_rewards/BAR_SHADOW");
    private static final Sprite BOX = new Sprite("donator_rewards/BOX");
    private static final Sprite BOX_HIGHLIGHTED = new Sprite("donator_rewards/BOX_HIGHLIGHTED");

    private static final DonatorRewards INSTANCE = new DonatorRewards();

    public static DonatorRewards getInstance() {
        return INSTANCE;
    }

    private int progressBarId;
    private int pricingItemContainer;
    private List<RSInterface> progressBars = Lists.newArrayList();

    private DonatorRewards() {}

    public void load() {
        int interfaceId = INTERFACE_ID;
        int childId = 0;
        RSInterface inter = addInterface(interfaceId++);
        inter.totalChildren(15 + (6 * 2));

        addSprite(interfaceId, BG);
        inter.child(childId++, interfaceId++, 64, 84);

        addText(interfaceId, 2, RSInterface.DEFAULT_TEXT_COLOR, true, "Weekly Donation Rewards");
        inter.child(childId++, interfaceId++, 254, 98);

        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL, 428, 98);
        inter.child(childId++, Interfaces.CLOSE_BUTTON_SMALL_HOVER, 428, 98);

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Week ends in 1d 4hrs");
        inter.child(childId++, interfaceId++, 254, 234);

        addSprite(interfaceId, BAR);
        inter.child(childId++, interfaceId++, 91, 196);

        for (int index = 0; index < 6; index++) {
            int y = 156;
            int x = 99 + (index * 57);
            addConfigSprite(interfaceId, BOX, BOX_HIGHLIGHTED, index, Configs.DONATOR_REWARDS_NEXT);
            inter.child(childId++, interfaceId++, x, y);

            addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "$100");
            inter.child(childId++, interfaceId++, x + 18, y - 12);
        }

        interfaceId++;
        interfaceId++;
        interfaceId++;

        addItemContainer(interfaceId, 6, 1, 25, 0, true);
        inter.child(childId++, interfaceId++, 104, 160);

        pricingItemContainer = interfaceId;
        addItemContainer(interfaceId, 6, 1, 0, 0, true);
        interfaceId++;

        progressBars.clear();
        int x = 92;
        for (int index = 0; index < 6; index++) {
            addProgressBarReal(interfaceId, 0, 12, 0x539F53);
            progressBars.add(get(interfaceId));
            inter.child(childId++, interfaceId++, x, 211);

            if (index == 0) {
                x += 27;
            } else {
                x += 56;
            }
        }

        addSprite(interfaceId, BAR_SHADOW);
        get(interfaceId).drawsTransparent = true;
        get(interfaceId).opacity = 70;
        inter.child(childId++, interfaceId++, 92, 211);

        addText(interfaceId, 0, RSInterface.DEFAULT_TEXT_COLOR, true, "$100 / $500");
        inter.child(childId++, interfaceId++, 258, 211);
    }

    public void onConfigChanged(int configId, int value) {
        if (configId == Configs.DONATOR_REWARDS_CURRENT_PROGRESS) {
            Client.instance.variousSettings[Configs.DONATOR_REWARDS_NEXT] = 6;
            for (int index = 0; index < progressBars.size(); index++) {
                int maxWidth = index == 0 ? 27 : 57;
                if (value > priceForIndex(index)) {
                    progressBars.get(index).width = maxWidth;
                } else {
                    int totalPriceDifferential = progressForIndex(value, index);
                    double percentage = (int) (((double) totalPriceDifferential / (double) priceDifferenceForIndex(index)) * 100);
                    if (percentage > 100)
                        percentage = 100;
                    if (percentage < 0)
                        percentage = 0;
                    progressBars.get(index).width = (int) (maxWidth * (percentage / 100d));
                    if (percentage < 100 && Client.instance.variousSettings[Configs.DONATOR_REWARDS_NEXT] == 6) {
                        Client.instance.variousSettings[Configs.DONATOR_REWARDS_NEXT] = index;
                    }
                }
            }
        }

        // Todo determine next item unlock and light it up with the config
        // multiple progress bars

    }

    private int priceForIndex(int index) {
        return get(pricingItemContainer).inventoryAmounts[index];
    }

    private int priceDifferenceForIndex(int index) {
        if (index == 0) {
            return priceForIndex(0);
        }

        return priceForIndex(index) - priceForIndex(index - 1);
    }

    private int progressForIndex(int totalSpent, int index) {
        if (index == 0)
            return totalSpent;
        return totalSpent - priceForIndex(index - 1);
    }
}
