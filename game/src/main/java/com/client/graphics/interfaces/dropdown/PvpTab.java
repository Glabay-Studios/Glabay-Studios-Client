package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.Configuration;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class PvpTab implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        switch (optionSelected) {
            case 0:
                Client.instance.setSidebarInterface(13, 17800); // pk toplist & killfeed
                break;
            case 1:
                Client.instance.setSidebarInterface(13, 0); // default tab
                break;
        }

    }
}
