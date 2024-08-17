package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class GroundItemOverlayMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        Client.getUserSettings().setGroundItemOverlay(optionSelected == 0);
    }
}
