package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.settings.Setting;

public class GameTimersMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        Client.getUserSettings().setGameTimers(optionSelected == 0);
    }
}
