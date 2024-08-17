package com.client.graphics.interfaces.builder.impl;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;
import com.client.graphics.interfaces.impl.Interfaces;

public class WildWarning extends InterfaceBuilder {

    private final Sprite BACKGROUND = new Sprite("wildwarning/bg");

    public WildWarning() {
        super(39_960);
    }

    @Override
    public void build() {
        int x = 47;
        int yOffset = -8;
        int centerX = 47 + (BACKGROUND.myWidth / 2);
        addSprite(nextInterface(), BACKGROUND);
        child(x, 44 + yOffset);

        addInterfaceContainer(nextInterface(), 408, 122, 4000);
        child(54, 128 + yOffset);

        RSInterface container = get(lastInterface());
        container.stringContainerContainerExtraScroll = 6;
        container.totalChildren(1);

        addStringContainer(nextInterface(), container.id, Client.instance.newSmallFont, true, RSInterface.DEFAULT_TEXT_COLOR,
                true, 12, "testing long testing long");
        container.child(0, lastInterface(), centerX - x, 12);

        addNonSpriteButton(nextInterface(), 90, 56, "Enter wilderness");
        child(154, 254 + yOffset);

        addNonSpriteButton(nextInterface(), 90, 56, "Don't enter wilderness");
        child(304, 254 + yOffset);

        child(Interfaces.CLOSE_BUTTON_SMALL, 460, 53 + yOffset);
        child(Interfaces.CLOSE_BUTTON_SMALL_HOVER, 460, 53 + yOffset);

        getRoot().setNewButtonClicking();
    }
}
