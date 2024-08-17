package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

import java.awt.*;

public class StretchedModeMenu implements MenuItem {


    public static void updateStretchedMode(boolean stretched) {

    }

    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        boolean bool = optionSelected == 0;
        if (Client.getUserSettings().isStretchedMode() == bool)
            return;
        Client.getUserSettings().setStretchedMode(bool);
        updateStretchedMode(bool);
    }
}
