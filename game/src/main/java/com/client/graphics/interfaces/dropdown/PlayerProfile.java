package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class PlayerProfile implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        switch (optionSelected) {
            case 0:
                Client.instance.setSidebarInterface(13, 41500); // player profile tab
                break;
            case 1:
                Client.instance.setSidebarInterface(13, 0); // default tab
                break;
        }

    }
}
