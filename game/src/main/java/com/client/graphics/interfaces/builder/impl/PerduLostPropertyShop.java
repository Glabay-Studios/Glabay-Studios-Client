package com.client.graphics.interfaces.builder.impl;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;

public class PerduLostPropertyShop extends InterfaceBuilder {

    private final Sprite BACKGROUND = new Sprite("scroll_interface/bg");

    public PerduLostPropertyShop() {
        super(22_992);
    }

    @Override
    public void build() {
        int x = 6;
        int yOffset = 8;
        addSprite(nextInterface(), BACKGROUND);
        child(x, 12 + yOffset);

        RSInterface container = addInterfaceContainer(nextInterface(), 410, 224, 4000);
        container.totalChildren(2);
        child(34, 74 + yOffset);

        addItemContainerAutoScrollable(nextInterface(), 6, 22, 36, 32, true, container.id, "Buy");
        container.child(0, lastInterface(), 20, 0);

        addHorizontalStringContainer(nextInterface(), 6, 36, 32, Client.instance.newSmallFont, true, false, 0x46320A, "");
        container.child(1, lastInterface(), 36, 48);

        addText(nextInterface(), 3, 0x46320A, true, "Lost Property Shop", false);
        child(x + (BACKGROUND.myWidth / 2), 42 + yOffset);

        addNonSpriteButton(nextInterface(), 23, 23, 3, "Close");
        child(455, 45);
    }
}
