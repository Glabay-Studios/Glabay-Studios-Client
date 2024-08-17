package com.client.graphics.interfaces.builder.impl.tob;

import com.client.Sprite;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.impl.Interfaces;

public class TobRewardsInterface extends InterfaceBuilder {

    private Sprite bg = new Sprite("tob/reward_bg");

    public TobRewardsInterface() {
        super(22959);
    }

    @Override
    public void build() {
        int x = 122;
        int y = 84;

        addSprite(nextInterface(), bg);
        child(x, y);

        addItemContainer(nextInterface(), 2, 3, 18, 12, true);
        child(x + 20, y + 44);

        child(Interfaces.CLOSE_BUTTON_SMALL, x + 250, y + 8);
        child(Interfaces.CLOSE_BUTTON_SMALL_HOVER, x, y);
    }
}
