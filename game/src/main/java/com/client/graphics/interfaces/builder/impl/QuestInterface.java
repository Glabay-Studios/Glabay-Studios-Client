package com.client.graphics.interfaces.builder.impl;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;

public class QuestInterface extends InterfaceBuilder {

    private final Sprite bg = new Sprite("quest/questbg 0");
    private final Sprite header = new Sprite("quest/questbg 1");


    public QuestInterface() {
        super(45_285);
    }

    @Override
    public void build() {
        addSprite(nextInterface(), bg);
        child(18, 62);

        addSprite(nextInterface(), header);
        child(18, 4);

        addText(nextInterface(), "Quest Name", 0x000000, true, false, 52, RSInterface.defaultTextDrawingAreas, 3);
        child(260, 15);

        // Close buttons
        child(8137, 452, 63);
        child(8138, 452, 63);

        RSInterface container = addInterface(nextInterface());
        container.height = 204;
        container.width = 404;
        container.scrollMax = 218;
        child(50, 93);

        RSInterface stringContainer = addStringContainer(nextInterface(), container.id, Client.instance.newRegularFont, true, 0x000080, false, 18, "j");
        stringContainer.invAutoScrollHeightOffset = 6;
        container.totalChildren(1);
        container.child(0, lastInterface(), 202, 18);
    }
}
