package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.Configuration;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class EnableRoofs implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        switch (optionSelected) {
            case 1:
                Client.removeRoofs = true;
                break;
            case 0:
                Client.removeRoofs = false;
                break;
        }

    }
}
