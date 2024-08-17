package com.client.graphics.interfaces.builder.impl.tob;

import com.client.Client;
import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;

public class TobPartyOverlay extends InterfaceBuilder {

    private static final Sprite BG = new Sprite("tob/party_overlay_bg");

    public TobPartyOverlay() {
        super(21_473);
    }

    @Override
    public void build() {
        addSprite(nextInterface(), BG);
        getLastChild().drawsTransparent = true;
        getLastChild().opacity = 120;
        child(8, 24);

        addText(nextInterface(), 2, RSInterface.DEFAULT_TEXT_COLOR, true, "Party");
        child(67, 30);

        addStringContainer(nextInterface(), 0, Client.instance.newRegularFont,
                true, RSInterface.DEFAULT_TEXT_COLOR, true, 15, "Testing");
        child(67, 58);
    }
}
