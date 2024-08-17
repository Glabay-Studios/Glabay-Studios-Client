package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.features.settings.Preferences;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class DragMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        switch (optionSelected) {
            case 0:
                Preferences.getPreferences().dragTime = 5;
                Client.instance.pushMessage("Your drag time has been set to default");
                break;
            case 1:
                Preferences.getPreferences().dragTime = 6;
                Client.instance.pushMessage("Your drag time has been set to 6 (default is 5).");
                break;

            case 2:
                Preferences.getPreferences().dragTime = 8;
                Client.instance.pushMessage("Your drag time has been set to 8 (default is 5).");
                break;

            case 3:
                Preferences.getPreferences().dragTime = 10;
                Client.instance.pushMessage("Your drag time has been set to 10 (default is 5).");
                break;

            case 4:
                Preferences.getPreferences().dragTime = 12;
                Client.instance.pushMessage("Your drag time has been set to 12 (default is 5).");
                break;
        }

    }
}
